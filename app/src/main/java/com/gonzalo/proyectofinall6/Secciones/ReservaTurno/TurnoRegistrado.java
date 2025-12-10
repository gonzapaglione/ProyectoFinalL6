package com.gonzalo.proyectofinall6.Secciones.ReservaTurno;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.Secciones.Inicio.HomeActivity;
import com.gonzalo.proyectofinall6.Secciones.MainActivity;
import com.google.android.material.button.MaterialButton;

public class TurnoRegistrado extends Fragment {

    public TurnoRegistrado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_turno_registrado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnVolverInicio = view.findViewById(R.id.btnVolverInicio);
        btnVolverInicio.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }
}
