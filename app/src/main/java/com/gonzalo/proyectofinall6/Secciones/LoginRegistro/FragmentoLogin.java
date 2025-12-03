package com.gonzalo.proyectofinall6.Secciones.LoginRegistro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.Secciones.Inicio.HomeActivity;
import com.gonzalo.proyectofinall6.databinding.FragmentoLoginBinding;
import com.gonzalo.proyectofinall6.models.LoginRequest;
import com.gonzalo.proyectofinall6.models.LoginResponse;
import com.gonzalo.proyectofinall6.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoLogin extends Fragment {

    private FragmentoLoginBinding binding;

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

        binding.tvRegistrarse.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new FragmentoRegistroPt1())
                    .addToBackStack(null)
                    .commit();
        });

        binding.btnIngresar.setOnClickListener(v -> {
            realizarLogin();
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

        LoginRequest loginRequest = new LoginRequest(email, password);

        RetrofitClient.getApiService().login(loginRequest)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        binding.btnIngresar.setEnabled(true);
                        binding.btnIngresar.setText("Ingresar");

                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();

                            if (loginResponse.isSuccess()) {
                                // Login exitoso
                                Toast.makeText(getContext(),
                                        "Bienvenido " + loginResponse.getData().getNombre(),
                                        Toast.LENGTH_SHORT).show();

                                // Guardar datos del usuario y estado de la sesión
                                boolean rememberMe = binding.cbRecordar.isChecked();
                                saveUserSession(loginResponse.getData(), rememberMe);

                                // Navegar a la pantalla principal
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish(); // Cierra la actividad de Login

                            } else {
                                Toast.makeText(getContext(),
                                        loginResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(),
                                    "Error: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        binding.btnIngresar.setEnabled(true);
                        binding.btnIngresar.setText("Ingresar");

                        Toast.makeText(getContext(),
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserSession(LoginResponse.UserData userData, boolean rememberMe) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("is_logged_in", rememberMe);
        editor.putInt("user_id", userData.getUserId());
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
