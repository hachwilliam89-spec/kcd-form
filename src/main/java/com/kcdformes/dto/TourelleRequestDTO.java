package com.kcdformes.dto;

import java.util.List;

public class TourelleRequestDTO {
    private String nom;
    private int position;
    private int portee;
    private List<FormeDTO> formes;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public int getPortee() { return portee; }
    public void setPortee(int portee) { this.portee = portee; }
    public List<FormeDTO> getFormes() { return formes; }
    public void setFormes(List<FormeDTO> formes) { this.formes = formes; }
}