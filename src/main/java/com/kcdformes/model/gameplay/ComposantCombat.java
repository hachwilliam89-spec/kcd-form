package com.kcdformes.model.gameplay;

public interface ComposantCombat {
    int getPvActuels();
    int getPvMax();
    boolean estVivant();
    void subirDegats(double degats);
    int getNombreVivants();
    int getNombreUnites();
}