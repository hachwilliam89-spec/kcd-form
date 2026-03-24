package com.kcdformes.factory;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Rectangle;
import org.springframework.stereotype.Component;

@Component
public class BelierFactory implements EnnemiFactory {

    @Override
    public String getType() { return "RECTANGLE"; }

    @Override
    public Ennemi creer(String nom, double coeffDifficulte) {
        return new Ennemi(nom, new Rectangle("forme", 10, 6), coeffDifficulte);
    }

    @Override
    public int getCout() { return 50; }
}