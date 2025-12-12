package com.gonzalo.proyectofinall6.ui.LoginRegistro;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.ui.Inicio.HomeActivity;
import com.gonzalo.proyectofinall6.databinding.FragmentoLoginBinding;
import com.gonzalo.proyectofinall6.ui.viewmodels.LoginViewModel;

public class FragmentoLogin extends Fragment {

    private FragmentoLoginBinding binding;
    private LoginViewModel loginViewModel;

    public FragmentoLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentoLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();

        binding.tvRegistrarse.setOnClickListener(v -> {
            try {
                // Usamos NavController para navegar a la siguiente pantalla
                Navigation.findNavController(v).navigate(R.id.action_fragmentoLogin_to_fragmentoRegistroPt1);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error de navegación: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnIngresar.setOnClickListener(v -> {
            realizarLogin();
        });
    }

    private void observeViewModel() {
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            binding.btnIngresar.setEnabled(true);
            binding.btnIngresar.setText("Ingresar");

            if (result.isSuccess() && result.getData() != null && result.getData().getData() != null) {
                Toast.makeText(getContext(),
                        "Bienvenido " + result.getData().getData().getNombre(),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                if (getActivity() != null)
                    getActivity().finish();
            } else {
                Toast.makeText(getContext(),
                        result.getError() != null ? result.getError() : "Error en el login",
                        Toast.LENGTH_SHORT).show();
            }

            loginViewModel.resetLoginResult();
        });
    }

    private void realizarLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            binding.etEmail.setError("El email es requerido");
            binding.etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.etPassword.setError("La contraseña es requerida");
            binding.etPassword.requestFocus();
            return;
        }

        binding.btnIngresar.setEnabled(false);
        binding.btnIngresar.setText("Ingresando...");

        boolean rememberMe = binding.cbRecordar.isChecked();
        loginViewModel.login(email, password, rememberMe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
