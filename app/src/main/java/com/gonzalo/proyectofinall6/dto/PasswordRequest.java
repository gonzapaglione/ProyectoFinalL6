package com.gonzalo.proyectofinall6.dto;

public class PasswordRequest {
    private String passwordActual;
    private String passwordNueva;

    public PasswordRequest(String passwordActual, String passwordNueva) {
        this.passwordActual = passwordActual;
        this.passwordNueva = passwordNueva;
    }

    // Getters y setters
}
