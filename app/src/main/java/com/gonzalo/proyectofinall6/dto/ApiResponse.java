package com.gonzalo.proyectofinall6.dto;

public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
