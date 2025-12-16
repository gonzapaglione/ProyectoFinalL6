package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class FcmTokenRequest {

    @SerializedName("idPaciente")
    private final int idPaciente;

    @SerializedName("token")
    private final String token;

    public FcmTokenRequest(int idPaciente, String token) {
        this.idPaciente = idPaciente;
        this.token = token;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public String getToken() {
        return token;
    }
}
