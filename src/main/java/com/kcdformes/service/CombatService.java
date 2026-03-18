package com.kcdformes.service;

import com.kcdformes.dto.CombatEtatDTO;
import com.kcdformes.dto.EnnemiEtatDTO;
import com.kcdformes.dto.MurailleEtatDTO;
import com.kcdformes.entity.MurailleEntity;
import com.kcdformes.entity.TourelleEntity;
import com.kcdformes.factory.FormeFactoryRegistry;
import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.gameplay.*;
import com.kcdformes.model.joueur.Joueur;
import com.kcdformes.repository.MurailleRepository;
import com.kcdformes.repository.PartieRepository;
import com.kcdformes.repository.TourelleRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class CombatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final PartieRepository partieRepository;
    private final TourelleRepository tourelleRepository;
    private final MurailleRepository murailleRepository;
    private final FormeFactoryRegistry factoryRegistry;

    private final Map<Long, Partie> partiesEnCours = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> schedulers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

    public CombatService(SimpMessagingTemplate messagingTemplate,
                         PartieRepository partieRepository,
                         TourelleRepository tourelleRepository,
                         MurailleRepository murailleRepository,
                         FormeFactoryRegistry factoryRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.partieRepository = partieRepository;
        this.tourelleRepository = tourelleRepository;
        this.murailleRepository = murailleRepository;
        this.factoryRegistry = factoryRegistry;
    }

    public void demarrerCombat(Long partieId) {
        // Si la partie existe déjà en mémoire (reprise après ENTRE_VAGUES)
        Partie partieExistante = partiesEnCours.get(partieId);
        if (partieExistante != null) {
            if (partieExistante.getEtat() == EtatPartie.ENTRE_VAGUES) {
                reprendreCombat(partieId, partieExistante);
            }
            return;
        }

        // Première fois : créer la partie
        var partieEntity = partieRepository.findById(partieId)
                .orElseThrow(() -> new RuntimeException("Partie introuvable"));
        var tourelles = tourelleRepository.findByPartieId(partieId);
        var muraillesEntity = murailleRepository.findByPartieId(partieId);

        Difficulte difficulte = partieEntity.getDifficulte();
        Joueur joueur = new Joueur(
                partieEntity.getJoueur().getNom(),
                partieEntity.getJoueur().getBudget(),
                3
        );

        Carte carte = new Carte("Terrain", 10, 6);
        List<Integer> chemin = new ArrayList<>();
        for (int i = 0; i < 21; i++) chemin.add(i);
        carte.setChemin(chemin);

        List<Integer> emplacements = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        carte.setEmplacementsTourelles(emplacements);

        Partie partie = new Partie(difficulte, joueur, carte);

        // Charger les tourelles
        chargerTourelles(carte, tourelles);

        // Charger les murailles
        for (MurailleEntity me : muraillesEntity) {
            partie.ajouterMuraille(me.getPosition(), me.getLargeur(), me.getLongueur(), me.getPvMax());
        }

        // Générer les vagues
        int nbVagues = difficulte.getNombreVagues();
        int duree = difficulte.getDureeVagueSecondes();
        double coeffBase = difficulte.getNiveau();

        for (int i = 0; i < nbVagues; i++) {
            Vague v = new Vague(i + 1, coeffBase, duree);
            if (i == nbVagues - 1) v.setDerniereVague(true);
            int coeff = i + 1;
            int nbBeliers = (i >= 2) ? coeff : 0;
            v.genererEscouades(3 * coeff, 2 * coeff, nbBeliers);
            partie.ajouterVague(v);
        }

        partie.demarrer();
        partiesEnCours.put(partieId, partie);
        lancerScheduler(partieId, partie);
    }

    public boolean peutReprendre(Long partieId) {
        Partie partie = partiesEnCours.get(partieId);
        return partie != null && partie.getEtat() == EtatPartie.ENTRE_VAGUES;
    }

    private void reprendreCombat(Long partieId, Partie partie) {
        // Recharger les tourelles (le joueur a pu en ajouter)
        var tourellesEntity = tourelleRepository.findByPartieId(partieId);
        Carte carte = partie.getCarte();
        // Vider les tourelles existantes et recharger
        for (var t : new ArrayList<>(carte.getTourelles())) {
            carte.supprimerTourelle(t.getPosition());
        }
        chargerTourelles(carte, tourellesEntity);

        // Recharger les murailles (le joueur a pu en ajouter)
        var muraillesEntity = murailleRepository.findByPartieId(partieId);
        for (MurailleEntity me : muraillesEntity) {
            if (!partie.getMurailles().containsKey(me.getPosition())) {
                partie.ajouterMuraille(me.getPosition(), me.getLargeur(), me.getLongueur(), me.getPvMax());
            }
        }

        partie.reprendre();
        lancerScheduler(partieId, partie);
    }

    private void chargerTourelles(Carte carte, List<TourelleEntity> tourelles) {
        for (TourelleEntity te : tourelles) {
            Tourelle t = new Tourelle(te.getNom(), te.getPosition());
            t.setPortee(te.getPortee());
            if (te.getNombreTirs() > 0) {
                t.ajouterForme(factoryRegistry.creerParType("TRIANGLE", "rouge", 4.0, 3.0));
            }
            if (te.isAoe()) {
                double rayon = te.getRayonAoe() > 0 ? te.getRayonAoe() : 3.0;
                t.ajouterForme(factoryRegistry.creerParType("CERCLE", "bleu", rayon, 0.0));
            }
            if (te.getPv() > 0) {
                t.ajouterForme(factoryRegistry.creerParType("RECTANGLE", "gris", 5.0, 3.0));
            }
            carte.placerTourelle(t, t.getPosition());
        }
    }

    private void lancerScheduler(Long partieId, Partie partie) {
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            try {
                partie.update();
                envoyerEtat(partieId, partie);

                EtatPartie etat = partie.getEtat();
                if (etat == EtatPartie.GAGNE || etat == EtatPartie.PERDU) {
                    arreterScheduler(partieId);
                    sauvegarderEtat(partieId, etat);
                } else if (etat == EtatPartie.ENTRE_VAGUES) {
                    arreterScheduler(partieId);
                    sauvegarderEtat(partieId, etat);
                }
            } catch (Exception e) {
                System.err.println("Erreur tick combat: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);

        schedulers.put(partieId, future);
    }

    private void arreterScheduler(Long partieId) {
        ScheduledFuture<?> future = schedulers.remove(partieId);
        if (future != null) future.cancel(false);
    }

    private void sauvegarderEtat(Long partieId, EtatPartie etat) {
        Partie partie = partiesEnCours.get(partieId);

        partieRepository.findById(partieId).ifPresent(p -> {
            p.setEtat(etat);

            if (partie != null && (etat == EtatPartie.GAGNE || etat == EtatPartie.PERDU)) {

                int totalEnnemis = 0;
                int totalElimines = 0;
                for (Vague v : partie.getVagues()) {
                    totalEnnemis += v.getNombreEnnemis();
                    totalElimines += (v.getNombreEnnemis() - v.getNombreVivants());
                }

                int pvRestants = partie.getForteresse().getPvActuels();
                int pvMax = partie.getForteresse().getPvMax();

                int budgetInitial = partie.getDifficulte().getBudgetInitial();
                int budgetRestant = partie.getJoueur().getBudget();
                int orDepense = budgetInitial - budgetRestant;

                double scoreRemparts = pvMax > 0 ? ((double) pvRestants / pvMax) * 30 : 0;
                double scoreEnnemis = totalEnnemis > 0 ? ((double) totalElimines / totalEnnemis) * 40 : 0;
                double scoreBudget = budgetInitial > 0 ? (1.0 - (double) orDepense / budgetInitial) * 30 : 0;
                int scoreFinal = (int) Math.round(scoreRemparts + scoreEnnemis + scoreBudget);

                int etoiles = 0;
                if (etat == EtatPartie.GAGNE) {
                    if (scoreFinal >= 80) etoiles = 3;
                    else if (scoreFinal >= 50) etoiles = 2;
                    else if (scoreFinal > 0) etoiles = 1;
                }

                p.setScoreFinal(scoreFinal);
                p.setEtoiles(etoiles);
                p.setEnnemisElimines(totalElimines);
                p.setEnnemisTotal(totalEnnemis);
                p.setForteressePvRestants(pvRestants);
                p.setForteressePvMax(pvMax);
                p.setOrDepense(orDepense);
            }

            partieRepository.save(p);
        });

        if (etat == EtatPartie.GAGNE || etat == EtatPartie.PERDU) {
            partiesEnCours.remove(partieId);
        }
    }

    private void envoyerEtat(Long partieId, Partie partie) {
        int vagueIdx = partie.getVagueActuelle();
        List<Vague> vagues = partie.getVagues();

        if (vagueIdx >= vagues.size()) return;

        Vague vagueActuelle = vagues.get(vagueIdx);
        List<EnnemiEtatDTO> ennemisDTO = new ArrayList<>();

        for (Ennemi e : vagueActuelle.getEnnemisActifs()) {
            ennemisDTO.add(new EnnemiEtatDTO(
                    System.identityHashCode(e),
                    e.getNom(),
                    e.getPosition(),
                    e.getPvActuels(),
                    e.getPvMax(),
                    e.estVivant(),
                    e.getForme().getClass().getSimpleName()
            ));
        }

        List<MurailleEtatDTO> muraillesDTO = new ArrayList<>();
        for (Partie.MurailleEnJeu m : partie.getMurailles().values()) {
            muraillesDTO.add(new MurailleEtatDTO(
                    m.getPosition(),
                    m.getPvActuels(),
                    m.getPvMax(),
                    m.estDetruite()
            ));
        }

        var forteresse = partie.getForteresse();
        CombatEtatDTO etatDTO = new CombatEtatDTO(
                vagueIdx + 1,
                partie.getEtat().name(),
                forteresse.getPvActuels(),
                forteresse.getPvMax(),
                partie.getJoueur().getScore(),
                vagueActuelle.getNombreVivants(),
                vagueActuelle.getNombreEnnemis(),
                vagueActuelle.getTempsEcoule(),
                vagueActuelle.getDureeSecondes(),
                partie.estDerniereVague(),
                ennemisDTO,
                muraillesDTO
        );

        messagingTemplate.convertAndSend("/topic/combat/" + partieId, etatDTO);
    }
}