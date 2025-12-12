package com.gonzalo.proyectofinall6.ui.ReservaTurno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.ui.adaptadores.OdontologoAdapter;
import com.gonzalo.proyectofinall6.databinding.FragmentPaso1Binding;
import com.gonzalo.proyectofinall6.ui.viewmodels.ReservarViewModel;

import java.util.ArrayList;

public class FragmentoPaso1 extends Fragment {

    private FragmentPaso1Binding binding;
    private ReservarViewModel reservarViewModel;
    private OdontologoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaso1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservarViewModel = new ViewModelProvider(requireActivity()).get(ReservarViewModel.class);

        setupRecyclerView();
        setupButtons();

        reservarViewModel.getOdontologos().observe(getViewLifecycleOwner(), odontologos -> {
            if (odontologos != null && !odontologos.isEmpty()) {
                adapter.updateData(odontologos);
            } else {
                Toast.makeText(getContext(), "No se pudieron cargar los odontólogos", Toast.LENGTH_SHORT).show();
            }
        });

        reservarViewModel.fetchOdontologos();
    }

    private void setupRecyclerView() {
        binding.recyclerViewOdontologos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OdontologoAdapter(new ArrayList<>(), odontologo -> {
            reservarViewModel.setOdontologoId(odontologo.getId());
            reservarViewModel.setOdontologoNombre(odontologo.getNombre() + " " + odontologo.getApellido());
            binding.btnNext.setEnabled(true);
        });
        binding.recyclerViewOdontologos.setAdapter(adapter);
    }

    private void setupButtons() {
        binding.btnNext.setEnabled(false);
        binding.btnNext.setOnClickListener(v -> {
            if (reservarViewModel.getOdontologoId().getValue() != null) {
                NavHostFragment.findNavController(FragmentoPaso1.this)
                        .navigate(R.id.action_fragmentoPaso1_to_fragmentoPaso2);
            } else {
                Toast.makeText(getContext(), "Por favor, seleccione un odontólogo", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}
