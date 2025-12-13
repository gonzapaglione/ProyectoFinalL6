package com.gonzalo.proyectofinall6.dominio.irepositorios;

import androidx.lifecycle.LiveData;

import com.gonzalo.proyectofinall6.data.remote.dto.ApiResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HistoriaClinicaResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.HorarioDisponible;
import com.gonzalo.proyectofinall6.data.remote.dto.MotivoConsulta;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PromedioValoracionResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoResponse;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;
import com.gonzalo.proyectofinall6.dominio.modelos.Turno;

import java.util.List;

public interface ITurnosRepository {

    final class TurnosResumen {
        private final List<Turno> proximos;
        private final List<Turno> historial;

        public TurnosResumen(List<Turno> proximos, List<Turno> historial) {
            this.proximos = proximos;
            this.historial = historial;
        }

        public List<Turno> getProximos() {
            return proximos;
        }

        public List<Turno> getHistorial() {
            return historial;
        }
    }

    LiveData<RepositoryResult<List<OdontologoResponse>>> getOdontologos();

    LiveData<RepositoryResult<List<MotivoConsulta>>> getMotivosConsulta();

    LiveData<RepositoryResult<List<HorarioDisponible>>> getHorariosDisponibles(int idOdontologo, String fecha);

    LiveData<RepositoryResult<ApiResponse<TurnoResponse>>> crearTurno(int idOdontologo, String fecha, String horaInicio,
            int idMotivoConsulta, int idObraSocial);

    LiveData<RepositoryResult<TurnosResumen>> getTurnosDelPacienteActual();

    LiveData<RepositoryResult<Void>> cancelarTurno(int turnoId, String motivo);

    LiveData<RepositoryResult<Void>> crearValoracion(int turnoId, int estrellas, String comentario);

    LiveData<RepositoryResult<PromedioValoracionResponse>> getPromedioValoracionOdontologo(int idOdontologo);

    LiveData<RepositoryResult<HistoriaClinicaResponse>> getHistoriaClinicaPorTurno(int idTurno);
}
