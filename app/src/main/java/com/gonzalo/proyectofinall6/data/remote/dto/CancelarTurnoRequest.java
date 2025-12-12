package com.gonzalo.proyectofinall6.data.remote.dto;

public class CancelarTurnoRequest {
    private int idTurno;
    private String motivo;

    public CancelarTurnoRequest(int idTurno, String motivo) {
        this.idTurno = idTurno;
        this.motivo = motivo;
    }

    // Getters y Setters si son necesarios para la serialización de Gson
    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
