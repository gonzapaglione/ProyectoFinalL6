package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.repositorios.PacienteRepository;
import com.gonzalo.proyectofinall6.data.repositorios.TurnosRepository;
import com.gonzalo.proyectofinall6.data.remote.dto.ApiResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HorarioDisponible;
import com.gonzalo.proyectofinall6.data.remote.dto.MotivoConsulta;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoResponse;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IPacienteRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ITurnosRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.List;

public class ReservarViewModel extends AndroidViewModel {

    private final MutableLiveData<List<OdontologoResponse>> odontologos = new MutableLiveData<>();
    private final MutableLiveData<Integer> odontologoId = new MutableLiveData<>();
    private final MutableLiveData<String> odontologoNombre = new MutableLiveData<>();
    private final MutableLiveData<String> fecha = new MutableLiveData<>();
    private final MutableLiveData<String> horaInicio = new MutableLiveData<>();
    private final MutableLiveData<List<HorarioDisponible>> horariosDisponibles = new MutableLiveData<>();
    private final MutableLiveData<List<MotivoConsulta>> motivosConsulta = new MutableLiveData<>();
    private final MutableLiveData<Integer> motivoConsultaId = new MutableLiveData<>();
    private final MutableLiveData<String> motivoConsultaNombre = new MutableLiveData<>();
    private final MutableLiveData<List<ObraSocial>> obrasSociales = new MutableLiveData<>();
    private final MutableLiveData<Integer> obraSocialId = new MutableLiveData<>();
    private final MutableLiveData<String> obraSocialNombre = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<TurnoResponse>> turnoCreado = new MutableLiveData<>();

    private final ITurnosRepository turnosRepository;
    private final IPacienteRepository pacienteRepository;

    public ReservarViewModel(@NonNull Application application) {
        super(application);
        this.turnosRepository = new TurnosRepository(application.getApplicationContext());
        this.pacienteRepository = new PacienteRepository(application.getApplicationContext());
    }

    public void crearTurno() {
        if (odontologoId.getValue() == null || fecha.getValue() == null || horaInicio.getValue() == null
                || motivoConsultaId.getValue() == null || obraSocialId.getValue() == null) {
            turnoCreado.postValue(null);
            return;
        }

        turnosRepository.crearTurno(
                odontologoId.getValue(),
                fecha.getValue(),
                horaInicio.getValue(),
                motivoConsultaId.getValue(),
                obraSocialId.getValue()).observeForever(result -> {
                    if (result != null && result.isSuccess()) {
                        turnoCreado.postValue(result.getData());
                    } else {
                        turnoCreado.postValue(null);
                    }
                });
    }

    public void fetchObrasSociales() {
        pacienteRepository.getObrasSocialesDelPacienteActual().observeForever(result -> {
            if (result != null && result.isSuccess()) {
                obrasSociales.postValue(result.getData());
            }
        });
    }

    public void fetchOdontologos() {
        turnosRepository.getOdontologos().observeForever(result -> {
            if (result != null && result.isSuccess()) {
                odontologos.postValue(result.getData());
            }
        });
    }

    public void fetchHorariosDisponibles() {
        if (odontologoId.getValue() == null || fecha.getValue() == null)
            return;

        turnosRepository.getHorariosDisponibles(odontologoId.getValue(), fecha.getValue()).observeForever(result -> {
            if (result != null && result.isSuccess()) {
                horariosDisponibles.postValue(result.getData());
            } else {
                horariosDisponibles.postValue(null);
            }
        });
    }

    public void fetchMotivosConsulta() {
        turnosRepository.getMotivosConsulta().observeForever(result -> {
            if (result != null && result.isSuccess()) {
                motivosConsulta.postValue(result.getData());
            }
        });
    }

    // LiveData Getters and Setters
    public LiveData<List<OdontologoResponse>> getOdontologos() {
        return odontologos;
    }

    public void setOdontologoId(int id) {
        odontologoId.setValue(id);
    }

    public LiveData<Integer> getOdontologoId() {
        return odontologoId;
    }

    public void setOdontologoNombre(String nombre) {
        odontologoNombre.setValue(nombre);
    }

    public LiveData<String> getOdontologoNombre() {
        return odontologoNombre;
    }

    public void setFecha(String f) {
        fecha.setValue(f);
    }

    public LiveData<String> getFecha() {
        return fecha;
    }

    public void setHoraInicio(String h) {
        horaInicio.setValue(h);
    }

    public LiveData<String> getHoraInicio() {
        return horaInicio;
    }

    public LiveData<List<HorarioDisponible>> getHorariosDisponibles() {
        return horariosDisponibles;
    }

    public LiveData<List<MotivoConsulta>> getMotivosConsulta() {
        return motivosConsulta;
    }

    public void setMotivoConsultaId(int id) {
        motivoConsultaId.setValue(id);
    }

    public LiveData<Integer> getMotivoConsultaId() {
        return motivoConsultaId;
    }

    public void setMotivoConsultaNombre(String nombre) {
        motivoConsultaNombre.setValue(nombre);
    }

    public LiveData<String> getMotivoConsultaNombre() {
        return motivoConsultaNombre;
    }

    public LiveData<List<ObraSocial>> getObrasSociales() {
        return obrasSociales;
    }

    public void setObraSocialId(int id) {
        obraSocialId.setValue(id);
    }

    public LiveData<Integer> getObraSocialId() {
        return obraSocialId;
    }

    public void setObraSocialNombre(String nombre) {
        obraSocialNombre.setValue(nombre);
    }

    public LiveData<String> getObraSocialNombre() {
        return obraSocialNombre;
    }

    public LiveData<ApiResponse<TurnoResponse>> getTurnoCreado() {
        return turnoCreado;
    }

}
