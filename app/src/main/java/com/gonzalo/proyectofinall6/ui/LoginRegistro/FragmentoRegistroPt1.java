package com.gonzalo.proyectofinall6.ui.LoginRegistro;

import android.os.Bundle;
import android.text.TextUtils;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        String password = binding.etPassword.getText().toString();
        String passwordRepeat = binding.etPasswordRepeat.getText().toString();

        if (TextUtils.isEmpty(binding.etNombre.getText()) ||
            TextUtils.isEmpty(binding.etApellido.getText()) ||
            TextUtils.isEmpty(binding.etEmail.getText()) ||
            TextUtils.isEmpty(password) ||
            TextUtils.isEmpty(passwordRepeat)) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
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
                binding.etApellido.getText().toString().trim()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
