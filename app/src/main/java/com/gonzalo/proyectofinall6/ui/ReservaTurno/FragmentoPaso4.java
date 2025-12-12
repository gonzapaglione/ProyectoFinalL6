package com.gonzalo.proyectofinall6.ui.ReservaTurno;

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
import com.gonzalo.proyectofinall6.databinding.FragmentPaso4Binding;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.ui.viewmodels.ReservarViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentoPaso4 extends Fragment {

    private FragmentPaso4Binding binding;
    private ReservarViewModel reservarViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaso4Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservarViewModel = new ViewModelProvider(requireActivity()).get(ReservarViewModel.class);

        setupButtons();

        reservarViewModel.getObrasSociales().observe(getViewLifecycleOwner(), obrasSociales -> {
            if (obrasSociales != null && !obrasSociales.isEmpty()) {
                setupAutoComplete(obrasSociales);
            } else {
                Toast.makeText(getContext(), "No se pudieron cargar las obras sociales", Toast.LENGTH_SHORT).show();
            }
        });

        reservarViewModel.fetchObrasSociales();
    }

    private void setupAutoComplete(List<ObraSocial> obrasSociales) {
        List<String> nombresObrasSociales = new ArrayList<>();
        for (ObraSocial obraSocial : obrasSociales) {
            nombresObrasSociales.add(obraSocial.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, nombresObrasSociales);
        binding.autoCompleteObrasSociales.setAdapter(adapter);

        binding.autoCompleteObrasSociales.setOnItemClickListener((parent, view, position, id) -> {
            String selectedObraSocialName = (String) parent.getItemAtPosition(position);
            for (ObraSocial obraSocial : obrasSociales) {
                if (obraSocial.getNombre().equals(selectedObraSocialName)) {
                    reservarViewModel.setObraSocialId(obraSocial.getIdObraSocial());
                    reservarViewModel.setObraSocialNombre(obraSocial.getNombre());
                    binding.btnNext.setEnabled(true);
                    break;
                }
            }
        });
    }

    private void setupButtons() {
        binding.btnNext.setEnabled(false);
        binding.btnNext.setOnClickListener(v -> {
            NavHostFragment.findNavController(FragmentoPaso4.this)
                    .navigate(R.id.action_fragmentoPaso4_to_fragmentoResumenTurno);
        });

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}
