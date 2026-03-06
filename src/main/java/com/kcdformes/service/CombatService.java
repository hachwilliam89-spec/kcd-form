package com.kcdformes.service;

import com.kcdformes.dto.CombatEtatDTO;
import com.kcdformes.dto.EnnemiEtatDTO;
import com.kcdformes.entity.TourelleEntity;
import com.kcdformes.model.defense.Forteresse;
import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import com.kcdformes.model.gameplay.*;
import com.kcdformes.model.joueur.Joueur;
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

    private final Map<Long, Partie> partiesEnCours = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> schedulers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

    public CombatService(SimpMessagingTemplate messagingTemplate,
                         PartieRepository partieRepository,
                         TourelleRepository tourelleRepository) {
        this.messagingTemplate = messagingTemplate;
        this.partieRepository = partieRepository;
        this.tourelleRepository = tourelleRepository;
    }

    public void demarrerCombat(Long partieId) {
        if (partiesEnCours.containsKey(partieId)) return;

        var partieEntity = partieRepository.findById(partieId)
                .orElseThrow(() -> new RuntimeException("Partie introuvable"));
        var tourelles = tourelleRepository.findByPartieId(partieId);

        Difficulte difficulte = partieEntity.getDifficulte();
        Joueur joueur = new Joueur(
                partieEntity.getJoueur().getNom(),
                partieEntity.getJoueur().getBudget(),
                3
        );

        // Construire la carte et le chemin
        Carte carte = new Carte("Terrain", 10, 6);
        List<Integer> chemin = new ArrayList<>();
        for (int i = 0; i < 21; i++) chemin.add(i);
        carte.setChemin(chemin);

        // Emplacements de tourelles disponibles
        List<Integer> emplacements = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        carte.setEmplacementsTourelles(emplacements);

        // Construire la partie métier
        Partie partie = new Partie(difficulte, joueur, carte);

        // Ajouter les tourelles du joueur depuis la BDD
        for (TourelleEntity te : tourelles) {
            Tourelle t = new Tourelle(te.getNom(), te.getPosition());
            t.setPortee(te.getPortee());
            if (te.getNombreTirs() > 0) {
                t.ajouterForme(new Triangle("rouge", 4.0, 3.0));
            }
            if (te.isAoe()) {
                double rayon = te.getRayonAoe() > 0 ? te.getRayonAoe() : 3.0;
                t.ajouterForme(new Cercle("bleu", rayon));
            }
            if (te.getPv() > 0) {
                t.ajouterForme(new Rectangle("gris", 5.0, 3.0));
            }
            carte.placerTourelle(t, t.getPosition());
        }

        // Générer les vagues
        int nbVagues = difficulte.getNombreVagues();
        int duree = difficulte.getDureeVagueSecondes();
        double coeffBase = difficulte.getNiveau();

        for (int i = 0; i < nbVagues; i++) {
            Vague v = new Vague(i + 1, coeffBase, duree);
            if (i == nbVagues - 1) v.setDerniereVague(true);
            int coeff = i + 1;
            // Béliers uniquement à partir de la vague 3
            int nbBeliers = (i >= 2) ? coeff : 0;
            v.genererEscouades(3 * coeff, 2 * coeff, nbBeliers);
            partie.ajouterVague(v);
        }

        partie.demarrer();
        partiesEnCours.put(partieId, partie);

        // Tick chaque seconde
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            try {
                partie.update();
                envoyerEtat(partieId, partie);

                if (partie.getEtat() == EtatPartie.GAGNE
                        || partie.getEtat() == EtatPartie.PERDU) {
                    arreterCombat(partieId, partie.getEtat());
                }
            } catch (Exception e) {
                System.err.println("Erreur tick combat: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);

        schedulers.put(partieId, future);
    }

    private void arreterCombat(Long partieId, EtatPartie etat) {
        ScheduledFuture<?> future = schedulers.remove(partieId);
        if (future != null) future.cancel(false);
        partiesEnCours.remove(partieId);

        partieRepository.findById(partieId).ifPresent(p -> {
            p.setEtat(etat);
            partieRepository.save(p);
        });
    }

    private void envoyerEtat(Long partieId, Partie partie) {
        int vagueIdx = partie.getVagueActuelle();
        List<Vague> vagues = partie.getVagues();

        if (vagueIdx >= vagues.size()) return;

        Vague vagueActuelle = vagues.get(vagueIdx);
        List<EnnemiEtatDTO> ennemisDTO = new ArrayList<>();

        for (Ennemi e : vagueActuelle.getEnnemisActifs()) {
            ennemisDTO.add(new EnnemiEtatDTO(
                    e.getNom(),
                    e.getPosition(),
                    e.getPvActuels(),
                    e.getPvMax(),
                    e.estVivant(),
                    e.getForme().getClass().getSimpleName()
            ));
        }

        Forteresse forteresse = partie.getForteresse();
        CombatEtatDTO etatDTO = new CombatEtatDTO(
                vagueIdx + 1,
                partie.getEtat().name(),
                forteresse.getPvActuels(),
                forteresse.getPvMax(),
                partie.getJoueur().getScore(),
                vagueActuelle.getNombreVivants(),
                vagueActuelle.getNombreEnnemis(),
                ennemisDTO
        );

        messagingTemplate.convertAndSend("/topic/combat/" + partieId, etatDTO);
    }
}