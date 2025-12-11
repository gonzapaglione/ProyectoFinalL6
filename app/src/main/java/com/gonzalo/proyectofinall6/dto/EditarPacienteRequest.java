package com.gonzalo.proyectofinall6.dto;

import com.gonzalo.proyectofinall6.modelos.ObraSocial;

import java.util.List;

public class EditarPacienteRequest {
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String email;
    private String password;
    private List<ObraSocial> obrasSociales;

    public EditarPacienteRequest(String dni, String nombre, String apellido, String telefono, String direccion, String email, String password, List<ObraSocial> obrasSociales) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.password = password;
        this.obrasSociales = obrasSociales;
    }

    // Getters y setters
}