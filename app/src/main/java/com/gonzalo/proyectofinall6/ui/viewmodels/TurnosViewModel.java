package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.repositorios.TurnosRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ITurnosRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;
import com.gonzalo.proyectofinall6.dominio.modelos.Turno;

import java.util.List;

public class TurnosViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Turno>> proximosTurnos = new MediatorLiveData<>();
    private final MediatorLiveData<List<Turno>> historialTurnos = new MediatorLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> turnoCancelado = new MutableLiveData<>();
    private final MutableLiveData<Boolean> valoracionGuardada = new MutableLiveData<>();
    private final MutableLiveData<String> valoracionError = new MutableLiveData<>();
    private final ITurnosRepository repository;

    private final MediatorLiveData<RepositoryResult<ITurnosRepository.TurnosResumen>> turnosResumenResult = new MediatorLiveData<>();

    private LiveData<RepositoryResult<ITurnosRepository.TurnosResumen>> turnosSource;
    private LiveData<RepositoryResult<Void>> cancelarSource;
    private LiveData<RepositoryResult<Void>> valorarSource;

    public TurnosViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TurnosRepository(application.getApplicationContext());

        proximosTurnos.addSource(turnosResumenResult, result -> {
            if (result == null)
                return;
            if (result.isSuccess() && result.getData() != null) {
                proximosTurnos.setValue(result.getData().getProximos());
            }
        });

        historialTurnos.addSource(turnosResumenResult, result -> {
            if (result == null)
                return;
            if (result.isSuccess() && result.getData() != null) {
                historialTurnos.setValue(result.getData().getHistorial());
            }
        });
    }

    public LiveData<List<Turno>> getProximosTurnos() {
        return proximosTurnos;
    }

    public LiveData<List<Turno>> getHistorialTurnos() {
        return historialTurnos;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getTurnoCancelado() {
        return turnoCancelado;
    }

    public LiveData<Boolean> getValoracionGuardada() {
        return valoracionGuardada;
    }

    public LiveData<String> getValoracionError() {
        return valoracionError;
    }

    public void loadTurnos() {
        if (turnosSource != null) {
            turnosResumenResult.removeSource(turnosSource);
        }

        turnosSource = repository.getTurnosDelPacienteActual();
        turnosResumenResult.addSource(turnosSource, result -> {
            if (result == null)
                return;

            if (result.isSuccess() && result.getData() != null) {
                turnosResumenResult.setValue(result);
            } else {
                error.setValue(result.getError());
            }

            if (turnosSource != null) {
                turnosResumenResult.removeSource(turnosSource);
            }
        });
    }

    public void cancelarTurno(int turnoId, String motivo) {
        if (cancelarSource != null) {
            // no-op: nothing to remove because we don't keep a mediator for cancel.
            cancelarSource = null;
        }

        LiveData<RepositoryResult<Void>> source = repository.cancelarTurno(turnoId, motivo);
        cancelarSource = source;

        MediatorLiveData<RepositoryResult<Void>> mediator = new MediatorLiveData<>();
        mediator.addSource(source, result -> {
            if (result != null && result.isSuccess()) {
                turnoCancelado.setValue(true);
                loadTurnos();
            } else {
                turnoCancelado.setValue(false);
            }
            mediator.removeSource(source);
        });
    }

    public void valorarTurno(int turnoId, int estrellas, String comentario) {
        LiveData<RepositoryResult<Void>> source = repository.crearValoracion(turnoId, estrellas, comentario);
        valorarSource = source;

        MediatorLiveData<RepositoryResult<Void>> mediator = new MediatorLiveData<>();
        mediator.addSource(source, result -> {
            if (result != null && result.isSuccess()) {
                valoracionGuardada.setValue(true);
                valoracionError.setValue(null);
                loadTurnos();
            } else {
                valoracionGuardada.setValue(false);
                valoracionError.setValue(result != null ? result.getError() : "Error al guardar valoración");
            }
            mediator.removeSource(source);
        });
    }
}
