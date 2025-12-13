package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class HistoriaClinicaResponse {

    @SerializedName("idHistoria")
    private Integer idHistoria;

    @SerializedName("idTurno")
    private Integer idTurno;

    // Datos del turno
    @SerializedName("fechaTurno")
    private String fechaTurno;

    @SerializedName("horaTurno")
    private String horaTurno;

    // Datos del paciente
    @SerializedName("idPaciente")
    private Integer idPaciente;

    @SerializedName("nombrePaciente")
    private String nombrePaciente;

    @SerializedName("apellidoPaciente")
    private String apellidoPaciente;

    @SerializedName("dniPaciente")
    private String dniPaciente;

    // Datos del odontólogo
    @SerializedName("idOdontologo")
    private Integer idOdontologo;

    @SerializedName("nombreOdontologo")
    private String nombreOdontologo;

    @SerializedName("apellidoOdontologo")
    private String apellidoOdontologo;

    // Información clínica
    @SerializedName("diagnostico")
    private String diagnostico;

    @SerializedName("tratamientoRealizado")
    private String tratamientoRealizado;

    @SerializedName("observaciones")
    private String observaciones;

    @SerializedName("motivoConsulta")
    private String motivoConsulta;

    public Integer getIdHistoria() {
        return idHistoria;
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public String getFechaTurno() {
        return fechaTurno;
    }

    public String getHoraTurno() {
        return horaTurno;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public String getApellidoPaciente() {
        return apellidoPaciente;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public Integer getIdOdontologo() {
        return idOdontologo;
    }

    public String getNombreOdontologo() {
        return nombreOdontologo;
    }

    public String getApellidoOdontologo() {
        return apellidoOdontologo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamientoRealizado() {
        return tratamientoRealizado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }
}
