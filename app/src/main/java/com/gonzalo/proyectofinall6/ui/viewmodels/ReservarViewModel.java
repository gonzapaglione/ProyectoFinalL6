package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class ReservarViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<OdontologoResponse>> odontologos = new MediatorLiveData<>();
    private final MutableLiveData<Integer> odontologoId = new MutableLiveData<>();
    private final MutableLiveData<String> odontologoNombre = new MutableLiveData<>();
    private final MutableLiveData<String> fecha = new MutableLiveData<>();
    private final MutableLiveData<String> horaInicio = new MutableLiveData<>();
    private final MediatorLiveData<List<HorarioDisponible>> horariosDisponibles = new MediatorLiveData<>();
    private final MediatorLiveData<List<MotivoConsulta>> motivosConsulta = new MediatorLiveData<>();
    private final MutableLiveData<Integer> motivoConsultaId = new MutableLiveData<>();
    private final MutableLiveData<String> motivoConsultaNombre = new MutableLiveData<>();
    private final MediatorLiveData<List<ObraSocial>> obrasSociales = new MediatorLiveData<>();
    private final MutableLiveData<Integer> obraSocialId = new MutableLiveData<>();
    private final MutableLiveData<String> obraSocialNombre = new MutableLiveData<>();
    private final MediatorLiveData<ApiResponse<TurnoResponse>> turnoCreado = new MediatorLiveData<>();

    private final MediatorLiveData<String> fechaHoraFormateada = new MediatorLiveData<>();

    private LiveData<RepositoryResult<List<OdontologoResponse>>> odontologosSource;
    private LiveData<RepositoryResult<List<MotivoConsulta>>> motivosSource;
    private LiveData<RepositoryResult<List<ObraSocial>>> obrasSource;
    private LiveData<RepositoryResult<List<HorarioDisponible>>> horariosSource;
    private LiveData<RepositoryResult<ApiResponse<TurnoResponse>>> crearTurnoSource;

    private final ITurnosRepository turnosRepository;
    private final IPacienteRepository pacienteRepository;

    public ReservarViewModel(@NonNull Application application) {
        super(application);
        this.turnosRepository = new TurnosRepository(application.getApplicationContext());
        this.pacienteRepository = new PacienteRepository(application.getApplicationContext());

        fechaHoraFormateada.addSource(fecha, v -> actualizarFechaHoraFormateada());
        fechaHoraFormateada.addSource(horaInicio, v -> actualizarFechaHoraFormateada());
    }

    private void actualizarFechaHoraFormateada() {
        String f = fecha.getValue();
        String h = horaInicio.getValue();
        if (f == null || h == null)
            return;

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(f);
            if (date == null)
                return;

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, dd 'de' MMMM", new Locale("es", "ES"));
            String formattedDate = dayFormat.format(date);

            String formattedTime = h.length() >= 5 ? h.substring(0, 5) : h;
            // Mantengo el sufijo como estaba en tu UI (AM).
            fechaHoraFormateada.setValue(formattedDate + " - " + formattedTime + " AM");
        } catch (ParseException ignored) {
            // si falla el parseo, no rompemos la UI
        }
    }

    public void crearTurno() {
        if (odontologoId.getValue() == null || fecha.getValue() == null || horaInicio.getValue() == null
                || motivoConsultaId.getValue() == null || obraSocialId.getValue() == null) {
            turnoCreado.postValue(null);
            return;
        }

        if (crearTurnoSource != null) {
            turnoCreado.removeSource(crearTurnoSource);
        }

        crearTurnoSource = turnosRepository.crearTurno(
                odontologoId.getValue(),
                fecha.getValue(),
                horaInicio.getValue(),
                motivoConsultaId.getValue(),
                obraSocialId.getValue());

        turnoCreado.addSource(crearTurnoSource, result -> {
            if (result != null && result.isSuccess()) {
                turnoCreado.setValue(result.getData());
            } else {
                turnoCreado.setValue(null);
            }
            if (crearTurnoSource != null) {
                turnoCreado.removeSource(crearTurnoSource);
            }
        });
    }

    public void fetchObrasSociales() {
        if (obrasSource != null) {
            obrasSociales.removeSource(obrasSource);
        }

        obrasSource = pacienteRepository.getObrasSocialesDelPacienteActual();
        obrasSociales.addSource(obrasSource, result -> {
            if (result != null && result.isSuccess()) {
                obrasSociales.setValue(result.getData());
            }
            if (obrasSource != null) {
                obrasSociales.removeSource(obrasSource);
            }
        });
    }

    public void fetchOdontologos() {
        if (odontologosSource != null) {
            odontologos.removeSource(odontologosSource);
        }

        odontologosSource = turnosRepository.getOdontologos();
        odontologos.addSource(odontologosSource, result -> {
            if (result != null && result.isSuccess()) {
                odontologos.setValue(result.getData());
            }
            if (odontologosSource != null) {
                odontologos.removeSource(odontologosSource);
            }
        });
    }

    public void fetchHorariosDisponibles() {
        if (odontologoId.getValue() == null || fecha.getValue() == null)
            return;

        if (horariosSource != null) {
            horariosDisponibles.removeSource(horariosSource);
        }

        horariosSource = turnosRepository.getHorariosDisponibles(odontologoId.getValue(), fecha.getValue());
        horariosDisponibles.addSource(horariosSource, result -> {
            if (result != null && result.isSuccess()) {
                horariosDisponibles.setValue(result.getData());
            } else {
                horariosDisponibles.setValue(null);
            }
            if (horariosSource != null) {
                horariosDisponibles.removeSource(horariosSource);
            }
        });
    }

    public void fetchMotivosConsulta() {
        if (motivosSource != null) {
            motivosConsulta.removeSource(motivosSource);
        }

        motivosSource = turnosRepository.getMotivosConsulta();
        motivosConsulta.addSource(motivosSource, result -> {
            if (result != null && result.isSuccess()) {
                motivosConsulta.setValue(result.getData());
            }
            if (motivosSource != null) {
                motivosConsulta.removeSource(motivosSource);
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

    public LiveData<String> getFechaHoraFormateada() {
        return fechaHoraFormateada;
    }

}
