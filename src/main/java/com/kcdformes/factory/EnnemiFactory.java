package com.kcdformes.factory;

import com.kcdformes.model.ennemis.Ennemi;

public interface EnnemiFactory {
    String getType(); // "TRIANGLE", "CERCLE", "RECTANGLE"
    Ennemi creer(String nom, double coeffDifficulte);
    int getCout(); // coût budget pour le mode multi
}