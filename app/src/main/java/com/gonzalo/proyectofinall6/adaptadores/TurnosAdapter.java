package com.gonzalo.proyectofinall6.adaptadores;

import android.annotation.SuppressLint;
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
    private OnTurnoActionListener listener;

    public interface OnTurnoActionListener {
        void onCancelarTurno(int turnoId, String motivo);
        void onVerDetalle(int turnoId);
    }

    public TurnosAdapter(List<Turno> turnos, Context context, OnTurnoActionListener listener) {
        this.turnos = turnos;
        this.context = context;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Turno> newTurnos) {
        this.turnos.clear();
        this.turnos.addAll(newTurnos);
        notifyDataSetChanged();
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
        holder.bind(turno, listener, context);
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvOdontologo, tvMotivo, tvEstado;
        Button btnCancelar, btnDetalle;
        MaterialCardView chipEstado;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvOdontologo = itemView.findViewById(R.id.tvOdontologo);
            tvMotivo = itemView.findViewById(R.id.tvMotivo);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
            btnDetalle = itemView.findViewById(R.id.btnDetalle);
            chipEstado = itemView.findViewById(R.id.chipEstado);
        }

        public void bind(final Turno turno, final OnTurnoActionListener listener, Context context) {
            tvFecha.setText(turno.getFecha() + " | " + turno.getHora().substring(0, 5));
            tvOdontologo.setText("Odontólogo: " + turno.getNombreOdontologo() + " " + turno.getApellidoOdontologo());
            tvMotivo.setText("Motivo consulta: " + turno.getMotivoConsulta());
            tvEstado.setText(turno.getEstadoTurno());

            // Lógica de visibilidad y estilo de botones y estados
            switch (turno.getEstadoTurno().toLowerCase()) {
                case "programado":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_programmed_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_programmed_text));
                    btnCancelar.setVisibility(View.VISIBLE);
                    btnDetalle.setVisibility(View.GONE);
                    break;
                case "realizado":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_realized_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_realized_text));
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.VISIBLE);
                    break;
                case "cancelado":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_cancelled_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled_text));
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.GONE);
                    break;
                case "ausente":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_absent_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_absent_text));
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.GONE);
                    break;
                default:
                    chipEstado.setVisibility(View.GONE);
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.GONE);
                    break;
            }

            btnCancelar.setOnClickListener(v -> {
                showCancelarDialog(context, turno, listener);
            });
            btnDetalle.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerDetalle(turno.getIdTurno().intValue());
                }
            });
        }

        private void showCancelarDialog(Context context, final Turno turno, final OnTurnoActionListener listener) {
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
