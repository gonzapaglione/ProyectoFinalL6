package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class ValoracionResponse {

    @SerializedName("idValoracion")
    private Integer idValoracion;

    @SerializedName("idTurno")
    private Integer idTurno;

    @SerializedName("idPaciente")
    private Integer idPaciente;

    @SerializedName("idOdontologo")
    private Integer idOdontologo;

    @SerializedName("estrellas")
    private Integer estrellas;

    @SerializedName("comentario")
    private String comentario;

    public Integer getIdValoracion() {
        return idValoracion;
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public Integer getIdOdontologo() {
        return idOdontologo;
    }

    public Integer getEstrellas() {
        return estrellas;
    }

    public String getComentario() {
        return comentario;
    }
}
