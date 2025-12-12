package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class CrearValoracionRequest {

    @SerializedName("idTurno")
    private final Integer idTurno;

    @SerializedName("idPaciente")
    private final Integer idPaciente;

    @SerializedName("estrellas")
    private final Integer estrellas;

    @SerializedName("comentario")
    private final String comentario;

    public CrearValoracionRequest(Integer idTurno, Integer idPaciente, Integer estrellas, String comentario) {
        this.idTurno = idTurno;
        this.idPaciente = idPaciente;
        this.estrellas = estrellas;
        this.comentario = comentario;
    }
}
