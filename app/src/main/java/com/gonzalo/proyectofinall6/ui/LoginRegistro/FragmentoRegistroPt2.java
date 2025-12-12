package com.gonzalo.proyectofinall6.ui.LoginRegistro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gonzalo.proyectofinall6.ui.viewmodels.RegistroViewModel;
import com.google.android.material.chip.Chip;
import com.gonzalo.proyectofinall6.ui.Inicio.HomeActivity;
import com.gonzalo.proyectofinall6.data.remote.api.ApiService;
import com.gonzalo.proyectofinall6.data.remote.api.RetrofitClient;
import com.gonzalo.proyectofinall6.databinding.FragmentoRegistroPt2Binding;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.data.remote.dto.ObrasSocialesResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroRequest;
import com.gonzalo.proyectofinall6.data.remote.dto.RegistroResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoRegistroPt2 extends Fragment {

    private FragmentoRegistroPt2Binding binding;
    private ApiService apiService;
    private List<ObraSocial> todasLasObrasSociales;
    private List<ObraSocial> obrasSocialesSeleccionadas = new ArrayList<>();
    private RegistroViewModel registroViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = RetrofitClient.getApiService();
        registroViewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentoRegistroPt2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarObrasSociales();

        binding.btnRegistrar.setOnClickListener(v -> registrarPaciente());
    }

    private void cargarObrasSociales() {
        apiService.getObrasSociales().enqueue(new Callback<ObrasSocialesResponse>() {
            @Override
            public void onResponse(Call<ObrasSocialesResponse> call, Response<ObrasSocialesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    todasLasObrasSociales = response.body().getData();

                    for (ObraSocial os : todasLasObrasSociales) {
                        if (os.getNombre().equalsIgnoreCase("PARTICULAR")) {
                            if (!obrasSocialesSeleccionadas.contains(os)) {
                                obrasSocialesSeleccionadas.add(os);
                                agregarChip(os, false);
                            }
                            break;
                        }
                    }

                    ArrayAdapter<ObraSocial> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, todasLasObrasSociales);
                    binding.actvCobertura.setAdapter(adapter);
                    binding.actvCobertura.setOnItemClickListener((parent, view, position, id) -> {
                        ObraSocial seleccionada = (ObraSocial) parent.getItemAtPosition(position);
                        if (!obrasSocialesSeleccionadas.contains(seleccionada)) {
                            obrasSocialesSeleccionadas.add(seleccionada);
                            agregarChip(seleccionada, true);
                        }
                        binding.actvCobertura.setText("");
                    });
                } else {
                    Toast.makeText(getContext(), "Error al cargar las obras sociales", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObrasSocialesResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarChip(ObraSocial obraSocial, boolean esCerrable) {
        Chip chip = new Chip(getContext());
        chip.setText(obraSocial.getNombre());
        chip.setCloseIconVisible(esCerrable);
        if (esCerrable) {
            chip.setOnCloseIconClickListener(v -> {
                obrasSocialesSeleccionadas.remove(obraSocial);
                binding.chipGroupCoberturas.removeView(chip);
            });
        }
        binding.chipGroupCoberturas.addView(chip);
    }

    private void registrarPaciente() {
        String dni = binding.etDni.getText().toString().trim();
        String telefono = binding.etTelefono.getText().toString().trim();
        String direccion = binding.etDireccion.getText().toString().trim();

        if (dni.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        registroViewModel.getRegistroData().observe(getViewLifecycleOwner(), registroData -> {
            List<ObraSocial> obrasParaEnviar = obrasSocialesSeleccionadas.stream()
                    .map(os -> new ObraSocial(os.getIdObraSocial()))
                    .collect(Collectors.toList());

            RegistroRequest request = new RegistroRequest(
                    registroData.getEmail(),
                    registroData.getPassword(),
                    dni,
                    registroData.getNombre(),
                    registroData.getApellido(),
                    telefono,
                    direccion,
                    obrasParaEnviar
            );

            apiService.registrarPaciente(request).enqueue(new Callback<RegistroResponse>() {
                @Override
                public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                        // Guardar sesión y navegar a Home
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("user_id", response.body().getData().getIdPaciente());
                        editor.putBoolean("is_logged_in", true);
                        editor.apply();

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();

                    } else {
                        Toast.makeText(getContext(), "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegistroResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
