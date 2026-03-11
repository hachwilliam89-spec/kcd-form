package com.kcdformes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {
    private int statut;
    private String message;
    private T donnees;
    private String erreur;
    private LocalDateTime horodatage;

    private ApiResponseDTO(int statut, String message, T donnees, String erreur) {
        this.statut = statut;
        this.message = message;
        this.donnees = donnees;
        this.erreur = erreur;
        this.horodatage = LocalDateTime.now();
    }

    // Méthodes de fabrique pour succès
    public static <T> ApiResponseDTO<T> cree(String message, T donnees) {
        return new ApiResponseDTO<>(201, message, donnees, null);
    }

    public static <T> ApiResponseDTO<T> ok(String message, T donnees) {
        return new ApiResponseDTO<>(200, message, donnees, null);
    }

    public static ApiResponseDTO<Void> sansContenu(String message) {
        return new ApiResponseDTO<>(204, message, null, null);
    }

    // Méthodes de fabrique pour erreurs
    public static ApiResponseDTO<Void> mauvaiseRequete(String erreur) {
        return new ApiResponseDTO<>(400, null, null, erreur);
    }

    public static ApiResponseDTO<Void> nonTrouve(String erreur) {
        return new ApiResponseDTO<>(404, null, null, erreur);
    }

    public static ApiResponseDTO<Void> conflit(String erreur) {
        return new ApiResponseDTO<>(409, null, null, erreur);
    }

    public static ApiResponseDTO<Void> nonTraitable(String erreur) {
        return new ApiResponseDTO<>(422, null, null, erreur);
    }

    // Getters
    public int getStatut() { return statut; }
    public String getMessage() { return message; }
    public T getDonnees() { return donnees; }
    public String getErreur() { return erreur; }
    public LocalDateTime getHorodatage() { return horodatage; }
}