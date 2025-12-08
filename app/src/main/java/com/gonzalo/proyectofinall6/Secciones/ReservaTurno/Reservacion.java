package com.gonzalo.proyectofinall6.Secciones.ReservaTurno;

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
            if (destinationId == R.id.paso1Fragment) {
                toolbarTitle.setText("Paso 1 de 5");
                toolbarSubtitle.setText("Seleccionar especialidad");
            } else if (destinationId == R.id.paso2Fragment) {
                toolbarTitle.setText("Paso 2 de 5");
                toolbarSubtitle.setText("Seleccionar profesional");
            } else if (destinationId == R.id.paso3Fragment) {
                toolbarTitle.setText("Paso 3 de 5");
                toolbarSubtitle.setText("Seleccionar fecha");
            } else if (destinationId == R.id.paso4Fragment) {
                toolbarTitle.setText("Paso 4 de 5");
                toolbarSubtitle.setText("Seleccionar horario");
            } else if (destinationId == R.id.resumenTurnoFragment) {
                toolbarTitle.setText("Resumen del turno");
                toolbarSubtitle.setText("Confirmar datos");
            } else {
                toolbarTitle.setText("Reservar Turno");
                toolbarSubtitle.setText(null);
            }
        });
    }
}