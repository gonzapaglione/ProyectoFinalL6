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

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.databinding.FragmentoResumenTurnoBinding;
import com.gonzalo.proyectofinall6.ui.viewmodels.ReservarViewModel;

public class FragmentoResumenTurno extends Fragment {

    private FragmentoResumenTurnoBinding binding;
    private ReservarViewModel reservarViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentoResumenTurnoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservarViewModel = new ViewModelProvider(requireActivity()).get(ReservarViewModel.class);

        setupButtons();
        observeViewModel();
    }

    private void observeViewModel() {
        reservarViewModel.getFechaHoraFormateada().observe(getViewLifecycleOwner(), texto -> {
            if (texto != null) {
                binding.tvFechaHora.setText(texto);
            }
        });

        reservarViewModel.getOdontologoNombre().observe(getViewLifecycleOwner(), nombre -> {
            binding.tvProfesional.setText(nombre);
        });

        reservarViewModel.getMotivoConsultaNombre().observe(getViewLifecycleOwner(), motivo -> {
            binding.tvMotivo.setText(motivo);
        });

        reservarViewModel.getObraSocialNombre().observe(getViewLifecycleOwner(), obraSocial -> {
            binding.tvCobertura.setText(obraSocial);
        });

        reservarViewModel.getTurnoCreado().observe(getViewLifecycleOwner(), turnoResponse -> {
            if (turnoResponse != null && turnoResponse.isSuccess()) {
                Toast.makeText(getContext(), "Turno creado exitosamente", Toast.LENGTH_SHORT).show();
                // Navigate to a success screen
                NavHostFragment.findNavController(FragmentoResumenTurno.this)
                        .navigate(R.id.action_fragmentoResumenTurno_to_turnoRegistrado);
            } else {
                Toast.makeText(getContext(), "Error al crear el turno", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {
        binding.btnConfirmar.setOnClickListener(v -> {
            reservarViewModel.crearTurno();
        });

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}
