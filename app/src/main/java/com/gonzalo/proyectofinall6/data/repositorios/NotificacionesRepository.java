package com.gonzalo.proyectofinall6.data.repositorios;

import android.content.Context;
import android.util.Log;

import com.gonzalo.proyectofinall6.data.remote.api.ApiService;
import com.gonzalo.proyectofinall6.data.remote.api.RetrofitClient;
import com.gonzalo.proyectofinall6.data.remote.dto.FcmTokenRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionesRepository {

    private static final String TAG = "NotificacionesRepo";

    private final ApiService apiService;
    private final SessionRepository sessionRepository;

    public NotificacionesRepository(Context context) {
        this.apiService = RetrofitClient.getApiService();
        this.sessionRepository = new SessionRepository(context);
    }

    public void registrarTokenFcm(String token) {
        if (token == null || token.trim().isEmpty()) {
            return;
        }

        int idPaciente = sessionRepository.getUserId();
        if (idPaciente <= 0) {
            // No hay sesión o no es paciente
            return;
        }

        apiService.registrarFcmToken(new FcmTokenRequest(idPaciente, token))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.w(TAG, "No se pudo registrar token FCM. HTTP=" + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.w(TAG, "Error registrando token FCM: " + t.getMessage());
                    }
                });
    }
}
