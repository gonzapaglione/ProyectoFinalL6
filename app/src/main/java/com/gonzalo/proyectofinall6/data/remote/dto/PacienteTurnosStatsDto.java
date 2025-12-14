package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class PacienteTurnosStatsDto {

    @SerializedName("total")
    private long total;

    @SerializedName("programados")
    private long programados;

    @SerializedName("realizados")
    private long realizados;

    @SerializedName("ausentes")
    private long ausentes;

    @SerializedName("cancelados")
    private long cancelados;

    public long getTotal() {
        return total;
    }

    public long getProgramados() {
        return programados;
    }

    public long getRealizados() {
        return realizados;
    }

    public long getAusentes() {
        return ausentes;
    }

    public long getCancelados() {
        return cancelados;
    }
}
