package com.gonzalo.proyectofinall6.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.modelos.Turno;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class TurnosAdapter extends RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder> {
    private List<Turno> turnos;
    private Context context;
    private OnTurnoActionListener listener; // Interface para comunicar acciones

    // Interface para manejar las acciones de los botones
    public interface OnTurnoActionListener {
        void onCancelarTurno(int turnoId, String motivo);
        void onVerDetalle(int turnoId);
    }

    public TurnosAdapter(List<Turno> turnos, Context context, OnTurnoActionListener listener) {
        this.turnos = turnos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historia_clinica, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        Turno turno = turnos.get(position);
        holder.bind(turno, listener);
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    public class TurnoViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvDoctor, tvCoverage, tvReason, tvTreatment, tvStatusText;
        MaterialCardView chipStatus;
        Button btnAction;


        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvCoverage = itemView.findViewById(R.id.tvCoverage);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvTreatment = itemView.findViewById(R.id.tvTreatment);
            tvStatusText = itemView.findViewById(R.id.tvStatusText);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            btnAction = itemView.findViewById(R.id.btnAction);
        }

        public void bind(final Turno turno, final OnTurnoActionListener listener) {
            tvDate.setText(String.format("%s | %s", turno.getFecha(), turno.getHora()));
            tvDoctor.setText(String.format("Odontólogo: %s %s", turno.getNombreOdontologo(), turno.getApellidoOdontologo()));
            tvCoverage.setText(String.format("Cobertura: %s", turno.getObraSocial()));
            tvReason.setText(String.format("Motivo consulta: %s", turno.getMotivoConsulta()));

            // Ocultar vistas por defecto y mostrarlas según el estado
            tvTreatment.setVisibility(View.GONE);
            btnAction.setVisibility(View.GONE);

            if (turno.getEstadoTurno() != null) {
                tvStatusText.setText(turno.getEstadoTurno());
                chipStatus.setVisibility(View.VISIBLE);

                switch (turno.getEstadoTurno()) {
                    case "PROGRAMADO":
                        chipStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_programmed_bg));
                        tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.status_programmed_text));
                        btnAction.setVisibility(View.VISIBLE);
                        btnAction.setText("Cancelar");
                        btnAction.setOnClickListener(v -> {
                            showCancelarDialog(turno, listener);
                        });
                        break;
                    case "REALIZADO":
                        chipStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_realized_bg));
                        tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.status_realized_text));
                        btnAction.setVisibility(View.VISIBLE);
                        btnAction.setText("Ver detalle");
                        btnAction.setOnClickListener(v -> {
                            if (listener != null) {
                                listener.onVerDetalle(turno.getIdTurno().intValue());
                            }
                        });
                        break;
                    case "CANCELADO":
                        chipStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_cancelled_bg));
                        tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled_text));
                        break;
                    case "AUSENTE":
                        chipStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_cancelled_bg));
                        tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled_text));
                        break;
                    default:
                        chipStatus.setVisibility(View.GONE);
                        break;
                }
            } else {
                chipStatus.setVisibility(View.GONE);
            }
        }

        private void showCancelarDialog(final Turno turno, final OnTurnoActionListener listener) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Cancelar Turno");
            builder.setMessage("Por favor, ingrese el motivo de la cancelación.");

            final EditText input = new EditText(context);
            input.setHint("Motivo (opcional)");
            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String motivo = input.getText().toString();
                if (listener != null) {
                    listener.onCancelarTurno(turno.getIdTurno().intValue(), motivo);
                }
            });
            builder.setNegativeButton("Cerrar", (dialog, which) -> dialog.cancel());

            builder.show();
        }
    }
}
