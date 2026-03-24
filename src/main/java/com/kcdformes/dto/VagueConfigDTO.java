package com.kcdformes.dto;

import java.util.List;

public class VagueConfigDTO {

    private int numero;
    private List<UniteConfig> unites;

    public VagueConfigDTO() {}

    public VagueConfigDTO(int numero, List<UniteConfig> unites) {
        this.numero = numero;
        this.unites = unites;
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public List<UniteConfig> getUnites() { return unites; }
    public void setUnites(List<UniteConfig> unites) { this.unites = unites; }

    public static class UniteConfig {
        private String type;
        private int quantite;

        public UniteConfig() {}

        public UniteConfig(String type, int quantite) {
            this.type = type;
            this.quantite = quantite;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public int getQuantite() { return quantite; }
        public void setQuantite(int quantite) { this.quantite = quantite; }
    }
}