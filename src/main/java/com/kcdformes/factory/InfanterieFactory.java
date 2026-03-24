package com.kcdformes.factory;

import com.kcdformes.model.ennemis.Ennemi;
import com.kcdformes.model.formes.Cercle;
import org.springframework.stereotype.Component;

@Component
public class InfanterieFactory implements EnnemiFactory {

    @Override
    public String getType() { return "CERCLE"; }

    @Override
    public Ennemi creer(String nom, double coeffDifficulte) {
        return new Ennemi(nom, new Cercle("forme", 4), coeffDifficulte);
    }

    @Override
    public int getCout() { return 15; }
}