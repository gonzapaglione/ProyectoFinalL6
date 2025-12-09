package com.gonzalo.proyectofinall6.Secciones.ReservaTurno;

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
import androidx.recyclerview.widget.GridLayoutManager;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.databinding.FragmentPaso2Binding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FragmentoPaso2 extends Fragment {

    private FragmentPaso2Binding binding;
    private ReservarViewModel reservarViewModel;
    private HorarioAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaso2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservarViewModel = new ViewModelProvider(requireActivity()).get(ReservarViewModel.class);

        setupCalendar();
        setupRecyclerView();
        setupButtons();

        reservarViewModel.getHorariosDisponibles().observe(getViewLifecycleOwner(), horarios -> {
            if (horarios != null && !horarios.isEmpty()) {
                adapter.updateData(horarios);
            } else {
                adapter.updateData(new ArrayList<>());
                Toast.makeText(getContext(), "No hay horarios disponibles para la fecha seleccionada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCalendar() {
        // Disable past dates
        binding.calendarView.setMinDate(System.currentTimeMillis());

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            
            // Use Spanish locale for formatting dates
            Locale spanishLocale = new Locale("es", "ES");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", spanishLocale);
            String selectedDate = sdf.format(calendar.getTime());
            reservarViewModel.setFecha(selectedDate);
            reservarViewModel.fetchHorariosDisponibles();

            SimpleDateFormat titleSdf = new SimpleDateFormat("dd 'de' MMMM", spanishLocale);
            String title = "Horarios para el " + titleSdf.format(calendar.getTime());
            binding.tvHorariosDisponibles.setText(title);
        });
    }

    private void setupRecyclerView() {
        binding.recyclerViewHorarios.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new HorarioAdapter(new ArrayList<>(), horario -> {
            reservarViewModel.setHoraInicio(horario.getHora());
            binding.btnNext.setEnabled(true);
        });
        binding.recyclerViewHorarios.setAdapter(adapter);
    }

    private void setupButtons() {
        binding.btnNext.setEnabled(false);
        binding.btnNext.setOnClickListener(v -> {
            NavHostFragment.findNavController(FragmentoPaso2.this)
                    .navigate(R.id.action_fragmentoPaso2_to_fragmentoPaso3);
        });

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}
