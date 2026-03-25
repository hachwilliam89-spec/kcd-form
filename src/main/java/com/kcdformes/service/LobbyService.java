package com.kcdformes.service;

import com.kcdformes.dto.LobbyDTO;
import com.kcdformes.dto.VagueConfigDTO;
import com.kcdformes.factory.EnnemiFactoryRegistry;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LobbyService {

    private final SimpMessagingTemplate messagingTemplate;
    private final EnnemiFactoryRegistry ennemiFactoryRegistry;
    private final Map<String, Lobby> lobbies = new ConcurrentHashMap<>();

    public LobbyService(SimpMessagingTemplate messagingTemplate,
                        EnnemiFactoryRegistry ennemiFactoryRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.ennemiFactoryRegistry = ennemiFactoryRegistry;
    }

    public String creerLobby(Long partieId, int nbVagues) {
        String lobbyId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Lobby lobby = new Lobby(lobbyId, partieId, nbVagues);
        lobbies.put(lobbyId, lobby);
        return lobbyId;
    }

    public LobbyDTO rejoindre(String lobbyId, String role) {
        Lobby lobby = getLobby(lobbyId);
        if ("DEFENSEUR".equals(role)) {
            lobby.defenseurConnecte = true;
        } else if ("ATTAQUANT".equals(role)) {
            lobby.attaquantConnecte = true;
        }
        broadcastLobby(lobby);
        return toDTO(lobby);
    }

    public LobbyDTO configurerVagues(String lobbyId, List<VagueConfigDTO> vagues) {
        Lobby lobby = getLobby(lobbyId);

        int coutTotal = 0;
        for (VagueConfigDTO vague : vagues) {
            for (VagueConfigDTO.UniteConfig unite : vague.getUnites()) {
                int cout = ennemiFactoryRegistry.getCout(unite.getType());
                coutTotal += cout * unite.getQuantite();
            }
        }

        if (coutTotal > lobby.budgetAttaquant) {
            throw new IllegalArgumentException(
                    "Budget dépassé : " + coutTotal + " / " + lobby.budgetAttaquant + " or disponible."
            );
        }

        lobby.vaguesAttaquant = vagues;
        lobby.budgetRestant = lobby.budgetAttaquant - coutTotal;
        broadcastLobby(lobby);
        return toDTO(lobby);
    }

    public void marquerEnCours(String lobbyId) {
        Lobby lobby = getLobby(lobbyId);
        lobby.etat = "EN_COURS";
        broadcastLobby(lobby);
    }

    public LobbyDTO marquerPret(String lobbyId, String role) {
        Lobby lobby = getLobby(lobbyId);
        if ("DEFENSEUR".equals(role)) {
            lobby.defenseurPret = true;
        } else if ("ATTAQUANT".equals(role)) {
            if (lobby.vaguesAttaquant == null || lobby.vaguesAttaquant.isEmpty()) {
                throw new IllegalArgumentException("L'attaquant doit configurer ses vagues avant d'être prêt.");
            }
            lobby.attaquantPret = true;
        }

        if (lobby.defenseurPret && lobby.attaquantPret) {
            lobby.etat = "PRET";
        }

        broadcastLobby(lobby);
        return toDTO(lobby);
    }

    public boolean estPret(String lobbyId) {
        Lobby lobby = lobbies.get(lobbyId);
        return lobby != null && "PRET".equals(lobby.etat);
    }

    public Lobby getLobby(String lobbyId) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Lobby introuvable : " + lobbyId);
        }
        return lobby;
    }

    public LobbyDTO getEtat(String lobbyId) {
        return toDTO(getLobby(lobbyId));
    }

    public void supprimerLobby(String lobbyId) {
        lobbies.remove(lobbyId);
    }

    private void broadcastLobby(Lobby lobby) {
        messagingTemplate.convertAndSend("/topic/lobby/" + lobby.lobbyId, toDTO(lobby));
    }

    private LobbyDTO toDTO(Lobby lobby) {
        LobbyDTO dto = new LobbyDTO();
        dto.setLobbyId(lobby.lobbyId);
        dto.setEtat(lobby.etat);
        dto.setPartieId(lobby.partieId);
        dto.setDefenseurPret(lobby.defenseurPret);
        dto.setAttaquantPret(lobby.attaquantPret);
        dto.setDefenseurConnecte(lobby.defenseurConnecte);
        dto.setAttaquantConnecte(lobby.attaquantConnecte);
        dto.setVaguesAttaquant(lobby.vaguesAttaquant);
        dto.setBudgetAttaquant(lobby.budgetAttaquant);
        dto.setBudgetRestant(lobby.budgetRestant);
        return dto;
    }

    public static class Lobby {
        public String lobbyId;
        public Long partieId;
        public String etat = "ATTENTE";
        public boolean defenseurConnecte = false;
        public boolean attaquantConnecte = false;
        public boolean defenseurPret = false;
        public boolean attaquantPret = false;
        public List<VagueConfigDTO> vaguesAttaquant;
        public int budgetAttaquant = 500;
        public int budgetRestant = 500;
        public int nbVagues;

        public Lobby(String lobbyId, Long partieId, int nbVagues) {
            this.lobbyId = lobbyId;
            this.partieId = partieId;
            this.nbVagues = nbVagues;
        }
    }
}