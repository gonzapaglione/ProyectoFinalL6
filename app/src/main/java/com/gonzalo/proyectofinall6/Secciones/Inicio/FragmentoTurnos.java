package com.gonzalo.proyectofinall6.Secciones.Inicio;

import android.content.Context;
import android.content.Intent;
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
import com.gonzalo.proyectofinall6.Secciones.ReservaTurno.Reservacion;
import com.gonzalo.proyectofinall6.adaptadores.TurnosAdapter;
import com.gonzalo.proyectofinall6.api.RetrofitClient;
import com.gonzalo.proyectofinall6.api.ApiService;
import com.gonzalo.proyectofinall6.modelos.CancelarTurnoRequest;
import com.gonzalo.proyectofinall6.modelos.HistorialResponse;
import com.gonzalo.proyectofinall6.modelos.PacienteResponse;
import com.gonzalo.proyectofinall6.modelos.ProximosTurnosResponse;
import com.gonzalo.proyectofinall6.modelos.Turno;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoTurnos extends Fragment implements TurnosAdapter.OnTurnoActionListener {

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
        upcomingAdapter = new TurnosAdapter(upcomingTurnos, getContext(), this);
        rvUpcoming.setAdapter(upcomingAdapter);

        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new TurnosAdapter(historyTurnos, getContext(), this);
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
                upcomingTurnos.clear();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Turno> turnos = response.body().getData();
                    if (turnos != null && !turnos.isEmpty()) {
                        // Si hay turnos, se muestran en la lista
                        upcomingTurnos.addAll(turnos);
                        rvUpcoming.setVisibility(View.VISIBLE);
                        cardEmptyState.setVisibility(View.GONE);
                    } else {
                        // Si no hay turnos, se muestra el empty state
                        rvUpcoming.setVisibility(View.GONE);
                        cardEmptyState.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Si la llamada falla, también se muestra el empty state
                    rvUpcoming.setVisibility(View.GONE);
                    cardEmptyState.setVisibility(View.VISIBLE);
                }
                upcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProximosTurnosResponse> call, Throwable t) {
                upcomingTurnos.clear();
                upcomingAdapter.notifyDataSetChanged();
                rvUpcoming.setVisibility(View.GONE);
                cardEmptyState.setVisibility(View.VISIBLE);
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

    // Implementación de la interfaz del adaptador
    @Override
    public void onCancelarTurno(int turnoId, String motivo) {
        CancelarTurnoRequest request = new CancelarTurnoRequest(turnoId, motivo);
        apiService.cancelarTurno(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Turno cancelado exitosamente", Toast.LENGTH_SHORT).show();
                    loadPacienteIdAndFetchTurnos(); // Recargamos los datos
                } else {
                    Toast.makeText(getContext(), "Error al cancelar el turno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVerDetalle(int turnoId) {
        Toast.makeText(getContext(), "Viendo detalle del turno...", Toast.LENGTH_SHORT).show();
        // TODO: Aquí debes implementar la navegación a la pantalla de detalle
        // del turno, pasando el turnoId como argumento.
    }
}
