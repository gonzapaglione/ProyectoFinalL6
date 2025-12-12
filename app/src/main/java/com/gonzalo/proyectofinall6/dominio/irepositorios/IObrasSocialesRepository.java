package com.gonzalo.proyectofinall6.dominio.irepositorios;

import androidx.lifecycle.LiveData;

import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.List;

public interface IObrasSocialesRepository {
    LiveData<RepositoryResult<List<ObraSocial>>> getObrasSociales();
}
