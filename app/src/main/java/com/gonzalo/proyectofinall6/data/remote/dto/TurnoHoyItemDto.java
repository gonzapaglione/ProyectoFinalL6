package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class TurnoHoyItemDto {

    @SerializedName("idTurno")
    private int idTurno;

    @SerializedName("hora")
    private String hora;

    @SerializedName("estado")
    private String estado;

    @SerializedName("odontologo")
    private String odontologo;

    @SerializedName("motivo")
    private String motivo;

    public int getIdTurno() {
        return idTurno;
    }

    public String getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }

    public String getOdontologo() {
        return odontologo;
    }

    public String getMotivo() {
        return motivo;
    }
}
