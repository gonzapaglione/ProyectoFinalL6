package com.gonzalo.proyectofinall6.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
    }

    private final MutableLiveData<RegistroData> registroData = new MutableLiveData<>();

    public void setRegistroData(String email, String password, String nombre, String apellido) {
        registroData.setValue(new RegistroData(email, password, nombre, apellido));
    }

    public LiveData<RegistroData> getRegistroData() {
        return registroData;
    }
}
