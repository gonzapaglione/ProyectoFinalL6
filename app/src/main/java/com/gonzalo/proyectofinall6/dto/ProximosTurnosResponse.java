package com.gonzalo.proyectofinall6.dto;

import com.gonzalo.proyectofinall6.modelos.Turno;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProximosTurnosResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Turno> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Turno> getData() {
        return data;
    }

    public void setData(List<Turno> data) {
        this.data = data;
    }
}
