package com.gonzalo.proyectofinall6.data.repositorios;

import android.content.Context;
import android.content.SharedPreferences;

import com.gonzalo.proyectofinall6.dominio.irepositorios.ISessionRepository;

public class SessionRepository implements ISessionRepository {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PACIENTE_ID = "paciente_id";
    private static final String KEY_ROL = "rol";

    private final SharedPreferences sharedPreferences;

    public SessionRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    @Override
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public int getPacienteId() {
        return sharedPreferences.getInt(KEY_PACIENTE_ID, -1);
    }

    public String getRol() {
        return sharedPreferences.getString(KEY_ROL, null);
    }

    @Override
    public void saveSession(int userId, boolean loggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putBoolean(KEY_IS_LOGGED_IN, loggedIn);
        editor.apply();
    }

    public void saveSession(int sessionId, Integer pacienteId, String rol, boolean loggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, sessionId);
        if (pacienteId != null) {
            editor.putInt(KEY_PACIENTE_ID, pacienteId);
        } else {
            editor.remove(KEY_PACIENTE_ID);
        }
        if (rol != null) {
            editor.putString(KEY_ROL, rol);
        } else {
            editor.remove(KEY_ROL);
        }
        editor.putBoolean(KEY_IS_LOGGED_IN, loggedIn);
        editor.apply();
    }

    @Override
    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }
}
