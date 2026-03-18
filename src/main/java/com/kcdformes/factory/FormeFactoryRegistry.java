package com.kcdformes.factory;

import com.kcdformes.dto.FormeDTO;
import com.kcdformes.model.formes.Forme;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FormeFactoryRegistry {

    private final Map<String, FormeFactory> factories;

    public FormeFactoryRegistry(List<FormeFactory> factoryList) {
        this.factories = factoryList.stream()
                .collect(Collectors.toMap(FormeFactory::getType, f -> f));
    }

    public Forme creer(FormeDTO dto) {
        FormeFactory factory = factories.get(dto.getType().toUpperCase());
        if (factory == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Type de forme inconnu : " + dto.getType());
        }
        return factory.creer(dto.getCouleur(), dto.getValeur1(), dto.getValeur2());
    }


    public Forme creerParType(String type, String couleur, double valeur1, double valeur2) {
        FormeFactory factory = factories.get(type.toUpperCase());
        if (factory == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Type de forme inconnu : " + type);
        }
        return factory.creer(couleur, valeur1, valeur2);
    }



}