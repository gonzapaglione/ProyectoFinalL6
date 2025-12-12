package com.gonzalo.proyectofinall6.dominio.irepositorios;

import androidx.lifecycle.LiveData;

import com.gonzalo.proyectofinall6.data.remote.dto.EditarPacienteRequest;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.List;

public interface IPacienteRepository {
    LiveData<RepositoryResult<Paciente>> getPacienteActual();

    LiveData<RepositoryResult<List<ObraSocial>>> getObrasSocialesDelPacienteActual();

    LiveData<RepositoryResult<Paciente>> editarPaciente(EditarPacienteRequest request);

    LiveData<RepositoryResult<Void>> cambiarPassword(String passwordActual, String passwordNueva);
}
