package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.remote.dto.LoginRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.LoginResponse;
import com.gonzalo.proyectofinall6.data.repositorios.AuthRepository;
import com.gonzalo.proyectofinall6.data.repositorios.SessionRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IAuthRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ISessionRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

public class LoginViewModel extends AndroidViewModel {

    private final IAuthRepository authRepository;
    private final ISessionRepository sessionRepository;

    private final MediatorLiveData<RepositoryResult<LoginResponse>> loginResult = new MediatorLiveData<>();
    private LiveData<RepositoryResult<LoginResponse>> loginSource;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = new AuthRepository();
        this.sessionRepository = new SessionRepository(application.getApplicationContext());
    }

    public LiveData<RepositoryResult<LoginResponse>> getLoginResult() {
        return loginResult;
    }

    public void resetLoginResult() {
        loginResult.setValue(null);
    }

    public void login(String email, String password, boolean rememberMe) {
        LoginRequest request = new LoginRequest(email, password);

        if (loginSource != null) {
            loginResult.removeSource(loginSource);
        }

        loginSource = authRepository.login(request);
        loginResult.addSource(loginSource, result -> {
            if (result != null && result.isSuccess() && result.getData() != null
                    && result.getData().getData() != null) {
                sessionRepository.saveSession(result.getData().getData().getUserId(), rememberMe);
            }
            loginResult.setValue(result);

            if (loginSource != null) {
                loginResult.removeSource(loginSource);
            }
        });
    }
}
