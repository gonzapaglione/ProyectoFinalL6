package com.gonzalo.proyectofinall6.ui.Inicio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.ui.ReservaTurno.Reservacion;
import com.gonzalo.proyectofinall6.ui.viewmodels.HomeViewModel;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import androidx.lifecycle.ViewModelProvider;

public class FragmentoHome extends Fragment {

    private TextView tvGreeting;
    private HomeViewModel homeViewModel;

    private Button btnReservarTurno;

    public FragmentoHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_home, container, false);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        btnReservarTurno = view.findViewById(R.id.btnReservarTurno);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getPaciente().observe(getViewLifecycleOwner(), paciente -> {
            if (paciente != null) {
                tvGreeting.setText("Hola, " + paciente.getNombre());
            }
        });

        homeViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        homeViewModel.loadPacienteActual();

        btnReservarTurno.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Reservacion.class);
            startActivity(intent);
        });
    }
}
