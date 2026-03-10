package com.kcdformes.dto;

public class MurailleEtatDTO {
    public int position;
    public int pvActuels;
    public int pvMax;
    public boolean detruite;

    public MurailleEtatDTO(int position, int pvActuels, int pvMax, boolean detruite) {
        this.position = position;
        this.pvActuels = pvActuels;
        this.pvMax = pvMax;
        this.detruite = detruite;
    }
}