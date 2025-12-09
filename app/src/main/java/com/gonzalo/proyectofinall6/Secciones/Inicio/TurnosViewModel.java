package com.gonzalo.proyectofinall6.Secciones.Inicio;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.api.ApiService;
import com.gonzalo.proyectofinall6.api.RetrofitClient;
import com.gonzalo.proyectofinall6.dto.CancelarTurnoRequest;
import com.gonzalo.proyectofinall6.dto.HistorialResponse;
import com.gonzalo.proyectofinall6.dto.PacienteResponse;
import com.gonzalo.proyectofinall6.dto.ProximosTurnosResponse;
import com.gonzalo.proyectofinall6.modelos.Turno;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TurnosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Turno>> proximosTurnos = new MutableLiveData<>();
    private final MutableLiveData<List<Turno>> historialTurnos = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> turnoCancelado = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;

    public TurnosViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    public LiveData<List<Turno>> getProximosTurnos() {
        return proximosTurnos;
    }

    public LiveData<List<Turno>> getHistorialTurnos() {
        return historialTurnos;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getTurnoCancelado() {
        return turnoCancelado;
    }

    public void loadTurnos() {
        new Thread(() -> {
            int userId = sharedPreferences.getInt("user_id", -1);
            if (userId != -1) {
                ApiService apiService = RetrofitClient.getApiService();
                apiService.getPaciente(String.valueOf(userId)).enqueue(new Callback<PacienteResponse>() {
                    @Override
                    public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            long pacienteId = response.body().getData().getIdPaciente();
                            fetchProximosTurnos(pacienteId);
                            fetchHistorialTurnos(pacienteId);
                        } else {
                            error.postValue("Error al obtener los datos del paciente.");
                        }
                    }

                    @Override
                    public void onFailure(Call<PacienteResponse> call, Throwable t) {
                        error.postValue("Error de red al obtener datos del paciente.");
                    }
                });
            } else {
                error.postValue("Error: No se pudo obtener el ID de usuario.");
            }
        }).start();
    }

    private void fetchProximosTurnos(long pacienteId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getProximos(pacienteId).enqueue(new Callback<ProximosTurnosResponse>() {
            @Override
            public void onResponse(Call<ProximosTurnosResponse> call, Response<ProximosTurnosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    proximosTurnos.postValue(response.body().getData());
                } else {
                    proximosTurnos.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ProximosTurnosResponse> call, Throwable t) {
                error.postValue("Error de red al cargar próximos turnos");
            }
        });
    }

    private void fetchHistorialTurnos(long pacienteId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getHistorial(pacienteId).enqueue(new Callback<HistorialResponse>() {
            @Override
            public void onResponse(Call<HistorialResponse> call, Response<HistorialResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Turno> allTurnos = response.body().getData();
                    List<Turno> filteredHistorial = new ArrayList<>();
                    for (Turno turno : allTurnos) {
                        if (!"Programado".equalsIgnoreCase(turno.getEstadoTurno())) {
                            filteredHistorial.add(turno);
                        }
                    }
                    historialTurnos.postValue(filteredHistorial);
                } else {
                    historialTurnos.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<HistorialResponse> call, Throwable t) {
                error.postValue("Error de red al cargar historial");
            }
        });
    }

    public void cancelarTurno(int turnoId, String motivo) {
        CancelarTurnoRequest request = new CancelarTurnoRequest(turnoId, motivo);
        RetrofitClient.getApiService().cancelarTurno(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    turnoCancelado.postValue(true);
                    loadTurnos(); // Recargar los turnos
                } else {
                    turnoCancelado.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                turnoCancelado.postValue(false);
            }
        });
    }
}
