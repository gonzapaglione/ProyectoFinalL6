package com.gonzalo.proyectofinall6.data.repositorios;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.remote.api.ApiService;
import com.gonzalo.proyectofinall6.data.remote.api.RetrofitClient;
import com.gonzalo.proyectofinall6.data.remote.dto.EditarPacienteRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.PacienteResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PasswordRequest;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ISessionRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IPacienteRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacienteRepository implements IPacienteRepository {

    private final ApiService apiService;
    private final ISessionRepository sessionRepository;

    public PacienteRepository(Context context) {
        this.apiService = RetrofitClient.getApiService();
        this.sessionRepository = new SessionRepository(context);
    }

    private int getUserId() {
        return sessionRepository.getUserId();
    }

    @Override
    public LiveData<RepositoryResult<Paciente>> getPacienteActual() {
        MutableLiveData<RepositoryResult<Paciente>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        apiService.getPaciente(String.valueOf(userId)).enqueue(new Callback<PacienteResponse>() {
            @Override
            public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(RepositoryResult.success(response.body().getData()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al obtener los datos del paciente."));
                }
            }

            @Override
            public void onFailure(Call<PacienteResponse> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al obtener datos del paciente."));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<List<ObraSocial>>> getObrasSocialesDelPacienteActual() {
        MediatorLiveData<RepositoryResult<List<ObraSocial>>> liveData = new MediatorLiveData<>();

        LiveData<RepositoryResult<Paciente>> pacienteLiveData = getPacienteActual();
        liveData.addSource(pacienteLiveData, result -> {
            if (result == null) {
                liveData.removeSource(pacienteLiveData);
                return;
            }

            if (result.isSuccess() && result.getData() != null) {
                List<ObraSocial> obras = result.getData().getObrasSociales();
                liveData.setValue(RepositoryResult.success(obras == null ? Collections.emptyList() : obras));
            } else {
                liveData.setValue(RepositoryResult
                        .error(result.getError() == null ? "Error al obtener obras sociales" : result.getError()));
            }

            liveData.removeSource(pacienteLiveData);
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<Paciente>> editarPaciente(EditarPacienteRequest request) {
        MutableLiveData<RepositoryResult<Paciente>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        apiService.editarPaciente(String.valueOf(userId), request).enqueue(new Callback<PacienteResponse>() {
            @Override
            public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(RepositoryResult.success(response.body().getData()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al editar los datos del paciente."));
                }
            }

            @Override
            public void onFailure(Call<PacienteResponse> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al editar los datos del paciente."));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<Void>> cambiarPassword(String passwordActual, String passwordNueva) {
        MutableLiveData<RepositoryResult<Void>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        PasswordRequest request = new PasswordRequest(passwordActual, passwordNueva);
        apiService.cambiarPassword(String.valueOf(userId), request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(RepositoryResult.success(null));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al cambiar la contraseña."));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al cambiar la contraseña."));
            }
        });

        return liveData;
    }
}
