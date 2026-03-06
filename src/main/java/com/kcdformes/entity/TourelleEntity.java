package com.kcdformes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tourelles")
public class TourelleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int position;
    private int portee;
    private int nombreTirs;
    private boolean aoe;
    private double rayonAoe;
    private double dps;
    private int pv;
    private int cout;

    @ManyToOne
    @JoinColumn(name = "partie_id")
    private PartieEntity partie;

    public TourelleEntity() {}

    public TourelleEntity(String nom, int position, int portee, int nombreTirs,
                          boolean aoe, double rayonAoe, double dps, int pv, int cout,
                          PartieEntity partie) {
        this.nom = nom;
        this.position = position;
        this.portee = portee;
        this.nombreTirs = nombreTirs;
        this.aoe = aoe;
        this.rayonAoe = rayonAoe;
        this.dps = dps;
        this.pv = pv;
        this.cout = cout;
        this.partie = partie;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public int getPosition() { return position; }
    public int getPortee() { return portee; }
    public int getNombreTirs() { return nombreTirs; }
    public boolean isAoe() { return aoe; }
    public double getRayonAoe() { return rayonAoe; }
    public double getDps() { return dps; }
    public int getPv() { return pv; }
    public int getCout() { return cout; }
    public PartieEntity getPartie() { return partie; }
}