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

    private static final String PREFS_NAME = "fcm_prefs";
    private static final String KEY_LAST_TOKEN_PREFIX = "last_registered_token_";

    private final ApiService apiService;
    private final SessionRepository sessionRepository;
    private final android.content.SharedPreferences fcmPrefs;

    public NotificacionesRepository(Context context) {
        this.apiService = RetrofitClient.getApiService();
        this.sessionRepository = new SessionRepository(context);
        this.fcmPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void registrarTokenFcm(String token) {
        if (token == null || token.trim().isEmpty()) {
            return;
        }

        int idPaciente = sessionRepository.getPacienteId();
        if (idPaciente <= 0) {
            // No hay sesión o no es paciente
            return;
        }

        String normalized = token.trim();
        String lastKey = KEY_LAST_TOKEN_PREFIX + idPaciente;
        String lastRegistered = fcmPrefs.getString(lastKey, null);
        if (normalized.equals(lastRegistered)) {
            return;
        }

        apiService.registrarFcmToken(new FcmTokenRequest(idPaciente, normalized))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.w(TAG, "No se pudo registrar token FCM. HTTP=" + response.code());
                            return;
                        }

                        fcmPrefs.edit().putString(lastKey, normalized).apply();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.w(TAG, "Error registrando token FCM: " + t.getMessage());
                    }
                });
    }

    public void desregistrarTokenFcm() {
        int idPaciente = sessionRepository.getPacienteId();
        if (idPaciente <= 0) {
            return;
        }

        apiService.desregistrarFcmToken(idPaciente)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.w(TAG, "No se pudo desregistrar token FCM. HTTP=" + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.w(TAG, "Error desregistrando token FCM: " + t.getMessage());
                    }
                });
    }

    public void clearLocalTokenCache() {
        int idPaciente = sessionRepository.getPacienteId();
        if (idPaciente > 0) {
            fcmPrefs.edit().remove(KEY_LAST_TOKEN_PREFIX + idPaciente).apply();
        }
    }
}
