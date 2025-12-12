package com.gonzalo.proyectofinall6.ui.adaptadores;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.dominio.modelos.Turno;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class TurnosAdapter extends RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder> {

    private List<Turno> turnos;
    private Context context;
    private OnTurnoActionListener listener;

    public interface OnTurnoActionListener {
        void onCancelarTurno(int turnoId, String motivo);

        void onVerDetalle(int turnoId);

        void onValorarTurno(int turnoId, int estrellas, String comentario);
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
        TextView tvFecha, tvOdontologo, tvMotivo, tvCancelacion, tvResena, tvEstado;
        TextView tvCancelacionInfo;
        TextView tvValoracionMensaje;
        TextView tvValoracionCta;
        TextView tvValoracionLink;
        RatingBar rbValoracion;
        Button btnCancelar, btnDetalle;
        MaterialCardView chipEstado;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvOdontologo = itemView.findViewById(R.id.tvOdontologo);
            tvMotivo = itemView.findViewById(R.id.tvMotivo);
            tvCancelacion = itemView.findViewById(R.id.tvCancelacion);
            tvResena = itemView.findViewById(R.id.tvResena);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvCancelacionInfo = itemView.findViewById(R.id.tvCancelacionInfo);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
            btnDetalle = itemView.findViewById(R.id.btnDetalle);
            chipEstado = itemView.findViewById(R.id.chipEstado);
            tvValoracionMensaje = itemView.findViewById(R.id.tvValoracionMensaje);
            tvValoracionCta = itemView.findViewById(R.id.tvValoracionCta);
            tvValoracionLink = itemView.findViewById(R.id.tvValoracionLink);
            rbValoracion = itemView.findViewById(R.id.rbValoracion);
        }

        public void bind(final Turno turno, final OnTurnoActionListener listener, Context context) {
            tvFecha.setText(turno.getFecha() + " | " + turno.getHora().substring(0, 5));
            tvOdontologo.setText("Odontólogo: " + turno.getNombreOdontologo() + " " + turno.getApellidoOdontologo());
            tvMotivo.setText("Motivo consulta: " + turno.getMotivoConsulta());
            tvEstado.setText(turno.getEstadoTurno());

            tvResena.setVisibility(View.GONE);
            tvResena.setText(null);

            tvCancelacion.setVisibility(View.GONE);
            tvCancelacion.setText(null);

            tvCancelacionInfo.setVisibility(View.GONE);
            tvCancelacionInfo.setText(null);

            // Reset estado de valoración
            tvValoracionMensaje.setVisibility(View.GONE);
            tvValoracionMensaje.setOnClickListener(null);
            tvValoracionCta.setVisibility(View.GONE);
            // tvValoracionCta usa texto definido en XML (@string/nos_ayudas)
            // No lo limpiamos para que se vea correctamente.
            tvValoracionLink.setVisibility(View.GONE);
            tvValoracionLink.setOnClickListener(null);
            rbValoracion.setVisibility(View.GONE);

            // Lógica de visibilidad y estilo de botones y estados
            switch (turno.getEstadoTurno().toLowerCase()) {
                case "programado":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_programmed_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_programmed_text));
                    btnCancelar.setVisibility(View.VISIBLE);
                    btnDetalle.setVisibility(View.GONE);

                    boolean puedeCancelar = puedeCancelarPaciente(turno);
                    btnCancelar.setEnabled(puedeCancelar);
                    btnCancelar.setAlpha(puedeCancelar ? 1f : 0.5f);

                    if (!puedeCancelar) {
                        tvCancelacionInfo.setVisibility(View.VISIBLE);
                        tvCancelacionInfo.setText(R.string.turno_cancelacion_fuera_de_horario);
                    }
                    break;
                case "realizado":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_realized_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_realized_text));
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.VISIBLE);
                    btnCancelar.setEnabled(true);
                    btnCancelar.setAlpha(1f);
                    if (turno.getEstrellasValoracion() == null) {
                        tvValoracionCta.setVisibility(View.VISIBLE);
                        tvValoracionLink.setVisibility(View.VISIBLE);
                        tvValoracionLink.setPaintFlags(tvValoracionLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        tvValoracionLink.setOnClickListener(v -> showValorarDialog(context, turno, listener));
                        rbValoracion.setVisibility(View.GONE);
                    } else {
                        tvValoracionMensaje.setVisibility(View.VISIBLE);
                        tvValoracionMensaje.setText(R.string.turno_valoracion_gracias);
                        rbValoracion.setVisibility(View.VISIBLE);
                        rbValoracion.setRating(turno.getEstrellasValoracion());
                        tvValoracionMensaje.setOnClickListener(null);

                        String resena = turno.getComentarioValoracion();
                        if (resena != null && !resena.trim().isEmpty()) {
                            tvResena.setVisibility(View.VISIBLE);
                            tvResena.setText(resena);
                        }
                    }
                    break;
                case "cancelado":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_cancelled_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled_text));
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.GONE);
                    btnCancelar.setEnabled(true);
                    btnCancelar.setAlpha(1f);

                    String notas = turno.getNotasCancelacion();
                    if (notas != null && !notas.trim().isEmpty()) {
                        tvCancelacion.setVisibility(View.VISIBLE);
                        tvCancelacion.setText("Motivo cancelación: " + notas);
                    }
                    break;
                case "ausente":
                    chipEstado.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_absent_bg));
                    tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_absent_text));
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.GONE);
                    btnCancelar.setEnabled(true);
                    btnCancelar.setAlpha(1f);
                    break;
                default:
                    chipEstado.setVisibility(View.GONE);
                    btnCancelar.setVisibility(View.GONE);
                    btnDetalle.setVisibility(View.GONE);
                    btnCancelar.setEnabled(true);
                    btnCancelar.setAlpha(1f);
                    break;
            }

            if (!btnCancelar.isEnabled()) {
                btnCancelar.setOnClickListener(null);
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

        private boolean puedeCancelarPaciente(Turno turno) {
            try {
                if (turno.getFecha() == null || turno.getHora() == null) {
                    return true;
                }
                String hora = turno.getHora().trim();
                // La API suele venir como HH:mm:ss; el UI muestra substring(0,5)
                String patronHora = hora.length() >= 8 ? "HH:mm:ss" : "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + patronHora, Locale.getDefault());
                Date fechaTurno = sdf.parse(turno.getFecha() + " " + hora);
                if (fechaTurno == null) {
                    return true;
                }
                long ahora = System.currentTimeMillis();
                long diffMs = fechaTurno.getTime() - ahora;
                long tresHorasMs = 3L * 60L * 60L * 1000L;
                return diffMs >= tresHorasMs;
            } catch (ParseException e) {
                return true;
            }
        }

        private void showValorarDialog(Context context, final Turno turno, final OnTurnoActionListener listener) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Valorar turno");

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_valorar_turno, null, false);
            RatingBar ratingBar = dialogView.findViewById(R.id.rbValorar);
            EditText input = dialogView.findViewById(R.id.etComentario);
            ratingBar.setIsIndicator(false);
            if (ratingBar.getRating() < 1f) {
                ratingBar.setRating(1f);
            }
            builder.setView(dialogView);

            builder.setPositiveButton("Enviar", (dialog, which) -> {
                int estrellas = (int) ratingBar.getRating();
                if (estrellas < 1)
                    estrellas = 1;
                String comentario = input.getText() != null ? input.getText().toString() : null;
                if (listener != null && turno.getIdTurno() != null) {
                    listener.onValorarTurno(turno.getIdTurno().intValue(), estrellas, comentario);
                }
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            builder.show();
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
