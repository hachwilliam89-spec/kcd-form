package com.kcdformes.factory;

import com.kcdformes.model.formes.Cercle;
import com.kcdformes.model.formes.Forme;
import org.springframework.stereotype.Component;

@Component
public class CercleFactory implements FormeFactory {

    @Override
    public Forme creer(String couleur, double valeur1, double valeur2) {
        return new Cercle(couleur, valeur1);
    }

    @Override
    public String getType() {
        return "CERCLE";
    }
}