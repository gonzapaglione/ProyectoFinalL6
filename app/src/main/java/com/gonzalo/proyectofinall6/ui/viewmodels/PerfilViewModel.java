package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.remote.dto.EditarPacienteRequest;
import com.gonzalo.proyectofinall6.data.repositorios.PacienteRepository;
import com.gonzalo.proyectofinall6.data.repositorios.ObrasSocialesRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IPacienteRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IObrasSocialesRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.ArrayList;
import java.util.List;

public class PerfilViewModel extends AndroidViewModel {

    private final IPacienteRepository repository;
    private final IObrasSocialesRepository obrasSocialesRepository;
    private final MediatorLiveData<Paciente> paciente = new MediatorLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> success = new MutableLiveData<>();
    private final MediatorLiveData<List<ObraSocial>> obrasSocialesCatalogo = new MediatorLiveData<>();

    private LiveData<RepositoryResult<Paciente>> pacienteSource;
    private LiveData<RepositoryResult<List<ObraSocial>>> obrasCatalogoSource;
    private LiveData<RepositoryResult<Paciente>> editarPacienteSource;
    private LiveData<RepositoryResult<Void>> cambiarPasswordSource;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PacienteRepository(application.getApplicationContext());
        this.obrasSocialesRepository = new ObrasSocialesRepository();
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

    public LiveData<List<ObraSocial>> getObrasSocialesCatalogo() {
        return obrasSocialesCatalogo;
    }

    public void cargarCatalogoObrasSociales() {
        if (obrasCatalogoSource != null) {
            obrasSocialesCatalogo.removeSource(obrasCatalogoSource);
        }

        obrasCatalogoSource = obrasSocialesRepository.getObrasSociales();
        obrasSocialesCatalogo.addSource(obrasCatalogoSource, result -> {
            if (result != null && result.isSuccess()) {
                obrasSocialesCatalogo.setValue(result.getData());
            } else {
                obrasSocialesCatalogo.setValue(new ArrayList<>());
                error.setValue(result == null ? "Error al cargar obras sociales" : result.getError());
            }
            if (obrasCatalogoSource != null) {
                obrasSocialesCatalogo.removeSource(obrasCatalogoSource);
            }
        });
    }

    public void loadPacienteData() {
        if (pacienteSource != null) {
            paciente.removeSource(pacienteSource);
        }

        pacienteSource = repository.getPacienteActual();
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

    public void editarPaciente(EditarPacienteRequest request) {
        if (editarPacienteSource != null) {
            paciente.removeSource(editarPacienteSource);
        }

        editarPacienteSource = repository.editarPaciente(request);
        paciente.addSource(editarPacienteSource, result -> {
            if (result != null && result.isSuccess()) {
                paciente.setValue(result.getData());
                success.setValue("Datos actualizados correctamente");
            } else {
                error.setValue(result == null ? "Error desconocido" : result.getError());
            }
            if (editarPacienteSource != null) {
                paciente.removeSource(editarPacienteSource);
            }
        });
    }

    public void cambiarPassword(String passwordActual, String passwordNueva) {
        if (cambiarPasswordSource != null) {
            paciente.removeSource(cambiarPasswordSource);
        }

        cambiarPasswordSource = repository.cambiarPassword(passwordActual, passwordNueva);
        paciente.addSource(cambiarPasswordSource, result -> {
            if (result != null && result.isSuccess()) {
                success.setValue("Contraseña actualizada correctamente");
            } else {
                error.setValue(result == null ? "Error desconocido" : result.getError());
            }

            if (cambiarPasswordSource != null) {
                paciente.removeSource(cambiarPasswordSource);
            }
        });
    }
}
