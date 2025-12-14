package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.gonzalo.proyectofinall6.data.repositorios.PacienteRepository;
import com.gonzalo.proyectofinall6.data.repositorios.TurnosRepository;
import com.gonzalo.proyectofinall6.data.remote.dto.PacienteTurnosStatsDto;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnosHoyDto;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IPacienteRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

public class HomeViewModel extends AndroidViewModel {

    private final IPacienteRepository pacienteRepository;
    private final TurnosRepository turnosRepository;

    private final MediatorLiveData<Paciente> paciente = new MediatorLiveData<>();
    private final MediatorLiveData<String> error = new MediatorLiveData<>();
    private final MediatorLiveData<TurnosHoyDto> turnosHoy = new MediatorLiveData<>();
    private final MediatorLiveData<PacienteTurnosStatsDto> turnosStats = new MediatorLiveData<>();

    private LiveData<RepositoryResult<Paciente>> pacienteSource;
    private LiveData<RepositoryResult<TurnosHoyDto>> turnosHoySource;
    private LiveData<RepositoryResult<PacienteTurnosStatsDto>> turnosStatsSource;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.pacienteRepository = new PacienteRepository(application.getApplicationContext());
        this.turnosRepository = new TurnosRepository(application.getApplicationContext());
    }

    public LiveData<Paciente> getPaciente() {
        return paciente;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<TurnosHoyDto> getTurnosHoy() {
        return turnosHoy;
    }

    public LiveData<PacienteTurnosStatsDto> getTurnosStats() {
        return turnosStats;
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

    public void loadDashboard() {
        loadTurnosHoy();
        loadTurnosStats();
    }

    private void loadTurnosHoy() {
        if (turnosHoySource != null) {
            turnosHoy.removeSource(turnosHoySource);
        }

        turnosHoySource = turnosRepository.getTurnosHoyPacienteActual();
        turnosHoy.addSource(turnosHoySource, result -> {
            if (result != null && result.isSuccess()) {
                turnosHoy.setValue(result.getData());
            } else {
                error.setValue(result == null ? "Error desconocido" : result.getError());
            }

            if (turnosHoySource != null) {
                turnosHoy.removeSource(turnosHoySource);
            }
        });
    }

    private void loadTurnosStats() {
        if (turnosStatsSource != null) {
            turnosStats.removeSource(turnosStatsSource);
        }

        turnosStatsSource = turnosRepository.getStatsTurnosPacienteActual();
        turnosStats.addSource(turnosStatsSource, result -> {
            if (result != null && result.isSuccess()) {
                turnosStats.setValue(result.getData());
            } else {
                error.setValue(result == null ? "Error desconocido" : result.getError());
            }

            if (turnosStatsSource != null) {
                turnosStats.removeSource(turnosStatsSource);
            }
        });
    }
}
