package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class PromedioValoracionResponse {

    @SerializedName("idOdontologo")
    private Integer idOdontologo;

    @SerializedName("promedio")
    private Double promedio;

    @SerializedName("cantidad")
    private Long cantidad;

    public Integer getIdOdontologo() {
        return idOdontologo;
    }

    public Double getPromedio() {
        return promedio;
    }

    public Long getCantidad() {
        return cantidad;
    }
}
