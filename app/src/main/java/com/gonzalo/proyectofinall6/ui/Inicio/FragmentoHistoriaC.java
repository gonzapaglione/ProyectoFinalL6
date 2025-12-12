package com.gonzalo.proyectofinall6.ui.Inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.ui.ReservaTurno.Reservacion;
import com.gonzalo.proyectofinall6.ui.adaptadores.TurnosAdapter;
import com.gonzalo.proyectofinall6.ui.viewmodels.TurnosViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentoHistoriaC extends Fragment implements TurnosAdapter.OnTurnoActionListener {

    private RecyclerView rvUpcoming, rvHistory;
    private TurnosAdapter upcomingAdapter, historyAdapter;
    private MaterialCardView cardEmptyState;
    private Button btnReservarTurno;
    private TurnosViewModel turnosViewModel;

    public FragmentoHistoriaC() {
        // Required empty public constructor
    }

    public static FragmentoHistoriaC newInstance() {
        return new FragmentoHistoriaC();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        turnosViewModel = new ViewModelProvider(this).get(TurnosViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_historiac, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvUpcoming = view.findViewById(R.id.rvUpcoming);
        rvHistory = view.findViewById(R.id.rvHistory);
        cardEmptyState = view.findViewById(R.id.cardEmptyState);
        btnReservarTurno = view.findViewById(R.id.btnReservarTurno);

        setupRecyclerViews();
        observeViewModel();

        turnosViewModel.loadTurnos();

        btnReservarTurno.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Reservacion.class);
            startActivity(intent);
        });

        FloatingActionButton fabReservarTurno = view.findViewById(R.id.fabReservarTurno);
        fabReservarTurno.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Reservacion.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerViews() {
        rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingAdapter = new TurnosAdapter(new ArrayList<>(), getContext(), this);
        rvUpcoming.setAdapter(upcomingAdapter);

        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new TurnosAdapter(new ArrayList<>(), getContext(), this);
        rvHistory.setAdapter(historyAdapter);
    }

    private void observeViewModel() {
        turnosViewModel.getProximosTurnos().observe(getViewLifecycleOwner(), turnos -> {
            if (turnos != null && !turnos.isEmpty()) {
                upcomingAdapter.updateData(turnos);
                rvUpcoming.setVisibility(View.VISIBLE);
                cardEmptyState.setVisibility(View.GONE);
            } else {
                rvUpcoming.setVisibility(View.GONE);
                cardEmptyState.setVisibility(View.VISIBLE);
            }
        });

        turnosViewModel.getHistorialTurnos().observe(getViewLifecycleOwner(), turnos -> {
            if (turnos != null && !turnos.isEmpty()) {
                historyAdapter.updateData(turnos);
                rvHistory.setVisibility(View.VISIBLE);
            } else {
                rvHistory.setVisibility(View.GONE);
            }
        });

        turnosViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            showEmptyState();
        });

        turnosViewModel.getTurnoCancelado().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Turno cancelado exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error al cancelar el turno", Toast.LENGTH_SHORT).show();
            }
        });

        turnosViewModel.getValoracionGuardada().observe(getViewLifecycleOwner(), success -> {
            if (success == null)
                return;
            if (success) {
                Toast.makeText(getContext(), "¡Gracias por valorar tu turno!", Toast.LENGTH_SHORT).show();
            }
        });

        turnosViewModel.getValoracionError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyState() {
        cardEmptyState.setVisibility(View.VISIBLE);
        rvUpcoming.setVisibility(View.GONE);
        rvHistory.setVisibility(View.GONE);
    }

    // Implementación de la interfaz del adaptador
    @Override
    public void onCancelarTurno(int turnoId, String motivo) {
        turnosViewModel.cancelarTurno(turnoId, motivo);
    }

    @Override
    public void onVerDetalle(int turnoId) {
        Toast.makeText(getContext(), "Viendo detalle del turno...", Toast.LENGTH_SHORT).show();
        // TODO: Aquí debes implementar la navegación a la pantalla de detalle
        // del turno, pasando el turnoId como argumento.
    }

    @Override
    public void onValorarTurno(int turnoId, int estrellas, String comentario) {
        turnosViewModel.valorarTurno(turnoId, estrellas, comentario);
    }
}
