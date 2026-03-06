package com.kcdformes.config;

import com.kcdformes.entity.JoueurEntity;
import com.kcdformes.entity.PartieEntity;
import com.kcdformes.entity.TourelleEntity;
import com.kcdformes.model.defense.Tourelle;
import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Rectangle;
import com.kcdformes.model.formes.Triangle;
import com.kcdformes.repository.JoueurRepository;
import com.kcdformes.repository.PartieRepository;
import com.kcdformes.repository.TourelleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.kcdformes.model.gameplay.Difficulte;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JoueurRepository joueurRepository;
    private final PartieRepository partieRepository;
    private final TourelleRepository tourelleRepository;

    public DataInitializer(JoueurRepository joueurRepository,
                           PartieRepository partieRepository,
                           TourelleRepository tourelleRepository) {
        this.joueurRepository = joueurRepository;
        this.partieRepository = partieRepository;
        this.tourelleRepository = tourelleRepository;
    }

    @Override
    public void run(String... args) {

        // Joueur de démonstration
        JoueurEntity joueur = new JoueurEntity("Démo", 400, 3);
        joueurRepository.save(joueur);

        // Composition 1 : vide
        PartieEntity partieVide = new PartieEntity(Difficulte.CHEVALIER, joueur);
        partieRepository.save(partieVide);

// Composition 2 : avec 4 tourelles
        PartieEntity partieDemo = new PartieEntity(Difficulte.ECUYER, joueur);
        partieRepository.save(partieDemo);

        // Tourelle 1 : Triangle seul (archer)
        Tourelle t1 = new Tourelle("Tour Archer", 1);
        t1.ajouterForme(new Triangle("rouge", 4.0, 3.0));
        tourelleRepository.save(new TourelleEntity(
                "Tour Archer", 1, 3,
                t1.getNombreTirs(), t1.hasAoE(), t1.getRayonZone(),
                t1.dpsTotal(), (int) t1.getPV(), t1.coutTotal(),
                partieDemo
        ));

        // Tourelle 2 : Cercle seul (catapulte AoE)
        Tourelle t2 = new Tourelle("Catapulte", 2);
        t2.ajouterForme(new Cercle("bleu", 3.0));
        tourelleRepository.save(new TourelleEntity(
                "Catapulte", 2, 3,
                t2.getNombreTirs(), t2.hasAoE(), t2.getRayonZone(),
                t2.dpsTotal(), (int) t2.getPV(), t2.coutTotal(),
                partieDemo
        ));

        // Tourelle 3 : Rectangle seul (muraille)
        Tourelle t3 = new Tourelle("Grande Muraille", 3);
        t3.ajouterForme(new Rectangle("gris", 5.0, 3.0));
        tourelleRepository.save(new TourelleEntity(
                "Grande Muraille", 3, 3,
                t3.getNombreTirs(), t3.hasAoE(), t3.getRayonZone(),
                t3.dpsTotal(), (int) t3.getPV(), t3.coutTotal(),
                partieDemo
        ));

        // Tourelle 4 : Triangle + Cercle + Rectangle (tourelle composée)
        Tourelle t4 = new Tourelle("Forteresse Suprême", 4);
        t4.ajouterForme(new Triangle("rouge", 4.0, 3.0));
        t4.ajouterForme(new Cercle("bleu", 3.0));
        t4.ajouterForme(new Rectangle("gris", 5.0, 3.0));
        tourelleRepository.save(new TourelleEntity(
                "Forteresse Suprême", 4, 3,
                t4.getNombreTirs(), t4.hasAoE(), t4.getRayonZone(),
                t4.dpsTotal(), (int) t4.getPV(), t4.coutTotal(),
                partieDemo
        ));

        System.out.println("DataInitializer : 2 compositions créées (1 vide, 1 avec 4 tourelles)");
    }
}