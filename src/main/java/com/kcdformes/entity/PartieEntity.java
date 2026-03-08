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
    private int scoreFinal;
    private int etoiles;
    private int ennemisElimines;
    private int ennemisTotal;
    private int forteressePvRestants;
    private int forteressePvMax;
    private int orDepense;

    @ManyToOne
    @JoinColumn(name = "joueur_id")
    private JoueurEntity joueur;

    public PartieEntity() {}

    public PartieEntity(Difficulte difficulte, JoueurEntity joueur) {
        this.difficulte = difficulte;
        this.joueur = joueur;
        this.etat = EtatPartie.EN_PAUSE;
        this.vagueActuelle = 0;
        this.scoreFinal = 0;
        this.etoiles = 0;
        this.ennemisElimines = 0;
        this.ennemisTotal = 0;
        this.forteressePvRestants = 0;
        this.forteressePvMax = 0;
        this.orDepense = 0;
    }

    public Long getId() { return id; }
    public Difficulte getDifficulte() { return difficulte; }
    public EtatPartie getEtat() { return etat; }
    public void setEtat(EtatPartie etat) { this.etat = etat; }
    public int getVagueActuelle() { return vagueActuelle; }
    public void setVagueActuelle(int vagueActuelle) { this.vagueActuelle = vagueActuelle; }
    public JoueurEntity getJoueur() { return joueur; }

    public int getScoreFinal() { return scoreFinal; }
    public void setScoreFinal(int scoreFinal) { this.scoreFinal = scoreFinal; }
    public int getEtoiles() { return etoiles; }
    public void setEtoiles(int etoiles) { this.etoiles = etoiles; }
    public int getEnnemisElimines() { return ennemisElimines; }
    public void setEnnemisElimines(int ennemisElimines) { this.ennemisElimines = ennemisElimines; }
    public int getEnnemisTotal() { return ennemisTotal; }
    public void setEnnemisTotal(int ennemisTotal) { this.ennemisTotal = ennemisTotal; }
    public int getForteressePvRestants() { return forteressePvRestants; }
    public void setForteressePvRestants(int forteressePvRestants) { this.forteressePvRestants = forteressePvRestants; }
    public int getForteressePvMax() { return forteressePvMax; }
    public void setForteressePvMax(int forteressePvMax) { this.forteressePvMax = forteressePvMax; }
    public int getOrDepense() { return orDepense; }
    public void setOrDepense(int orDepense) { this.orDepense = orDepense; }
}