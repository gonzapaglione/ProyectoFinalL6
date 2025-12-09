package com.gonzalo.proyectofinall6.dto;

import com.gonzalo.proyectofinall6.modelos.ObraSocial;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ObrasSocialesResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ObraSocial> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<ObraSocial> getData() {
        return data;
    }
}
