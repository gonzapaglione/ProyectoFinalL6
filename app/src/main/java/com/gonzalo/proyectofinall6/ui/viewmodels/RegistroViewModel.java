package com.gonzalo.proyectofinall6.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.gonzalo.proyectofinall6.data.remote.dto.RegistroRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroResponse;
import com.gonzalo.proyectofinall6.data.repositorios.AuthRepository;
import com.gonzalo.proyectofinall6.data.repositorios.ObrasSocialesRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IAuthRepository;
import com.gonzalo.proyectofinall6.dominio.irepositorios.IObrasSocialesRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import java.util.ArrayList;
import java.util.List;

public class RegistroViewModel extends ViewModel {

    public static class RegistroData {
        private final String email;
        private final String password;
        private final String nombre;
        private final String apellido;

        public RegistroData(String email, String password, String nombre, String apellido) {
            this.email = email;
            this.password = password;
            this.nombre = nombre;
            this.apellido = apellido;
        }

        // Getters
        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }
    }

    private final IAuthRepository authRepository = new AuthRepository();
    private final IObrasSocialesRepository obrasSocialesRepository = new ObrasSocialesRepository();

    private final MutableLiveData<RegistroData> registroData = new MutableLiveData<>();
    private final MutableLiveData<List<ObraSocial>> obrasSocialesDisponibles = new MutableLiveData<>();
    private final MutableLiveData<RepositoryResult<RegistroResponse>> registroPacienteResult = new MutableLiveData<>();

    public void setRegistroData(String email, String password, String nombre, String apellido) {
        registroData.setValue(new RegistroData(email, password, nombre, apellido));
    }

    public LiveData<RegistroData> getRegistroData() {
        return registroData;
    }

    public LiveData<List<ObraSocial>> getObrasSocialesDisponibles() {
        return obrasSocialesDisponibles;
    }

    public LiveData<RepositoryResult<RegistroResponse>> getRegistroPacienteResult() {
        return registroPacienteResult;
    }

    public void resetRegistroPacienteResult() {
        registroPacienteResult.setValue(null);
    }

    public void cargarObrasSociales() {
        LiveData<RepositoryResult<List<ObraSocial>>> source = obrasSocialesRepository.getObrasSociales();
        Observer<RepositoryResult<List<ObraSocial>>> observer = new Observer<RepositoryResult<List<ObraSocial>>>() {
            @Override
            public void onChanged(RepositoryResult<List<ObraSocial>> result) {
                if (result != null && result.isSuccess()) {
                    obrasSocialesDisponibles.setValue(result.getData());
                } else {
                    obrasSocialesDisponibles.setValue(new ArrayList<>());
                }
                source.removeObserver(this);
            }
        };
        source.observeForever(observer);
    }

    public void registrarPaciente(String dni, String telefono, String direccion, List<ObraSocial> obrasSeleccionadas) {
        RegistroData data = registroData.getValue();
        if (data == null) {
            registroPacienteResult.setValue(RepositoryResult.error("Faltan datos del paso 1"));
            return;
        }

        List<ObraSocial> obrasParaEnviar = new ArrayList<>();
        if (obrasSeleccionadas != null) {
            for (ObraSocial os : obrasSeleccionadas) {
                if (os != null) {
                    obrasParaEnviar.add(new ObraSocial(os.getIdObraSocial()));
                }
            }
        }

        RegistroRequest request = new RegistroRequest(
                data.getEmail(),
                data.getPassword(),
                dni,
                data.getNombre(),
                data.getApellido(),
                telefono,
                direccion,
                obrasParaEnviar);

        LiveData<RepositoryResult<RegistroResponse>> source = authRepository.registrarPaciente(request);
        Observer<RepositoryResult<RegistroResponse>> observer = new Observer<RepositoryResult<RegistroResponse>>() {
            @Override
            public void onChanged(RepositoryResult<RegistroResponse> result) {
                registroPacienteResult.setValue(result);
                source.removeObserver(this);
            }
        };
        source.observeForever(observer);
    }
}
