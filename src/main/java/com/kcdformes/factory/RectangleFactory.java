package com.kcdformes.factory;

import com.kcdformes.model.formes.Forme;
import com.kcdformes.model.formes.Rectangle;
import org.springframework.stereotype.Component;

@Component
public class RectangleFactory implements FormeFactory {

    @Override
    public Forme creer(String couleur, double valeur1, double valeur2) {
        return new Rectangle(couleur, valeur1, valeur2);
    }

    @Override
    public String getType() {
        return "RECTANGLE";
    }
}