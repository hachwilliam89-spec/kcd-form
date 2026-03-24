package com.kcdformes.factory;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EnnemiFactoryRegistry {

    private final Map<String, EnnemiFactory> factories = new HashMap<>();

    public EnnemiFactoryRegistry(List<EnnemiFactory> factoryList) {
        for (EnnemiFactory factory : factoryList) {
            factories.put(factory.getType().toUpperCase(), factory);
        }
    }

    public EnnemiFactory getFactory(String type) {
        EnnemiFactory factory = factories.get(type.toUpperCase());
        if (factory == null) {
            throw new IllegalArgumentException("Type d'ennemi inconnu : " + type);
        }
        return factory;
    }

    public int getCout(String type) {
        return getFactory(type).getCout();
    }

    public Map<String, Integer> getTousLesCouts() {
        Map<String, Integer> couts = new HashMap<>();
        factories.forEach((type, factory) -> couts.put(type, factory.getCout()));
        return couts;
    }
}