package com.gonzalo.proyectofinall6.Secciones.ReservaTurno;

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
import androidx.navigation.fragment.NavHostFragment;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.databinding.FragmentPaso3Binding;
import com.gonzalo.proyectofinall6.dto.MotivoConsulta;

import java.util.ArrayList;
import java.util.List;

public class FragmentoPaso3 extends Fragment {

    private FragmentPaso3Binding binding;
    private ReservarViewModel reservarViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaso3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservarViewModel = new ViewModelProvider(requireActivity()).get(ReservarViewModel.class);

        setupButtons();

        reservarViewModel.getMotivosConsulta().observe(getViewLifecycleOwner(), motivos -> {
            if (motivos != null && !motivos.isEmpty()) {
                setupAutoComplete(motivos);
            } else {
                Toast.makeText(getContext(), "No se pudieron cargar los motivos de consulta", Toast.LENGTH_SHORT).show();
            }
        });

        reservarViewModel.fetchMotivosConsulta();
    }

    private void setupAutoComplete(List<MotivoConsulta> motivos) {
        List<String> nombresMotivos = new ArrayList<>();
        for (MotivoConsulta motivo : motivos) {
            nombresMotivos.add(motivo.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, nombresMotivos);
        binding.autoCompleteReason.setAdapter(adapter);

        binding.autoCompleteReason.setOnItemClickListener((parent, view, position, id) -> {
            String selectedReasonName = (String) parent.getItemAtPosition(position);
            for (MotivoConsulta motivo : motivos) {
                if (motivo.getNombre().equals(selectedReasonName)) {
                    reservarViewModel.setMotivoConsultaId(motivo.getId());
                    reservarViewModel.setMotivoConsultaNombre(motivo.getNombre());
                    binding.btnNext.setEnabled(true);
                    break;
                }
            }
        });
    }

    private void setupButtons() {
        binding.btnNext.setEnabled(false);
        binding.btnNext.setOnClickListener(v -> {
            NavHostFragment.findNavController(FragmentoPaso3.this)
                    .navigate(R.id.action_fragmentoPaso3_to_fragmentoPaso4);
        });

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}
