package com.gonzalo.proyectofinall6.data.repositorios;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzalo.proyectofinall6.data.remote.api.ApiService;
import com.gonzalo.proyectofinall6.data.remote.api.RetrofitClient;
import com.gonzalo.proyectofinall6.data.remote.dto.ApiResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.CancelarTurnoRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.CrearValoracionRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.GetOdontologosResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HistoriaClinicaResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HistorialResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HorarioDisponible;
import com.gonzalo.proyectofinall6.data.remote.dto.HorariosDisponiblesResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.MotivoConsulta;
import com.gonzalo.proyectofinall6.data.remote.dto.MotivosConsultaResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PacienteTurnosStatsDto;
import com.gonzalo.proyectofinall6.data.remote.dto.PacienteResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PromedioValoracionResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.ProximosTurnosResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnosHoyDto;
import com.gonzalo.proyectofinall6.data.remote.dto.ValoracionResponse;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ISessionRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.ITurnosRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;
import com.gonzalo.proyectofinall6.dominio.modelos.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TurnosRepository implements ITurnosRepository {

    private final ApiService apiService;
    private final ISessionRepository sessionRepository;

    public TurnosRepository(Context context) {
        this.apiService = RetrofitClient.getApiService();
        this.sessionRepository = new SessionRepository(context);
    }

    private int getUserId() {
        return sessionRepository.getUserId();
    }

    public LiveData<RepositoryResult<TurnosHoyDto>> getTurnosHoyPacienteActual() {
        MutableLiveData<RepositoryResult<TurnosHoyDto>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        apiService.getTurnosHoy(userId).enqueue(new Callback<ApiResponse<TurnosHoyDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<TurnosHoyDto>> call, Response<ApiResponse<TurnosHoyDto>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                        && response.body().getData() != null) {
                    liveData.postValue(RepositoryResult.success(response.body().getData()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al obtener turnos de hoy"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TurnosHoyDto>> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al obtener turnos de hoy"));
            }
        });

        return liveData;
    }

    public LiveData<RepositoryResult<PacienteTurnosStatsDto>> getStatsTurnosPacienteActual() {
        MutableLiveData<RepositoryResult<PacienteTurnosStatsDto>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        apiService.getTurnosStats(userId).enqueue(new Callback<ApiResponse<PacienteTurnosStatsDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<PacienteTurnosStatsDto>> call,
                    Response<ApiResponse<PacienteTurnosStatsDto>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                        && response.body().getData() != null) {
                    liveData.postValue(RepositoryResult.success(response.body().getData()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al obtener estadísticas de turnos"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PacienteTurnosStatsDto>> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al obtener estadísticas de turnos"));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<List<OdontologoResponse>>> getOdontologos() {
        MutableLiveData<RepositoryResult<List<OdontologoResponse>>> liveData = new MutableLiveData<>();

        apiService.getOdontologos().enqueue(new Callback<GetOdontologosResponse>() {
            @Override
            public void onResponse(Call<GetOdontologosResponse> call, Response<GetOdontologosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(RepositoryResult.success(response.body().getData()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al cargar odontólogos"));
                }
            }

            @Override
            public void onFailure(Call<GetOdontologosResponse> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al cargar odontólogos"));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<List<MotivoConsulta>>> getMotivosConsulta() {
        MutableLiveData<RepositoryResult<List<MotivoConsulta>>> liveData = new MutableLiveData<>();

        apiService.getMotivosConsulta().enqueue(new Callback<MotivosConsultaResponse>() {
            @Override
            public void onResponse(Call<MotivosConsultaResponse> call, Response<MotivosConsultaResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(RepositoryResult.success(response.body().getData()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al cargar motivos de consulta"));
                }
            }

            @Override
            public void onFailure(Call<MotivosConsultaResponse> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al cargar motivos de consulta"));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<List<HorarioDisponible>>> getHorariosDisponibles(int idOdontologo, String fecha) {
        MutableLiveData<RepositoryResult<List<HorarioDisponible>>> liveData = new MutableLiveData<>();

        apiService.getHorariosDisponibles(idOdontologo, fecha).enqueue(new Callback<HorariosDisponiblesResponse>() {
            @Override
            public void onResponse(Call<HorariosDisponiblesResponse> call,
                    Response<HorariosDisponiblesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<HorarioDisponible> filtered = filtrarHorariosDisponibles(response.body().getData(), fecha);
                    liveData.postValue(RepositoryResult.success(filtered));
                } else {
                    liveData.postValue(RepositoryResult.success(new ArrayList<>()));
                }
            }

            @Override
            public void onFailure(Call<HorariosDisponiblesResponse> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al cargar horarios disponibles"));
            }
        });

        return liveData;
    }

    private List<HorarioDisponible> filtrarHorariosDisponibles(List<HorarioDisponible> allHorarios,
            String fechaSeleccionada) {
        if (allHorarios == null)
            return new ArrayList<>();

        List<HorarioDisponible> availableHorarios = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(new Date());
        boolean isToday = fechaSeleccionada != null && fechaSeleccionada.equals(todayStr);

        if (!isToday) {
            for (HorarioDisponible horario : allHorarios) {
                if (horario != null && horario.isDisponible()) {
                    availableHorarios.add(horario);
                }
            }
            return availableHorarios;
        }

        // Si es HOY: filtrar por hora del DISPOSITIVO con margen de 30 min.
        // Nota: usamos LocalTime (mismo día) porque acá ya estamos en "hoy".
        LocalTime limite = LocalTime.now().plusMinutes(30);
        for (HorarioDisponible horario : allHorarios) {
            if (horario == null || !horario.isDisponible() || horario.getHora() == null)
                continue;

            // Normalizamos: backend puede mandar HH:mm o HH:mm:ss
            String horaStr = horario.getHora();
            if (horaStr.length() >= 5) {
                horaStr = horaStr.substring(0, 5);
            }

            try {
                LocalTime hora = LocalTime.parse(horaStr);
                if (!hora.isBefore(limite)) {
                    availableHorarios.add(horario);
                }
            } catch (Exception ignored) {
                // Si no se puede parsear, por seguridad NO lo mostramos
            }
        }

        return availableHorarios;
    }

    @Override
    public LiveData<RepositoryResult<ApiResponse<TurnoResponse>>> crearTurno(int idOdontologo, String fecha,
            String horaInicio, int idMotivoConsulta, int idObraSocial) {
        MutableLiveData<RepositoryResult<ApiResponse<TurnoResponse>>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        TurnoRequest request = new TurnoRequest(userId, idOdontologo, fecha, horaInicio, idMotivoConsulta,
                idObraSocial);
        apiService.crearTurno(request).enqueue(new Callback<ApiResponse<TurnoResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<TurnoResponse>> call,
                    Response<ApiResponse<TurnoResponse>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(RepositoryResult.success(response.body()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al crear el turno"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TurnoResponse>> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al crear el turno"));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<HistoriaClinicaResponse>> getHistoriaClinicaPorTurno(int idTurno) {
        MutableLiveData<RepositoryResult<HistoriaClinicaResponse>> liveData = new MutableLiveData<>();

        apiService.getHistoriaClinicaPorTurno(idTurno).enqueue(new Callback<ApiResponse<HistoriaClinicaResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<HistoriaClinicaResponse>> call,
                    Response<ApiResponse<HistoriaClinicaResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<HistoriaClinicaResponse> body = response.body();
                    if (body.isSuccess() && body.getData() != null) {
                        liveData.postValue(RepositoryResult.success(body.getData()));
                    } else {
                        String msg = body.getMessage() != null ? body.getMessage()
                                : "No se pudo obtener la historia clínica";
                        liveData.postValue(RepositoryResult.error(msg));
                    }
                } else {
                    liveData.postValue(RepositoryResult.error("Error al obtener la historia clínica"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HistoriaClinicaResponse>> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al obtener la historia clínica"));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<TurnosResumen>> getTurnosDelPacienteActual() {
        MutableLiveData<RepositoryResult<TurnosResumen>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        apiService.getPaciente(String.valueOf(userId)).enqueue(new Callback<PacienteResponse>() {
            @Override
            public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()
                        || response.body().getData() == null) {
                    liveData.postValue(RepositoryResult.error("Error al obtener los datos del paciente."));
                    return;
                }

                long pacienteId = response.body().getData().getIdPaciente();

                AtomicReference<List<Turno>> proximosRef = new AtomicReference<>();
                AtomicReference<List<Turno>> historialRef = new AtomicReference<>();
                AtomicReference<String> errorRef = new AtomicReference<>();

                Runnable maybePublish = () -> {
                    if (errorRef.get() != null) {
                        liveData.postValue(RepositoryResult.error(errorRef.get()));
                        return;
                    }
                    if (proximosRef.get() != null && historialRef.get() != null) {
                        liveData.postValue(
                                RepositoryResult.success(new TurnosResumen(proximosRef.get(), historialRef.get())));
                    }
                };

                apiService.getProximos(pacienteId).enqueue(new Callback<ProximosTurnosResponse>() {
                    @Override
                    public void onResponse(Call<ProximosTurnosResponse> call,
                            Response<ProximosTurnosResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            proximosRef.set(response.body().getData());
                        } else {
                            proximosRef.set(new ArrayList<>());
                        }
                        maybePublish.run();
                    }

                    @Override
                    public void onFailure(Call<ProximosTurnosResponse> call, Throwable t) {
                        errorRef.set("Error de red al cargar próximos turnos");
                        maybePublish.run();
                    }
                });

                apiService.getHistorial(pacienteId).enqueue(new Callback<HistorialResponse>() {
                    @Override
                    public void onResponse(Call<HistorialResponse> call, Response<HistorialResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()
                                && response.body().getData() != null) {
                            List<Turno> allTurnos = response.body().getData();
                            List<Turno> filtered = new ArrayList<>();
                            for (Turno turno : allTurnos) {
                                if (turno != null && turno.getEstadoTurno() != null
                                        && !"Programado".equalsIgnoreCase(turno.getEstadoTurno())) {
                                    filtered.add(turno);
                                }
                            }
                            historialRef.set(filtered);
                        } else {
                            historialRef.set(new ArrayList<>());
                        }
                        maybePublish.run();
                    }

                    @Override
                    public void onFailure(Call<HistorialResponse> call, Throwable t) {
                        errorRef.set("Error de red al cargar historial");
                        maybePublish.run();
                    }
                });
            }

            @Override
            public void onFailure(Call<PacienteResponse> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al obtener datos del paciente."));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<Void>> cancelarTurno(int turnoId, String motivo) {
        MutableLiveData<RepositoryResult<Void>> liveData = new MutableLiveData<>();

        CancelarTurnoRequest request = new CancelarTurnoRequest(turnoId, motivo);
        apiService.cancelarTurno(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(RepositoryResult.success(null));
                } else {
                    liveData.postValue(RepositoryResult.error("No se pudo cancelar el turno"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al cancelar el turno"));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<Void>> crearValoracion(int turnoId, int estrellas, String comentario) {
        MutableLiveData<RepositoryResult<Void>> liveData = new MutableLiveData<>();

        int userId = getUserId();
        if (userId == -1) {
            liveData.setValue(RepositoryResult.error("Error: No se pudo obtener el ID de usuario."));
            return liveData;
        }

        CrearValoracionRequest request = new CrearValoracionRequest(turnoId, userId, estrellas, comentario);
        apiService.crearValoracion(request).enqueue(new Callback<ApiResponse<ValoracionResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ValoracionResponse>> call,
                    Response<ApiResponse<ValoracionResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(RepositoryResult.success(null));
                } else {
                    String msg = (response.body() != null && response.body().getMessage() != null)
                            ? response.body().getMessage()
                            : "No se pudo guardar la valoración";
                    liveData.postValue(RepositoryResult.error(msg));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ValoracionResponse>> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al guardar la valoración"));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<RepositoryResult<PromedioValoracionResponse>> getPromedioValoracionOdontologo(int idOdontologo) {
        MutableLiveData<RepositoryResult<PromedioValoracionResponse>> liveData = new MutableLiveData<>();

        apiService.getPromedioValoracion(idOdontologo).enqueue(new Callback<ApiResponse<PromedioValoracionResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PromedioValoracionResponse>> call,
                    Response<ApiResponse<PromedioValoracionResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(RepositoryResult.success(response.body().getData()));
                } else {
                    liveData.postValue(RepositoryResult.error("Error al cargar el promedio de valoraciones"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PromedioValoracionResponse>> call, Throwable t) {
                liveData.postValue(RepositoryResult.error("Error de red al cargar el promedio de valoraciones"));
            }
        });

        return liveData;
    }
}
