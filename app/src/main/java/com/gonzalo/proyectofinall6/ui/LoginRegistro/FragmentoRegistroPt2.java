package com.gonzalo.proyectofinall6.ui.LoginRegistro;

import android.content.Context;
import android.content.Intent;
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
import com.gonzalo.proyectofinall6.databinding.FragmentoRegistroPt2Binding;
import com.gonzalo.proyectofinall6.data.repositorios.SessionRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;

import com.gonzalo.proyectofinall6.dominio.modelos.RepositoryResult;

import com.gonzalo.proyectofinall6.data.remote.dto.RegistroResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentoRegistroPt2 extends Fragment {

    private FragmentoRegistroPt2Binding binding;
    private List<ObraSocial> todasLasObrasSociales;
    private List<ObraSocial> obrasSocialesSeleccionadas = new ArrayList<>();
    private RegistroViewModel registroViewModel;
    private boolean particularAgregado = false;
    private SessionRepository sessionRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registroViewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);
        sessionRepository = new SessionRepository(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentoRegistroPt2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observarObrasSociales();
        observarRegistro();
        registroViewModel.cargarObrasSociales();

        binding.btnRegistrar.setOnClickListener(v -> registrarPaciente());
    }

    private void observarObrasSociales() {
        registroViewModel.getObrasSocialesDisponibles().observe(getViewLifecycleOwner(), obras -> {
            if (obras == null || obras.isEmpty()) {
                Toast.makeText(getContext(), "Error al cargar las obras sociales", Toast.LENGTH_SHORT).show();
                return;
            }

            todasLasObrasSociales = obras;

            if (!particularAgregado) {
                for (ObraSocial os : todasLasObrasSociales) {
                    if (os != null && os.getNombre() != null && os.getNombre().equalsIgnoreCase("PARTICULAR")) {
                        if (!obrasSocialesSeleccionadas.contains(os)) {
                            obrasSocialesSeleccionadas.add(os);
                            agregarChip(os, false);
                        }
                        particularAgregado = true;
                        break;
                    }
                }
            }

            ArrayAdapter<ObraSocial> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, todasLasObrasSociales);
            binding.actvCobertura.setAdapter(adapter);
            binding.actvCobertura.setOnItemClickListener((parent, view, position, id) -> {
                ObraSocial seleccionada = (ObraSocial) parent.getItemAtPosition(position);
                if (!obrasSocialesSeleccionadas.contains(seleccionada)) {
                    obrasSocialesSeleccionadas.add(seleccionada);
                    agregarChip(seleccionada, true);
                }
                binding.actvCobertura.setText("");
            });
        });
    }

    private void observarRegistro() {
        registroViewModel.getRegistroPacienteResult().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            if (result.isSuccess() && result.getData() != null) {
                RegistroResponse response = result.getData();
                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

                sessionRepository.saveSession(response.getData().getIdPaciente(), true);

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            } else {
                String error = result.getError() != null ? result.getError() : "Error en el registro";
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }

            registroViewModel.resetRegistroPacienteResult();
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

        binding.etDni.setError(null);
        binding.etTelefono.setError(null);
        binding.etDireccion.setError(null);

        if (dni.isEmpty()) {
            binding.etDni.setError("Campo obligatorio");
            binding.etDni.requestFocus();
            return;
        }
        if (!dni.matches("^\\d{7,9}$")) {
            binding.etDni.setError("DNI inválido (7 a 9 dígitos)");
            binding.etDni.requestFocus();
            return;
        }

        if (telefono.isEmpty()) {
            binding.etTelefono.setError("Campo obligatorio");
            binding.etTelefono.requestFocus();
            return;
        }
        String telefonoNormalizado = telefono.replaceAll("[\\s\\-()]", "");
        if (!telefonoNormalizado.matches("^\\+?\\d{8,15}$")) {
            binding.etTelefono.setError("Teléfono inválido");
            binding.etTelefono.requestFocus();
            return;
        }

        if (direccion.isEmpty()) {
            binding.etDireccion.setError("Campo obligatorio");
            binding.etDireccion.requestFocus();
            return;
        }
        if (direccion.length() < 5) {
            binding.etDireccion.setError("Dirección muy corta");
            binding.etDireccion.requestFocus();
            return;
        }

        if (obrasSocialesSeleccionadas == null || obrasSocialesSeleccionadas.isEmpty()) {
            Toast.makeText(getContext(), "Selecciona al menos una cobertura", Toast.LENGTH_SHORT).show();
            return;
        }

        registroViewModel.registrarPaciente(dni, telefono, direccion, obrasSocialesSeleccionadas);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
