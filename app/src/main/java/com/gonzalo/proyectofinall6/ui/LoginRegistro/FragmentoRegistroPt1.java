package com.gonzalo.proyectofinall6.ui.LoginRegistro;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.databinding.FragmentoRegistroPt1Binding;
import com.gonzalo.proyectofinall6.ui.viewmodels.RegistroViewModel;

public class FragmentoRegistroPt1 extends Fragment {

    private FragmentoRegistroPt1Binding binding;
    private RegistroViewModel registroViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registroViewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentoRegistroPt1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSiguiente.setOnClickListener(v -> {
            if (validarCampos()) {
                guardarDatosEnViewModel();
                try {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_fragmentoRegistroPt1_to_fragmentoRegistroPt2);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error de navegación: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validarCampos() {
        String nombre = binding.etNombre.getText() != null ? binding.etNombre.getText().toString().trim() : "";
        String apellido = binding.etApellido.getText() != null ? binding.etApellido.getText().toString().trim() : "";
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";
        String passwordRepeat = binding.etPasswordRepeat.getText() != null
                ? binding.etPasswordRepeat.getText().toString()
                : "";

        // Limpia errores previos
        binding.etNombre.setError(null);
        binding.etApellido.setError(null);
        binding.etEmail.setError(null);
        binding.etPassword.setError(null);
        binding.etPasswordRepeat.setError(null);

        if (TextUtils.isEmpty(nombre)) {
            binding.etNombre.setError("Campo obligatorio");
            binding.etNombre.requestFocus();
            return false;
        }
        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,}$")) {
            binding.etNombre.setError("Ingresa un nombre válido");
            binding.etNombre.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(apellido)) {
            binding.etApellido.setError("Campo obligatorio");
            binding.etApellido.requestFocus();
            return false;
        }
        if (!apellido.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,}$")) {
            binding.etApellido.setError("Ingresa un apellido válido");
            binding.etApellido.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Campo obligatorio");
            binding.etEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Email inválido");
            binding.etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Campo obligatorio");
            binding.etPassword.requestFocus();
            return false;
        }
        if (password.contains(" ")) {
            binding.etPassword.setError("La contraseña no puede tener espacios");
            binding.etPassword.requestFocus();
            return false;
        }
        if (password.length() < 8) {
            binding.etPassword.setError("Mínimo 8 caracteres");
            binding.etPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(passwordRepeat)) {
            binding.etPasswordRepeat.setError("Campo obligatorio");
            binding.etPasswordRepeat.requestFocus();
            return false;
        }
        if (!password.equals(passwordRepeat)) {
            binding.etPasswordRepeat.setError("Las contraseñas no coinciden");
            binding.etPasswordRepeat.requestFocus();
            return false;
        }

        return true;
    }

    private void guardarDatosEnViewModel() {
        registroViewModel.setRegistroData(
                binding.etEmail.getText().toString().trim(),
                binding.etPassword.getText().toString().trim(),
                binding.etNombre.getText().toString().trim(),
                binding.etApellido.getText().toString().trim());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
