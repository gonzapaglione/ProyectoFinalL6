package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.gonzalo.proyectofinall6.data.repositorios.TurnosRepository;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ITurnosRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.List;

public class AboutUsViewModel extends AndroidViewModel {

    private final ITurnosRepository turnosRepository;

    private final MediatorLiveData<List<OdontologoResponse>> odontologos = new MediatorLiveData<>();
    private final MediatorLiveData<String> error = new MediatorLiveData<>();

    private LiveData<RepositoryResult<List<OdontologoResponse>>> odontologosSource;

    public AboutUsViewModel(@NonNull Application application) {
        super(application);
        this.turnosRepository = new TurnosRepository(application.getApplicationContext());
    }

    public LiveData<List<OdontologoResponse>> getOdontologos() {
        return odontologos;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchOdontologos() {
        if (odontologosSource != null) {
            odontologos.removeSource(odontologosSource);
        }

        odontologosSource = turnosRepository.getOdontologos();
        odontologos.addSource(odontologosSource, result -> {
            if (result != null && result.isSuccess()) {
                odontologos.setValue(result.getData());
                error.setValue(null);
            } else {
                error.setValue(result == null ? "Error al cargar odontólogos" : result.getError());
            }

            if (odontologosSource != null) {
                odontologos.removeSource(odontologosSource);
            }
        });
    }
}
