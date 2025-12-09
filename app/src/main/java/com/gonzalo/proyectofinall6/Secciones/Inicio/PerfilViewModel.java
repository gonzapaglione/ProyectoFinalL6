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
import com.gonzalo.proyectofinall6.dto.PacienteResponse;
import com.gonzalo.proyectofinall6.modelos.Paciente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final MutableLiveData<Paciente> paciente = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    public LiveData<Paciente> getPaciente() {
        return paciente;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadPacienteData() {
        new Thread(() -> {
            int patientIdInt = sharedPreferences.getInt("user_id", -1);
            if (patientIdInt != -1) {
                String patientId = String.valueOf(patientIdInt);
                ApiService apiService = RetrofitClient.getApiService();
                apiService.getPaciente(patientId).enqueue(new Callback<PacienteResponse>() {
                    @Override
                    public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            paciente.postValue(response.body().getData());
                        } else {
                            error.postValue("Error al cargar datos del paciente");
                        }
                    }

                    @Override
                    public void onFailure(Call<PacienteResponse> call, Throwable t) {
                        error.postValue("Error de red: " + t.getMessage());
                    }
                });
            }
        }).start();
    }
}
