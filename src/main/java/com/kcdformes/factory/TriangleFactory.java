package com.kcdformes.factory;

import com.kcdformes.model.formes.Forme;
import com.kcdformes.model.formes.Triangle;
import org.springframework.stereotype.Component;

@Component
public class TriangleFactory implements FormeFactory {

    @Override
    public Forme creer(String couleur, double valeur1, double valeur2) {
        return new Triangle(couleur, valeur1, valeur2);
    }

    @Override
    public String getType() {
        return "TRIANGLE";
    }
}