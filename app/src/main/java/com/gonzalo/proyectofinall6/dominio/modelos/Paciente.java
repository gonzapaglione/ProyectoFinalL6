package com.gonzalo.proyectofinall6.dominio.modelos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Paciente {

    @SerializedName("idPaciente")
    private int idPaciente;

    @SerializedName("dni")
    private String dni;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("email")
    private String email;

    @SerializedName("obrasSociales")
    private List<ObraSocial> obrasSociales;

    // Getters
    public int getIdPaciente() {
        return idPaciente;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    public List<ObraSocial> getObrasSociales() {
        return obrasSociales;
    }
}
