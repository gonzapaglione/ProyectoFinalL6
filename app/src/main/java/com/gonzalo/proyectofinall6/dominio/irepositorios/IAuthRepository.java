package com.gonzalo.proyectofinall6.dominio.irepositorios;

import androidx.lifecycle.LiveData;

import com.gonzalo.proyectofinall6.data.remote.dto.RegistroRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroResponse;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

public interface IAuthRepository {
    LiveData<RepositoryResult<RegistroResponse>> registrarPaciente(RegistroRequest request);
}
