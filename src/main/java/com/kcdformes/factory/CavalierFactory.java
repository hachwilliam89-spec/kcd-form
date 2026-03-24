package com.kcdformes.factory;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Triangle;
import org.springframework.stereotype.Component;

@Component
public class CavalierFactory implements EnnemiFactory {

    @Override
    public String getType() { return "TRIANGLE"; }

    @Override
    public Ennemi creer(String nom, double coeffDifficulte) {
        return new Ennemi(nom, new Triangle("forme", 8, 6), coeffDifficulte);
    }

    @Override
    public int getCout() { return 30; }
}