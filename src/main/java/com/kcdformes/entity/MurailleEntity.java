package com.kcdformes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "murailles")
public class MurailleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int position;
    private double largeur;
    private double longueur;
    private int pvMax;
    private int pvActuels;
    private int cout;

    @ManyToOne
    @JoinColumn(name = "partie_id")
    private PartieEntity partie;

    public MurailleEntity() {}

    public MurailleEntity(int position, double largeur, double longueur, int pvMax, int cout, PartieEntity partie) {
        this.position = position;
        this.largeur = largeur;
        this.longueur = longueur;
        this.pvMax = pvMax;
        this.pvActuels = pvMax;
        this.cout = cout;
        this.partie = partie;
    }

    public Long getId() { return id; }
    public int getPosition() { return position; }
    public double getLargeur() { return largeur; }
    public double getLongueur() { return longueur; }
    public int getPvMax() { return pvMax; }
    public int getPvActuels() { return pvActuels; }
    public void setPvActuels(int pvActuels) { this.pvActuels = pvActuels; }
    public int getCout() { return cout; }
    public PartieEntity getPartie() { return partie; }
}