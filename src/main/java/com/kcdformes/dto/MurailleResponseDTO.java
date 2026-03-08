package com.kcdformes.dto;

public class MurailleResponseDTO {
    private Long id;
    private int position;
    private int pvMax;
    private int pvActuels;
    private int cout;
    private Long partieId;

    public MurailleResponseDTO(Long id, int position, int pvMax, int pvActuels, int cout, Long partieId) {
        this.id = id;
        this.position = position;
        this.pvMax = pvMax;
        this.pvActuels = pvActuels;
        this.cout = cout;
        this.partieId = partieId;
    }

    public Long getId() { return id; }
    public int getPosition() { return position; }
    public int getPvMax() { return pvMax; }
    public int getPvActuels() { return pvActuels; }
    public int getCout() { return cout; }
    public Long getPartieId() { return partieId; }
}