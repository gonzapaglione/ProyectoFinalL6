package com.gonzalo.proyectofinall6.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HorariosDisponiblesResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<HorarioDisponible> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<HorarioDisponible> getData() {
        return data;
    }
}
