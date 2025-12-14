package com.gonzalo.proyectofinall6.ui.Inicio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnoHoyItemDto;
import com.gonzalo.proyectofinall6.data.remote.dto.TurnosHoyDto;
import com.gonzalo.proyectofinall6.ui.ReservaTurno.Reservacion;
import com.gonzalo.proyectofinall6.ui.viewmodels.HomeViewModel;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;
import androidx.lifecycle.ViewModelProvider;

public class FragmentoHome extends Fragment {

    private TextView tvGreeting;
    private TextView tvTurnosHoyCount;
    private LinearLayout layoutTurnosHoyEmpty;
    private LinearLayout layoutTurnosHoyList;
    private TextView tvProgramadosLabel;
    private TextView tvRealizadosLabel;
    private TextView tvAusentesLabel;
    private TextView tvCanceladosLabel;
    private TextView tvTotalTurnos;
    private ProgressBar pbProgramados;
    private ProgressBar pbRealizados;
    private ProgressBar pbAusentes;
    private ProgressBar pbCancelados;
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
        tvTurnosHoyCount = view.findViewById(R.id.tvTurnosHoyCount);
        layoutTurnosHoyEmpty = view.findViewById(R.id.layoutTurnosHoyEmpty);
        layoutTurnosHoyList = view.findViewById(R.id.layoutTurnosHoyList);
        tvProgramadosLabel = view.findViewById(R.id.tvProgramadosLabel);
        tvRealizadosLabel = view.findViewById(R.id.tvRealizadosLabel);
        tvAusentesLabel = view.findViewById(R.id.tvAusentesLabel);
        tvCanceladosLabel = view.findViewById(R.id.tvCanceladosLabel);
        tvTotalTurnos = view.findViewById(R.id.tvTotalTurnos);
        pbProgramados = view.findViewById(R.id.pbProgramados);
        pbRealizados = view.findViewById(R.id.pbRealizados);
        pbAusentes = view.findViewById(R.id.pbAusentes);
        pbCancelados = view.findViewById(R.id.pbCancelados);
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

        homeViewModel.getTurnosHoy().observe(getViewLifecycleOwner(), turnosHoy -> {
            renderTurnosHoy(turnosHoy);
        });

        homeViewModel.getTurnosStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null) {
                long total = stats.getTotal();
                tvTotalTurnos.setText("Mi total de turnos: " + total);
                int max = total > 0 ? (total > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) total) : 1;

                int programados = (int) Math.min(stats.getProgramados(), (long) Integer.MAX_VALUE);
                int realizados = (int) Math.min(stats.getRealizados(), (long) Integer.MAX_VALUE);
                int ausentes = (int) Math.min(stats.getAusentes(), (long) Integer.MAX_VALUE);
                int cancelados = (int) Math.min(stats.getCancelados(), (long) Integer.MAX_VALUE);

                tvProgramadosLabel.setText("Programados: " + stats.getProgramados());
                tvRealizadosLabel.setText("Realizados: " + stats.getRealizados());
                tvAusentesLabel.setText("Ausentes: " + stats.getAusentes());
                tvCanceladosLabel.setText("Cancelados: " + stats.getCancelados());

                pbProgramados.setMax(max);
                pbRealizados.setMax(max);
                pbAusentes.setMax(max);
                pbCancelados.setMax(max);

                pbProgramados.setProgress(Math.min(programados, max));
                pbRealizados.setProgress(Math.min(realizados, max));
                pbAusentes.setProgress(Math.min(ausentes, max));
                pbCancelados.setProgress(Math.min(cancelados, max));
            }
        });

        homeViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        homeViewModel.loadPacienteActual();
        homeViewModel.loadDashboard();

        btnReservarTurno.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Reservacion.class);
            startActivity(intent);
        });
    }

    private void renderTurnosHoy(TurnosHoyDto data) {
        if (data == null) {
            return;
        }

        long cantidad = data.getCantidad();
        tvTurnosHoyCount.setText(String.valueOf(cantidad));

        layoutTurnosHoyList.removeAllViews();

        if (cantidad <= 0) {
            layoutTurnosHoyEmpty.setVisibility(View.VISIBLE);
            layoutTurnosHoyList.setVisibility(View.GONE);
            return;
        }

        layoutTurnosHoyEmpty.setVisibility(View.GONE);
        layoutTurnosHoyList.setVisibility(View.VISIBLE);

        if (data.getTurnos() == null) {
            return;
        }

        for (TurnoHoyItemDto t : data.getTurnos()) {
            if (t == null)
                continue;

            View item = LayoutInflater.from(getContext()).inflate(R.layout.item_turno_hoy, layoutTurnosHoyList, false);
            TextView tvTop = item.findViewById(R.id.tvTurnoHoyTop);
            TextView tvBottom = item.findViewById(R.id.tvTurnoHoyBottom);

            String hora = t.getHora() != null ? t.getHora() : "";
            String odontologo = t.getOdontologo() != null ? t.getOdontologo() : "";
            String motivo = t.getMotivo() != null ? t.getMotivo() : "";
            String estado = t.getEstado() != null ? t.getEstado() : "";

            tvTop.setText(hora + "  •  " + odontologo);

            String bottom = motivo;
            if (!estado.isEmpty()) {
                bottom = (bottom.isEmpty() ? "" : (bottom + " ")) + "(" + estado + ")";
            }
            tvBottom.setText(bottom.isEmpty() ? " " : bottom);

            layoutTurnosHoyList.addView(item);
        }
    }
}
