package com.gonzalo.proyectofinall6.ui.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.remote.api.ApiService;
import com.gonzalo.proyectofinall6.data.remote.api.RetrofitClient;
import com.gonzalo.proyectofinall6.data.remote.dto.ApiResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.GetOdontologosResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HorarioDisponible;
import com.gonzalo.proyectofinall6.data.remote.dto.HorariosDisponiblesResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.MotivoConsulta;
import com.gonzalo.proyectofinall6.data.remote.dto.MotivosConsultaResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PacienteResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoResponse;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private SharedPreferences sharedPreferences;

    public ReservarViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    public void crearTurno() {
        int userId = sharedPreferences.getInt("user_id", -1);
        if (userId != -1 && odontologoId.getValue() != null && fecha.getValue() != null && horaInicio.getValue() != null && motivoConsultaId.getValue() != null && obraSocialId.getValue() != null) {
            TurnoRequest turnoRequest = new TurnoRequest(
                    userId,
                    odontologoId.getValue(),
                    fecha.getValue(),
                    horaInicio.getValue(),
                    motivoConsultaId.getValue(),
                    obraSocialId.getValue()
            );
            ApiService apiService = RetrofitClient.getApiService();
            apiService.crearTurno(turnoRequest).enqueue(new Callback<ApiResponse<TurnoResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<TurnoResponse>> call, Response<ApiResponse<TurnoResponse>> response) {
                    turnoCreado.postValue(response.body());
                }

                @Override
                public void onFailure(Call<ApiResponse<TurnoResponse>> call, Throwable t) {
                    turnoCreado.postValue(null);
                }
            });
        }
    }

    public void fetchObrasSociales() {
        int userId = sharedPreferences.getInt("user_id", -1);
        if (userId != -1) {
            ApiService apiService = RetrofitClient.getApiService();
            apiService.getPaciente(String.valueOf(userId)).enqueue(new Callback<PacienteResponse>() {
                @Override
                public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        obrasSociales.postValue(response.body().getData().getObrasSociales());
                    }
                }

                @Override
                public void onFailure(Call<PacienteResponse> call, Throwable t) {
                    // Handle failure
                }
            });
        }
    }

    public void fetchOdontologos() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getOdontologos().enqueue(new Callback<GetOdontologosResponse>() {
            @Override
            public void onResponse(Call<GetOdontologosResponse> call, Response<GetOdontologosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    odontologos.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GetOdontologosResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void fetchHorariosDisponibles() {
        if (odontologoId.getValue() == null || fecha.getValue() == null) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getHorariosDisponibles(odontologoId.getValue(), fecha.getValue()).enqueue(new Callback<HorariosDisponiblesResponse>() {
            @Override
            public void onResponse(Call<HorariosDisponiblesResponse> call, Response<HorariosDisponiblesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<HorarioDisponible> allHorarios = response.body().getData();
                    processHorarios(allHorarios);
                } else {
                    horariosDisponibles.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<HorariosDisponiblesResponse> call, Throwable t) {
                horariosDisponibles.postValue(new ArrayList<>());
            }
        });
    }

    private void processHorarios(List<HorarioDisponible> allHorarios) {
        new Thread(() -> {
            List<HorarioDisponible> availableHorarios = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String todayStr = sdf.format(new Date());
            boolean isToday = fecha.getValue() != null && fecha.getValue().equals(todayStr);

            if (isToday) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                Date now = new Date();
                for (HorarioDisponible horario : allHorarios) {
                    if (horario.isDisponible()) {
                        try {
                            Date time = timeFormat.parse(horario.getHora());
                            if (time.after(now)) {
                                availableHorarios.add(horario);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                for (HorarioDisponible horario : allHorarios) {
                    if (horario.isDisponible()) {
                        availableHorarios.add(horario);
                    }
                }
            }
            horariosDisponibles.postValue(availableHorarios);
        }).start();
    }


    public void fetchMotivosConsulta() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getMotivosConsulta().enqueue(new Callback<MotivosConsultaResponse>() {
            @Override
            public void onResponse(Call<MotivosConsultaResponse> call, Response<MotivosConsultaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    motivosConsulta.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<MotivosConsultaResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // LiveData Getters and Setters
    public LiveData<List<OdontologoResponse>> getOdontologos() { return odontologos; }
    public void setOdontologoId(int id) { odontologoId.setValue(id); }
    public LiveData<Integer> getOdontologoId() { return odontologoId; }
    public void setOdontologoNombre(String nombre) { odontologoNombre.setValue(nombre); }
    public LiveData<String> getOdontologoNombre() { return odontologoNombre; }
    public void setFecha(String f) { fecha.setValue(f); }
    public LiveData<String> getFecha() { return fecha; }
    public void setHoraInicio(String h) { horaInicio.setValue(h); }
    public LiveData<String> getHoraInicio() { return horaInicio; }
    public LiveData<List<HorarioDisponible>> getHorariosDisponibles() { return horariosDisponibles; }
    public LiveData<List<MotivoConsulta>> getMotivosConsulta() { return motivosConsulta; }
    public void setMotivoConsultaId(int id) { motivoConsultaId.setValue(id); }
    public LiveData<Integer> getMotivoConsultaId() { return motivoConsultaId; }
    public void setMotivoConsultaNombre(String nombre) { motivoConsultaNombre.setValue(nombre); }
    public LiveData<String> getMotivoConsultaNombre() { return motivoConsultaNombre; }
    public LiveData<List<ObraSocial>> getObrasSociales() { return obrasSociales; }
    public void setObraSocialId(int id) { obraSocialId.setValue(id); }
    public LiveData<Integer> getObraSocialId() { return obraSocialId; }
    public void setObraSocialNombre(String nombre) { obraSocialNombre.setValue(nombre); }
    public LiveData<String> getObraSocialNombre() { return obraSocialNombre; }
    public LiveData<ApiResponse<TurnoResponse>> getTurnoCreado() { return turnoCreado; }

}
