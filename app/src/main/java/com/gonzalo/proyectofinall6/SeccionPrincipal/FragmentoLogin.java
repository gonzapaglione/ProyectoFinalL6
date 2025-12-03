package com.gonzalo.proyectofinall6.SeccionPrincipal;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.databinding.FragmentoLoginBinding;

public class FragmentoLogin extends Fragment {

    private FragmentoLoginBinding binding;

    public FragmentoLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentoLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvRegistrarse.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new FragmentoRegistroPt1())
                .addToBackStack(null) // Permite volver al fragmento de login
                .commit();
        });

        // También puedes añadir la lógica para el botón de ingreso aquí
        // binding.btnIngresar.setOnClickListener(v -> {
        //     // Lógica del botón de ingreso
        // });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpia la referencia al binding para evitar fugas de memoria
        binding = null;
    }
}
