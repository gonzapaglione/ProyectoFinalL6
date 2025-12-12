package com.gonzalo.proyectofinall6.data.remote.dto;

public class TurnoResponse {
    private Integer idTurno;
    private String fecha;
    private String hora;
    private String nombreOdontologo;
    private String apellidoOdontologo;
    private String especialidad;
    private String motivoConsulta;
    private String estadoTurno;
    private String obraSocial;

    public TurnoResponse(Integer idTurno, String fecha, String hora, String nombreOdontologo, String apellidoOdontologo, String especialidad, String motivoConsulta, String estadoTurno, String obraSocial) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.nombreOdontologo = nombreOdontologo;
        this.apellidoOdontologo = apellidoOdontologo;
        this.especialidad = especialidad;
        this.motivoConsulta = motivoConsulta;
        this.estadoTurno = estadoTurno;
        this.obraSocial = obraSocial;
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Integer idTurno) {
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getEstadoTurno() {
        return estadoTurno;
    }

    public void setEstadoTurno(String estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }
}
