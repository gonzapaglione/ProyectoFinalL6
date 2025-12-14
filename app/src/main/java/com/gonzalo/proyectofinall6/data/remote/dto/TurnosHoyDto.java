package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TurnosHoyDto {

    @SerializedName("cantidad")
    private long cantidad;

    @SerializedName("turnos")
    private List<TurnoHoyItemDto> turnos;

    public long getCantidad() {
        return cantidad;
    }

    public List<TurnoHoyItemDto> getTurnos() {
        return turnos;
    }
}
