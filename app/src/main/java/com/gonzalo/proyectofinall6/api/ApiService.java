package com.gonzalo.proyectofinall6.api;

import com.gonzalo.proyectofinall6.modelos.LoginRequest;
import com.gonzalo.proyectofinall6.modelos.LoginResponse;
import com.gonzalo.proyectofinall6.modelos.ObrasSocialesResponse;
import com.gonzalo.proyectofinall6.modelos.PacienteResponse;
import com.gonzalo.proyectofinall6.modelos.RegistroRequest;
import com.gonzalo.proyectofinall6.modelos.RegistroResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/paciente/{id}")
    Call<PacienteResponse> getPaciente(@Path("id") String id);

    @GET("api/catalogos/obras-sociales")
    Call<ObrasSocialesResponse> getObrasSociales();

    @POST("api/auth/registro/paciente")
    Call<RegistroResponse> registrarPaciente(@Body RegistroRequest registroRequest);
}
