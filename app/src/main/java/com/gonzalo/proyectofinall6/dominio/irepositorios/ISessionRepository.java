package com.gonzalo.proyectofinall6.dominio.irepositorios;

public interface ISessionRepository {
    boolean isLoggedIn();

    int getUserId();

    void saveSession(int userId, boolean loggedIn);

    void clearSession();
}
