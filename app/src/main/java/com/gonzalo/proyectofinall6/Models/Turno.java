
package com.gonzalo.proyectofinall6.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Turno implements Serializable {

    @SerializedName("idTurno")
    private Long idTurno;

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("hora")
    private String hora;

    @SerializedName("estadoTurno")
    private String estadoTurno;

    @SerializedName("motivoConsulta")
    private String motivoConsulta;

    @SerializedName("fechaSolicitud")
    private String fechaSolicitud;

    @SerializedName("idPaciente")
    private Long idPaciente;

    @SerializedName("nombrePaciente")
    private String nombrePaciente;

    @SerializedName("apellidoPaciente")
    private String apellidoPaciente;

    @SerializedName("dniPaciente")
    private String dniPaciente;

    @SerializedName("idOdontologo")
    private Long idOdontologo;

    @SerializedName("nombreOdontologo")
    private String nombreOdontologo;

    @SerializedName("apellidoOdontologo")
    private String apellidoOdontologo;

    @SerializedName("obraSocial")
    private String obraSocial;

    @SerializedName("notasCancelacion")
    private String notasCancelacion;

    // Getters and Setters

    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstadoTurno() {
        return estadoTurno;
    }

    public void setEstadoTurno(String estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidoPaciente() {
        return apellidoPaciente;
    }

    public void setApellidoPaciente(String apellidoPaciente) {
        this.apellidoPaciente = apellidoPaciente;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public Long getIdOdontologo() {
        return idOdontologo;
    }

    public void setIdOdontologo(Long idOdontologo) {
        this.idOdontologo = idOdontologo;
    }

    public String getNombreOdontologo() {
        return nombreOdontologo;
    }

    public void setNombreOdontologo(String nombreOdontologo) {
        this.nombreOdontologo = nombreOdontologo;
    }

    public String getApellidoOdontologo() {
        return apellidoOdontologo;
    }

    public void setApellidoOdontologo(String apellidoOdontologo) {
        this.apellidoOdontologo = apellidoOdontologo;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getNotasCancelacion() {
        return notasCancelacion;
    }

    public void setNotasCancelacion(String notasCancelacion) {
        this.notasCancelacion = notasCancelacion;
    }
}
