package com.gonzalo.proyectofinall6.Secciones.Inicio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.gonzalo.proyectofinall6.Secciones.MainActivity;
import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.modelos.Paciente;

public class FragmentoPerfil extends Fragment {

    private EditText etNombre, etApellido, etDni, etTelefono, etDireccion, etEmail;
    private TextView tvUsername, tvEmailHeader, tvChangePassword;
    private MaterialButton btnEditarInformacion, btnLogout;
    private PerfilViewModel perfilViewModel;
    private boolean isEditing = false;

    public FragmentoPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        observeViewModel();

        perfilViewModel.loadPacienteData();

        btnEditarInformacion.setOnClickListener(v -> toggleEditState());
        btnLogout.setOnClickListener(v -> logout());

    }

    private void bindViews(View view) {
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etDni = view.findViewById(R.id.etDni);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        etEmail = view.findViewById(R.id.etEmail);
        tvUsername = view.findViewById(R.id.tvUsernameHeader);
        tvEmailHeader = view.findViewById(R.id.tvEmailHeader);
        btnEditarInformacion = view.findViewById(R.id.btnEditarInformacion);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
    }

    private void observeViewModel() {
        perfilViewModel.getPaciente().observe(getViewLifecycleOwner(), this::populateUI);
        perfilViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    private void populateUI(Paciente paciente) {
        etNombre.setText(paciente.getNombre());
        etApellido.setText(paciente.getApellido());
        etDni.setText(paciente.getDni());
        etTelefono.setText(paciente.getTelefono());
        etDireccion.setText(paciente.getDireccion());
        etEmail.setText(paciente.getEmail());

        if (tvUsername != null) tvUsername.setText(paciente.getNombre() + " " + paciente.getApellido());
        if (tvEmailHeader != null) tvEmailHeader.setText(paciente.getEmail());

    }

    private void toggleEditState() {
        isEditing = !isEditing;
        enableEdit(isEditing);

        if (isEditing) {
            btnEditarInformacion.setText("Guardar Cambios");
            //btnEditarInformacion.setIconResource(R.drawable.ic_save); // Add ic_save to drawables
        } else {
            btnEditarInformacion.setText("Editar Información");
            btnEditarInformacion.setIconResource(android.R.drawable.ic_menu_edit);
            // TODO: Add logic to save data to the backend
            Toast.makeText(getContext(), "Cambios guardados (simulado)", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableEdit(boolean enable) {
        etNombre.setEnabled(enable);
        etApellido.setEnabled(enable);
        etDni.setEnabled(enable);
        etTelefono.setEnabled(enable);
        etDireccion.setEnabled(enable);
        // etEmail.setEnabled(enable); // Usually, email is not editable
    }

    private void logout() {
        new AlertDialog.Builder(getContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Limpiar SharedPreferences
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        // Redirigir al usuario a la pantalla de login
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
