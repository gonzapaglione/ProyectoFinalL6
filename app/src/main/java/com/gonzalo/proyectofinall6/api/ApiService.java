package com.gonzalo.proyectofinall6.api;

import com.gonzalo.proyectofinall6.models.LoginRequest;
import com.gonzalo.proyectofinall6.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
