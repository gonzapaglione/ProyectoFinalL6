package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.repositorios.TurnosRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ITurnosRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;
import com.gonzalo.proyectofinall6.dominio.modelos.Turno;

import java.util.List;

public class TurnosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Turno>> proximosTurnos = new MutableLiveData<>();
    private final MutableLiveData<List<Turno>> historialTurnos = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> turnoCancelado = new MutableLiveData<>();
    private final ITurnosRepository repository;

    public TurnosViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TurnosRepository(application.getApplicationContext());
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

    public void loadTurnos() {
        repository.getTurnosDelPacienteActual().observeForever(result -> {
            if (result == null)
                return;
            if (result.isSuccess() && result.getData() != null) {
                proximosTurnos.postValue(result.getData().getProximos());
                historialTurnos.postValue(result.getData().getHistorial());
            } else {
                error.postValue(result.getError());
            }
        });
    }

    public void cancelarTurno(int turnoId, String motivo) {
        repository.cancelarTurno(turnoId, motivo).observeForever(result -> {
            if (result != null && result.isSuccess()) {
                turnoCancelado.postValue(true);
                loadTurnos();
            } else {
                turnoCancelado.postValue(false);
            }
        });
    }
}
