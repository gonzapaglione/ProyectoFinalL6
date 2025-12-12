package com.gonzalo.proyectofinall6.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private UserData data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserData getData() {
        return data;
    }

    public static class UserData {
        @SerializedName("userId")
        private int userId;

        @SerializedName("email")
        private String email;

        @SerializedName("rol")
        private String rol;

        @SerializedName("nombre")
        private String nombre;

        @SerializedName("apellido")
        private String apellido;


        public int getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public String getRol() {
            return rol;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }
    }
}
