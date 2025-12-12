package com.gonzalo.proyectofinall6.data.repositorios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.remote.api.ApiService;
import com.gonzalo.proyectofinall6.data.remote.api.RetrofitClient;
import com.gonzalo.proyectofinall6.data.remote.dto.ObrasSocialesResponse;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IObrasSocialesRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObrasSocialesRepository implements IObrasSocialesRepository {

    private final ApiService apiService;

    public ObrasSocialesRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    @Override
    public LiveData<RepositoryResult<List<ObraSocial>>> getObrasSociales() {
        MutableLiveData<RepositoryResult<List<ObraSocial>>> liveData = new MutableLiveData<>();

        apiService.getObrasSociales().enqueue(new Callback<ObrasSocialesResponse>() {
            @Override
            public void onResponse(Call<ObrasSocialesResponse> call, Response<ObrasSocialesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<ObraSocial> data = response.body().getData();
                    liveData.postValue(RepositoryResult.success(data == null ? Collections.emptyList() : data));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al cargar las obras sociales"));
                }
            }

            @Override
            public void onFailure(Call<ObrasSocialesResponse> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al cargar las obras sociales"));
            }
        });

        return liveData;
    }
}
