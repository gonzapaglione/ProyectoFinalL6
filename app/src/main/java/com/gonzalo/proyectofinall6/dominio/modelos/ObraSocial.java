package com.gonzalo.proyectofinall6.dominio.modelos;

import com.google.gson.annotations.SerializedName;

public class ObraSocial {

    @SerializedName("idObraSocial")
    private int idObraSocial;

    @SerializedName("nombre")
    private String nombre;

    // Constructor, Getters y Setters

    public ObraSocial(int idObraSocial) {
        this.idObraSocial = idObraSocial;
    }

    public int getIdObraSocial() {
        return idObraSocial;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
