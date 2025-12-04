package com.gonzalo.proyectofinall6.modelos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegistroRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

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

    @SerializedName("obrasSociales")
    private List<ObraSocial> obrasSociales;

    // Constructor, Getters y Setters

    public RegistroRequest(String email, String password, String dni, String nombre, String apellido, String telefono, String direccion, List<ObraSocial> obrasSociales) {
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.obrasSociales = obrasSociales;
    }

}
