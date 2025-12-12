package com.gonzalo.proyectofinall6.ui.ReservaTurno;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.gonzalo.proyectofinall6.R;

public class Reservacion extends AppCompatActivity {

    private NavController navController;
    private TextView toolbarTitle;
    private TextView toolbarSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Desactivar el título por defecto y el botón de "atrás"
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarSubtitle = findViewById(R.id.toolbar_subtitle);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_reservacion);
        navController = navHostFragment.getNavController();

        // Listener para actualizar el título y subtítulo personalizados
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destinationId = destination.getId();
            if (destinationId == R.id.fragmentoPaso1) {
                toolbarTitle.setText("Paso 1 de 4");
                toolbarSubtitle.setText("Selecciona odontologo");
            } else if (destinationId == R.id.fragmentoPaso2) {
                toolbarTitle.setText("Paso 2 de 4");
                toolbarSubtitle.setText("Seleccionar fecha y hora");
            } else if (destinationId == R.id.fragmentoPaso3) {
                toolbarTitle.setText("Paso 3 de 4");
                toolbarSubtitle.setText("Seleccionar motivo");
            } else if (destinationId == R.id.fragmentoPaso4) {
                toolbarTitle.setText("Paso 4 de 4");
                toolbarSubtitle.setText("Seleccionar obra social");
            } else if (destinationId == R.id.fragmentoResumenTurno) {
                toolbarTitle.setText("Resumen del turno");
                toolbarSubtitle.setText("Confirmar datos");
            } else {
                toolbarTitle.setText("Reservar Turno");
                toolbarSubtitle.setText(null);
            }
        });
    }
}