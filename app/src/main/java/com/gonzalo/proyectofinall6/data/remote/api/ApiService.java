package com.gonzalo.proyectofinall6.data.remote.api;

import com.gonzalo.proyectofinall6.data.remote.dto.ApiResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.CancelarTurnoRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.EditarPacienteRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.GetOdontologosResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HistorialResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HorariosDisponiblesResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.LoginRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.LoginResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.MotivosConsultaResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.ObrasSocialesResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PacienteResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PasswordRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.ProximosTurnosResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/paciente/{id}")
    Call<PacienteResponse> getPaciente(@Path("id") String id);

    @PUT("api/paciente/{id}")
    Call<PacienteResponse> editarPaciente(@Path("id") String id, @Body EditarPacienteRequest request);

    @PUT("api/paciente/{id}/password")
    Call<Void> cambiarPassword(@Path("id") String id, @Body PasswordRequest request);

    @GET("api/catalogos/obras-sociales")
    Call<ObrasSocialesResponse> getObrasSociales();

    @POST("api/auth/registro/paciente")
    Call<RegistroResponse> registrarPaciente(@Body RegistroRequest registroRequest);


    @GET("api/turnos/paciente/{id}/historial")
    Call<HistorialResponse> getHistorial(@Path("id") long pacienteId);

    @GET("api/turnos/paciente/{id}/proximos")
    Call<ProximosTurnosResponse> getProximos(@Path("id") long pacienteId);

    @POST("api/turnos/cancelar")
    Call<Void> cancelarTurno(@Body CancelarTurnoRequest request);

    // Reserva de turnos

    @GET("api/odontologos")
    Call<GetOdontologosResponse> getOdontologos();

    @GET("api/turnos/horarios-disponibles")
    Call<HorariosDisponiblesResponse> getHorariosDisponibles(
            @Query("idOdontologo") Integer idOdontologo,
            @Query("fecha") String fecha
    );

    @GET("api/catalogos/motivos-consulta")
    Call<MotivosConsultaResponse> getMotivosConsulta();

    @POST("api/turnos")
    Call<ApiResponse<TurnoResponse>> crearTurno(
            @Body TurnoRequest request
    );

}
