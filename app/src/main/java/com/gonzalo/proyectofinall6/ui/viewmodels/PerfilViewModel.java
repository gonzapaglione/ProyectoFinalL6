package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.gonzalo.proyectofinall6.data.remote.dto.EditarPacienteRequest;
import com.gonzalo.proyectofinall6.data.repositorios.PacienteRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IPacienteRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

public class PerfilViewModel extends AndroidViewModel {

    private final IPacienteRepository repository;
    private final MutableLiveData<Paciente> paciente = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> success = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PacienteRepository(application.getApplicationContext());
    }

    public LiveData<Paciente> getPaciente() {
        return paciente;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<String> getSuccess() {
        return success;
    }

    public void loadPacienteData() {
        LiveData<RepositoryResult<Paciente>> source = repository.getPacienteActual();
        Observer<RepositoryResult<Paciente>> observer = new Observer<RepositoryResult<Paciente>>() {
            @Override
            public void onChanged(RepositoryResult<Paciente> result) {
                if (result != null && result.isSuccess()) {
                    paciente.setValue(result.getData());
                } else {
                    error.setValue(result == null ? "Error desconocido" : result.getError());
                }
                source.removeObserver(this);
            }
        };
        source.observeForever(observer);
    }

    public void editarPaciente(EditarPacienteRequest request) {
        LiveData<RepositoryResult<Paciente>> source = repository.editarPaciente(request);
        Observer<RepositoryResult<Paciente>> observer = new Observer<RepositoryResult<Paciente>>() {
            @Override
            public void onChanged(RepositoryResult<Paciente> result) {
                if (result != null && result.isSuccess()) {
                    paciente.setValue(result.getData());
                    success.setValue("Datos actualizados correctamente");
                } else {
                    error.setValue(result == null ? "Error desconocido" : result.getError());
                }
                source.removeObserver(this);
            }
        };
        source.observeForever(observer);
    }

    public void cambiarPassword(String passwordActual, String passwordNueva) {
        LiveData<RepositoryResult<Void>> source = repository.cambiarPassword(passwordActual, passwordNueva);
        Observer<RepositoryResult<Void>> observer = new Observer<RepositoryResult<Void>>() {
            @Override
            public void onChanged(RepositoryResult<Void> result) {
                if (result != null && result.isSuccess()) {
                    success.setValue("Contraseña actualizada correctamente");
                } else {
                    error.setValue(result == null ? "Error desconocido" : result.getError());
                }
                source.removeObserver(this);
            }
        };
        source.observeForever(observer);
    }
}
