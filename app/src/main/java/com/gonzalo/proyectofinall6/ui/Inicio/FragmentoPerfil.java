package com.gonzalo.proyectofinall6.ui.Inicio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gonzalo.proyectofinall6.ui.viewmodels.PerfilViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.gonzalo.proyectofinall6.ui.MainActivity;
import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.data.remote.dto.EditarPacienteRequest;
import com.gonzalo.proyectofinall6.data.repositorios.NotificacionesRepository;
import com.gonzalo.proyectofinall6.data.repositorios.SessionRepository;
import com.gonzalo.proyectofinall6.dominio.modelos.ObraSocial;
import com.gonzalo.proyectofinall6.dominio.modelos.Paciente;

import java.util.ArrayList;
import java.util.List;

public class FragmentoPerfil extends Fragment {

    private EditText etNombre, etApellido, etDni, etTelefono, etDireccion, etEmail;
    private TextView tvUsername, tvEmailHeader, tvChangePassword;
    private MaterialButton btnEditarInformacion, btnLogout;
    private PerfilViewModel perfilViewModel;
    private boolean isEditing = false;

    private CardView cardObrasSociales;
    private AutoCompleteTextView actvCobertura;
    private ChipGroup chipGroupCoberturas;
    private TextInputLayout tilCobertura;
    private List<ObraSocial> todasLasObrasSociales;
    private final List<ObraSocial> obrasSocialesSeleccionadas = new ArrayList<>();
    private SessionRepository sessionRepository;

    public FragmentoPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        sessionRepository = new SessionRepository(requireContext());
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
        perfilViewModel.cargarCatalogoObrasSociales();

        btnEditarInformacion.setOnClickListener(v -> toggleEditState());
        btnLogout.setOnClickListener(v -> logout());
        tvChangePassword.setOnClickListener(v -> showChangePasswordDialog());
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView)
                .setTitle("Cambiar Contraseña")
                .setPositiveButton("Guardar", (dialog, id) -> {
                    EditText etPasswordActual = dialogView.findViewById(R.id.etPasswordActual);
                    EditText etPasswordNueva = dialogView.findViewById(R.id.etPasswordNueva);
                    EditText etRepetirPasswordNueva = dialogView.findViewById(R.id.etRepetirPasswordNueva);

                    String passwordActual = etPasswordActual.getText().toString();
                    String passwordNueva = etPasswordNueva.getText().toString();
                    String repetirPasswordNueva = etRepetirPasswordNueva.getText().toString();

                    if (passwordNueva.equals(repetirPasswordNueva)) {
                        perfilViewModel.cambiarPassword(passwordActual, passwordNueva);
                    } else {
                        Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
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
        cardObrasSociales = view.findViewById(R.id.cardObrasSociales);
        actvCobertura = view.findViewById(R.id.actvCobertura);
        chipGroupCoberturas = view.findViewById(R.id.chipGroupCoberturas);
        tilCobertura = view.findViewById(R.id.tilCobertura);
    }

    private void observeViewModel() {
        perfilViewModel.getPaciente().observe(getViewLifecycleOwner(), this::populateUI);
        perfilViewModel.getObrasSocialesCatalogo().observe(getViewLifecycleOwner(), obras -> {
            if (obras == null || obras.isEmpty()) {
                return;
            }
            todasLasObrasSociales = obras;
            ArrayAdapter<ObraSocial> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, todasLasObrasSociales);
            actvCobertura.setAdapter(adapter);
            actvCobertura.setOnItemClickListener((parent, view, position, id) -> {
                ObraSocial seleccionada = (ObraSocial) parent.getItemAtPosition(position);
                if (!obrasSocialesSeleccionadas.contains(seleccionada)) {
                    obrasSocialesSeleccionadas.add(seleccionada);
                    actualizarChips();
                }
                actvCobertura.setText("");
            });
        });
        perfilViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
        perfilViewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            Toast.makeText(getContext(), success, Toast.LENGTH_SHORT).show();
        });
    }

    private void populateUI(Paciente paciente) {
        etNombre.setText(paciente.getNombre());
        etApellido.setText(paciente.getApellido());
        etDni.setText(paciente.getDni());
        etTelefono.setText(paciente.getTelefono());
        etDireccion.setText(paciente.getDireccion());
        etEmail.setText(paciente.getEmail());

        if (tvUsername != null)
            tvUsername.setText(paciente.getNombre() + " " + paciente.getApellido());
        if (tvEmailHeader != null)
            tvEmailHeader.setText(paciente.getEmail());

        if (paciente.getObrasSociales() != null) {
            obrasSocialesSeleccionadas.clear();
            obrasSocialesSeleccionadas.addAll(paciente.getObrasSociales());
            actualizarChips();
        }
    }

    private void toggleEditState() {
        isEditing = !isEditing;
        enableEdit(isEditing);

        if (isEditing) {
            btnEditarInformacion.setText("Guardar Cambios");
            tilCobertura.setVisibility(View.VISIBLE);
            actualizarChips(); // Para mostrar el icono de cerrar
        } else {
            btnEditarInformacion.setText("Editar Información");
            btnEditarInformacion.setIconResource(android.R.drawable.ic_menu_edit);
            tilCobertura.setVisibility(View.GONE);
            savePatientData();
            actualizarChips(); // Para ocultar el icono de cerrar
        }
    }

    private void savePatientData() {
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String dni = etDni.getText().toString();
        String telefono = etTelefono.getText().toString();
        String direccion = etDireccion.getText().toString();
        String email = etEmail.getText().toString();

        EditarPacienteRequest request = new EditarPacienteRequest(dni, nombre, apellido, telefono, direccion, email,
                "paciente", obrasSocialesSeleccionadas);

        perfilViewModel.editarPaciente(request);
    }

    private void enableEdit(boolean enable) {
        etNombre.setEnabled(enable);
        etApellido.setEnabled(enable);
        etDni.setEnabled(enable);
        etTelefono.setEnabled(enable);
        etDireccion.setEnabled(enable);
    }

    private void actualizarChips() {
        chipGroupCoberturas.removeAllViews();
        for (ObraSocial obraSocial : obrasSocialesSeleccionadas) {
            agregarChip(obraSocial);
        }
    }

    private void agregarChip(ObraSocial obraSocial) {
        Chip chip = new Chip(getContext());
        chip.setText(obraSocial.getNombre());

        boolean esParticular = "PARTICULAR".equalsIgnoreCase(obraSocial.getNombre());
        chip.setCloseIconVisible(isEditing && !esParticular);

        if (isEditing && !esParticular) {
            chip.setOnCloseIconClickListener(v -> {
                obrasSocialesSeleccionadas.remove(obraSocial);
                actualizarChips();
            });
        }
        chipGroupCoberturas.addView(chip);
    }

    private void logout() {
        new AlertDialog.Builder(getContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotificacionesRepository notificacionesRepository = new NotificacionesRepository(
                                requireContext());
                        notificacionesRepository.desregistrarTokenFcm();
                        notificacionesRepository.clearLocalTokenCache();
                        sessionRepository.clearSession();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
