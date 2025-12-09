
package com.gonzalo.proyectofinall6.dto;

import java.util.List;

public class MotivosConsultaResponse {
    private boolean success;
    private List<MotivoConsulta> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<MotivoConsulta> getData() {
        return data;
    }

    public void setData(List<MotivoConsulta> data) {
        this.data = data;
    }
}
