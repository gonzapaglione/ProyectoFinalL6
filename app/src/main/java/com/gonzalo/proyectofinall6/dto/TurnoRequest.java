package com.gonzalo.proyectofinall6.dto;

import com.google.gson.annotations.SerializedName;

public class TurnoRequest {
    private int idPaciente;
    private int idOdontologo;
    private String fecha;

    @SerializedName("hora")
    private String hora;

    @SerializedName("idMotivo")
    private int idMotivo;

    @SerializedName("idObraSocial")
    private int idObraSocial;

    public TurnoRequest(int idPaciente, int idOdontologo, String fecha, String hora, int idMotivo, int idObraSocial) {
        this.idPaciente = idPaciente;
        this.idOdontologo = idOdontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.idMotivo = idMotivo;
        this.idObraSocial = idObraSocial;
    }
}
