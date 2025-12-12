package com.gonzalo.proyectofinall6.ui.ReservaTurno;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.data.remote.dto.HorarioDisponible;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder> {

    private List<HorarioDisponible> horarios;
    private OnItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(HorarioDisponible horario);
    }

    public HorarioAdapter(List<HorarioDisponible> horarios, OnItemClickListener listener) {
        this.horarios = horarios;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<HorarioDisponible> newHorarios) {
        this.horarios.clear();
        this.horarios.addAll(newHorarios);
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
        HorarioDisponible horario = horarios.get(position);
        holder.bind(horario, position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition != holder.getAdapterPosition()) {
                int oldSelected = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldSelected);
                notifyItemChanged(selectedPosition);
                listener.onItemClick(horario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    static class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvHorario;

        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHorario = itemView.findViewById(R.id.tv_horario);
        }

        public void bind(final HorarioDisponible horario, boolean isSelected) {
            tvHorario.setText(horario.getHora().substring(0, 5));
            itemView.setSelected(isSelected);
            if (isSelected) {
                tvHorario.setTextColor(Color.WHITE);
            } else {
                tvHorario.setTextColor(Color.BLACK);
            }
        }
    }
}
