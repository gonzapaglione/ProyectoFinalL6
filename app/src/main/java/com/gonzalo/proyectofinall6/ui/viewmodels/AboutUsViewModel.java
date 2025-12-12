package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.gonzalo.proyectofinall6.data.repositorios.TurnosRepository;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PromedioValoracionResponse;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ITurnosRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AboutUsViewModel extends AndroidViewModel {

    private final ITurnosRepository turnosRepository;

    private final MediatorLiveData<List<OdontologoResponse>> odontologos = new MediatorLiveData<>();
    private final MediatorLiveData<String> error = new MediatorLiveData<>();
    private final MediatorLiveData<Map<Integer, PromedioValoracionResponse>> promedios = new MediatorLiveData<>();

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

    public LiveData<Map<Integer, PromedioValoracionResponse>> getPromedios() {
        return promedios;
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

                // Cargar promedios por odontólogo
                Map<Integer, PromedioValoracionResponse> map = new HashMap<>();
                promedios.setValue(map);
                if (result.getData() != null) {
                    for (OdontologoResponse o : result.getData()) {
                        if (o == null)
                            continue;
                        int id = o.getId();
                        LiveData<RepositoryResult<PromedioValoracionResponse>> src = turnosRepository
                                .getPromedioValoracionOdontologo(id);

                        promedios.addSource(src, promRes -> {
                            if (promRes != null && promRes.isSuccess() && promRes.getData() != null) {
                                Map<Integer, PromedioValoracionResponse> current = promedios.getValue();
                                if (current == null)
                                    current = new HashMap<>();
                                current.put(id, promRes.getData());
                                promedios.setValue(current);
                            }
                            promedios.removeSource(src);
                        });
                    }
                }
            } else {
                error.setValue(result == null ? "Error al cargar odontólogos" : result.getError());
            }

            if (odontologosSource != null) {
                odontologos.removeSource(odontologosSource);
            }
        });
    }
}
