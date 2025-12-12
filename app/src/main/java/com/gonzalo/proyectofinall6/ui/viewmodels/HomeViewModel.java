package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.gonzalo.proyectofinall6.data.repositorios.PacienteRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IPacienteRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

public class HomeViewModel extends AndroidViewModel {

    private final IPacienteRepository pacienteRepository;

    private final MediatorLiveData<Paciente> paciente = new MediatorLiveData<>();
    private final MediatorLiveData<String> error = new MediatorLiveData<>();

    private LiveData<RepositoryResult<Paciente>> pacienteSource;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.pacienteRepository = new PacienteRepository(application.getApplicationContext());
    }

    public LiveData<Paciente> getPaciente() {
        return paciente;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadPacienteActual() {
        if (pacienteSource != null) {
            paciente.removeSource(pacienteSource);
        }

        pacienteSource = pacienteRepository.getPacienteActual();
        paciente.addSource(pacienteSource, result -> {
            if (result != null && result.isSuccess()) {
                paciente.setValue(result.getData());
            } else {
                error.setValue(result == null ? "Error desconocido" : result.getError());
            }

            if (pacienteSource != null) {
                paciente.removeSource(pacienteSource);
            }
        });
    }
}
