package com.gonzalo.proyectofinall6.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public TurnosAdapter(List<Turno> turnos, Context context) {
        this.turnos = turnos;
        this.context = context;
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
        holder.bind(turno);
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    public class TurnoViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvDoctor, tvCoverage, tvReason, tvTreatment, tvStatusText;
        MaterialCardView chipStatus;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvCoverage = itemView.findViewById(R.id.tvCoverage);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvTreatment = itemView.findViewById(R.id.tvTreatment);
            tvStatusText = itemView.findViewById(R.id.tvStatusText);
            chipStatus = itemView.findViewById(R.id.chipStatus);
        }

        public void bind(Turno turno) {
            tvDate.setText(String.format("%s | %s", turno.getFecha(), turno.getHora()));
            tvDoctor.setText(String.format("Odontólogo: %s %s", turno.getNombreOdontologo(), turno.getApellidoOdontologo()));
            tvCoverage.setText(String.format("Cobertura: %s", turno.getObraSocial()));
            tvReason.setText(String.format("Motivo consulta: %s", turno.getMotivoConsulta()));

            if (turno.getEstadoTurno() != null) {
                tvStatusText.setText(turno.getEstadoTurno());
                switch (turno.getEstadoTurno()) {
                    case "PROGRAMADO":
                        chipStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_programmed_bg));
                        tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.status_programmed_text));
                        break;
                    case "REALIZADO":
                        chipStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_realized_bg));
                        tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.status_realized_text));
                        break;
                    case "CANCELADO":
                        chipStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_cancelled_bg));
                        tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled_text));
                        break;
                    default:
                        chipStatus.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }
}
