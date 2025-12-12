package com.gonzalo.proyectofinall6.data.repositorios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.remote.api.ApiService;
import com.gonzalo.proyectofinall6.data.remote.api.RetrofitClient;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroResponse;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IAuthRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository implements IAuthRepository {

    private final ApiService apiService;

    public AuthRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    @Override
    public LiveData<RepositoryResult<RegistroResponse>> registrarPaciente(RegistroRequest request) {
        MutableLiveData<RepositoryResult<RegistroResponse>> liveData = new MutableLiveData<>();

        apiService.registrarPaciente(request).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(RepositoryResult.success(response.body()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error en el registro"));
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                String message = (t != null && t.getMessage() != null) ? t.getMessage() : "Error de red";
                liveData.postValue(RepositoryResult.error("Error de red: " + message));
            }
        });

        return liveData;
    }
}
