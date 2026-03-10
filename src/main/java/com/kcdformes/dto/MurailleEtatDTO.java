package com.kcdformes.dto;

public class MurailleEtatDTO {
    private int position;
    private int pvActuels;
    private int pvMax;
    private boolean detruite;

    public MurailleEtatDTO(int position, int pvActuels, int pvMax, boolean detruite) {
        this.position = position;
        this.pvActuels = pvActuels;
        this.pvMax = pvMax;
        this.detruite = detruite;
    }

    public int getPosition() { return position; }
    public int getPvActuels() { return pvActuels; }
    public int getPvMax() { return pvMax; }
    public boolean isDetruite() { return detruite; }
}