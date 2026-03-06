package com.kcdformes.entity;

import com.kcdformes.model.gameplay.Difficulte;
import com.kcdformes.model.gameplay.EtatPartie;
import jakarta.persistence.*;

@Entity
@Table(name = "parties")
public class PartieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Difficulte difficulte;

    @Enumerated(EnumType.STRING)
    private EtatPartie etat;

    private int vagueActuelle;

    @ManyToOne
    @JoinColumn(name = "joueur_id")
    private JoueurEntity joueur;

    public PartieEntity() {}

    public PartieEntity(Difficulte difficulte, JoueurEntity joueur) {
        this.difficulte = difficulte;
        this.joueur = joueur;
        this.etat = EtatPartie.EN_PAUSE;
        this.vagueActuelle = 0;
    }

    public Long getId() { return id; }
    public Difficulte getDifficulte() { return difficulte; }
    public EtatPartie getEtat() { return etat; }
    public void setEtat(EtatPartie etat) { this.etat = etat; }
    public int getVagueActuelle() { return vagueActuelle; }
    public void setVagueActuelle(int vagueActuelle) { this.vagueActuelle = vagueActuelle; }
    public JoueurEntity getJoueur() { return joueur; }
}