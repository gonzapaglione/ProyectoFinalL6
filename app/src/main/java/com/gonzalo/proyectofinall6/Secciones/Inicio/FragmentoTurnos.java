package com.gonzalo.proyectofinall6.Secciones.Inicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.adaptadores.TurnosAdapter;
import com.gonzalo.proyectofinall6.api.RetrofitClient;
import com.gonzalo.proyectofinall6.api.ApiService;
import com.gonzalo.proyectofinall6.modelos.HistorialResponse;
import com.gonzalo.proyectofinall6.modelos.PacienteResponse;
import com.gonzalo.proyectofinall6.modelos.ProximosTurnosResponse;
import com.gonzalo.proyectofinall6.modelos.Turno;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoTurnos extends Fragment {

    private RecyclerView rvUpcoming, rvHistory;
    private TurnosAdapter upcomingAdapter, historyAdapter;
    private List<Turno> upcomingTurnos = new ArrayList<>();
    private List<Turno> historyTurnos = new ArrayList<>();
    private ApiService apiService;
    private MaterialCardView cardEmptyState;
    private Button btnReservarTurno;
    private SharedPreferences sharedPreferences;

    public FragmentoTurnos() {
        // Required empty public constructor
    }

    public static FragmentoTurnos newInstance() {
        return new FragmentoTurnos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_turnos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvUpcoming = view.findViewById(R.id.rvUpcoming);
        rvHistory = view.findViewById(R.id.rvHistory);
        cardEmptyState = view.findViewById(R.id.cardEmptyState);
        btnReservarTurno = view.findViewById(R.id.btnReservarTurno);

        apiService = RetrofitClient.getApiService();
        sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        setupRecyclerViews();
        loadPacienteIdAndFetchTurnos();

        btnReservarTurno.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Funcionalidad no implementada", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupRecyclerViews() {
        rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingAdapter = new TurnosAdapter(upcomingTurnos, getContext());
        rvUpcoming.setAdapter(upcomingAdapter);

        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new TurnosAdapter(historyTurnos, getContext());
        rvHistory.setAdapter(historyAdapter);
    }

    private void loadPacienteIdAndFetchTurnos() {
        int userId = sharedPreferences.getInt("user_id", -1);
        if (userId != -1) {
            fetchPacienteId(String.valueOf(userId));
        } else {
            Toast.makeText(getContext(), "Error: No se pudo obtener el ID de usuario.", Toast.LENGTH_LONG).show();
            showEmptyState();
        }
    }

    private void fetchPacienteId(String userId) {
        apiService.getPaciente(userId).enqueue(new Callback<PacienteResponse>() {
            @Override
            public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    long pacienteId = response.body().getData().getIdPaciente();
                    fetchProximosTurnos(pacienteId);
                    fetchHistorialTurnos(pacienteId);
                } else {
                    Toast.makeText(getContext(), "Error al obtener los datos del paciente.", Toast.LENGTH_SHORT).show();
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<PacienteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red al obtener datos del paciente.", Toast.LENGTH_SHORT).show();
                showEmptyState();
            }
        });
    }

    private void fetchProximosTurnos(long pacienteId) {
        apiService.getProximos(pacienteId).enqueue(new Callback<ProximosTurnosResponse>() {
            @Override
            public void onResponse(Call<ProximosTurnosResponse> call, Response<ProximosTurnosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Turno> turnos = response.body().getData();
                    if (turnos != null && !turnos.isEmpty()) {
                        upcomingTurnos.clear();
                        upcomingTurnos.addAll(turnos);
                        upcomingAdapter.notifyDataSetChanged();
                        rvUpcoming.setVisibility(View.VISIBLE);
                        cardEmptyState.setVisibility(View.GONE);
                    } else {
                        showUpcomingEmptyState();
                    }
                } else {
                    showUpcomingEmptyState();
                }
            }

            @Override
            public void onFailure(Call<ProximosTurnosResponse> call, Throwable t) {
                showUpcomingEmptyState();
                Toast.makeText(getContext(), "Error de red al cargar próximos turnos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchHistorialTurnos(long pacienteId) {
        apiService.getHistorial(pacienteId).enqueue(new Callback<HistorialResponse>() {
            @Override
            public void onResponse(Call<HistorialResponse> call, Response<HistorialResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Turno> turnos = response.body().getData();
                    if (turnos != null && !turnos.isEmpty()) {
                        historyTurnos.clear();
                        historyTurnos.addAll(turnos);
                        historyAdapter.notifyDataSetChanged();
                        rvHistory.setVisibility(View.VISIBLE);
                    } else {
                        rvHistory.setVisibility(View.GONE);
                    }
                } else {
                     rvHistory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<HistorialResponse> call, Throwable t) {
                rvHistory.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de red al cargar historial", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyState() {
        cardEmptyState.setVisibility(View.VISIBLE);
        rvUpcoming.setVisibility(View.GONE);
        rvHistory.setVisibility(View.GONE);
    }

    private void showUpcomingEmptyState() {
        rvUpcoming.setVisibility(View.GONE);
        cardEmptyState.setVisibility(View.VISIBLE);
    }
}
