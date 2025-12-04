package com.gonzalo.proyectofinall6.Secciones.Inicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.api.ApiService;
import com.gonzalo.proyectofinall6.api.RetrofitClient;
import com.gonzalo.proyectofinall6.modelos.Paciente;
import com.gonzalo.proyectofinall6.modelos.PacienteResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoHome extends Fragment {

    private TextView tvGreeting;
    private ApiService apiService;

    public FragmentoHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = RetrofitClient.getApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_home, container, false);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPatientData();
    }

    private void loadPatientData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int patientIdInt = sharedPreferences.getInt("user_id", -1);

        if (patientIdInt != -1) {
            String patientId = String.valueOf(patientIdInt);
            apiService.getPaciente(patientId).enqueue(new Callback<PacienteResponse>() {
                @Override
                public void onResponse(Call<PacienteResponse> call, Response<PacienteResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        Paciente paciente = response.body().getData();
                        tvGreeting.setText("Hola, " + paciente.getNombre());
                    } else {
                        Toast.makeText(getContext(), "Error al obtener los datos del paciente", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PacienteResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
