package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OdontologoResponse {
    @SerializedName("idOdontologo")
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private List<EspecialidadResponse> especialidades;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public List<EspecialidadResponse> getEspecialidades() {
        return especialidades;
    }
}
