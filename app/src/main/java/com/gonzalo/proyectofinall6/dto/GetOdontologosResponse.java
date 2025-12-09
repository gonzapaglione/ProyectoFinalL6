package com.gonzalo.proyectofinall6.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOdontologosResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<OdontologoResponse> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<OdontologoResponse> getData() {
        return data;
    }
}
