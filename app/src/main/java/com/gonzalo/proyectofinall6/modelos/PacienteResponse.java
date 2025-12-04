package com.gonzalo.proyectofinall6.modelos;

import com.google.gson.annotations.SerializedName;

public class PacienteResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Paciente data;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Paciente getData() {
        return data;
    }
}
