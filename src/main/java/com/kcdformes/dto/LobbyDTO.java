package com.kcdformes.dto;

import java.util.List;

public class LobbyDTO {

    private String lobbyId;
    private String etat;
    private Long partieId;
    private boolean defenseurPret;
    private boolean attaquantPret;
    private boolean defenseurConnecte;
    private boolean attaquantConnecte;
    private List<VagueConfigDTO> vaguesAttaquant;
    private int budgetAttaquant;
    private int budgetRestant;

    public LobbyDTO() {}

    public String getLobbyId() { return lobbyId; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
    public Long getPartieId() { return partieId; }
    public void setPartieId(Long partieId) { this.partieId = partieId; }
    public boolean isDefenseurPret() { return defenseurPret; }
    public void setDefenseurPret(boolean defenseurPret) { this.defenseurPret = defenseurPret; }
    public boolean isAttaquantPret() { return attaquantPret; }
    public void setAttaquantPret(boolean attaquantPret) { this.attaquantPret = attaquantPret; }
    public boolean isDefenseurConnecte() { return defenseurConnecte; }
    public void setDefenseurConnecte(boolean defenseurConnecte) { this.defenseurConnecte = defenseurConnecte; }
    public boolean isAttaquantConnecte() { return attaquantConnecte; }
    public void setAttaquantConnecte(boolean attaquantConnecte) { this.attaquantConnecte = attaquantConnecte; }
    public List<VagueConfigDTO> getVaguesAttaquant() { return vaguesAttaquant; }
    public void setVaguesAttaquant(List<VagueConfigDTO> vaguesAttaquant) { this.vaguesAttaquant = vaguesAttaquant; }
    public int getBudgetAttaquant() { return budgetAttaquant; }
    public void setBudgetAttaquant(int budgetAttaquant) { this.budgetAttaquant = budgetAttaquant; }
    public int getBudgetRestant() { return budgetRestant; }
    public void setBudgetRestant(int budgetRestant) { this.budgetRestant = budgetRestant; }
}