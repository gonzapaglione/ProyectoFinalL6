package com.gonzalo.proyectofinall6.data.remote.dto;

public class HorarioDisponible {
    private String fecha;
    private String hora;
    private boolean disponible;

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public boolean isDisponible() {
        return disponible;
    }
}
