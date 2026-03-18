package com.kcdformes.factory;

import com.kcdformes.model.formes.Forme;

public interface FormeFactory {
    Forme creer(String couleur, double valeur1, double valeur2);
    String getType();
}