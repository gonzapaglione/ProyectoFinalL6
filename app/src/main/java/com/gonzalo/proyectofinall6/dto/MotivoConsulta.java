package com.gonzalo.proyectofinall6.dto;

import com.google.gson.annotations.SerializedName;

public class MotivoConsulta {
    @SerializedName("idMotivo")
    private int id;

    @SerializedName("descripcion")
    private String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
