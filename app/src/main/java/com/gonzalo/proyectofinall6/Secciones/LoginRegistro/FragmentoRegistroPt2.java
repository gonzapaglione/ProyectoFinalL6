package com.gonzalo.proyectofinall6.Secciones.LoginRegistro;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonzalo.proyectofinall6.databinding.FragmentoRegistroPt2Binding;

public class FragmentoRegistroPt2 extends Fragment {

    private FragmentoRegistroPt2Binding binding;

    public FragmentoRegistroPt2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentoRegistroPt2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ahora puedes acceder a tus vistas de forma segura a través del objeto binding
        // Por ejemplo:
        // binding.tilDni.setHint("Nuevo Hint para DNI");
        // binding.btnRegistrar.setOnClickListener(v -> {
        //     // Lógica del botón de registro
        // });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpia la referencia al binding para evitar fugas de memoria
        binding = null;
    }
}
