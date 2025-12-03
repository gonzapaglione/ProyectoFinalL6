package com.gonzalo.proyectofinall6.SeccionPrincipal;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonzalo.proyectofinall6.databinding.FragmentoRegistroPt1Binding;

public class FragmentoRegistroPt1 extends Fragment {

    private FragmentoRegistroPt1Binding binding;

    public FragmentoRegistroPt1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentoRegistroPt1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ahora puedes acceder a tus vistas de forma segura a través del objeto binding
        // Por ejemplo:
        // binding.tilNombre.setHint("Nuevo Hint para Nombre");
        // binding.btnSiguiente.setOnClickListener(v -> {
        //     // Lógica del botón siguiente
        // });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpia la referencia al binding para evitar fugas de memoria
        binding = null;
    }
}
