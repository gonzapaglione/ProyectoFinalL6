package com.gonzalo.proyectofinall6.dto;

import com.google.gson.annotations.SerializedName;

public class EspecialidadResponse {
    @SerializedName("idEspecialidad")
    private int id;
    private String nombre;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
