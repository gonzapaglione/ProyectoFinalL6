package com.gonzalo.proyectofinall6.data.remote.dto;

import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import com.google.gson.annotations.SerializedName;

public class RegistroResponse {

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
