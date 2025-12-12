package com.gonzalo.proyectofinall6.ui.adaptadores;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;

import java.util.List;

public class OdontologoAdapter extends RecyclerView.Adapter<OdontologoAdapter.OdontologoViewHolder> {

    private List<OdontologoResponse> odontologos;
    private OnItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(OdontologoResponse odontologo);
    }

    public OdontologoAdapter(List<OdontologoResponse> odontologos, OnItemClickListener listener) {
        this.odontologos = odontologos;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<OdontologoResponse> newOdontologos) {
        this.odontologos.clear();
        this.odontologos.addAll(newOdontologos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OdontologoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_odontologo, parent, false);
        return new OdontologoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OdontologoViewHolder holder, int position) {
        OdontologoResponse odontologo = odontologos.get(position);
        holder.bind(odontologo, position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition != holder.getAdapterPosition()) {
                int oldSelected = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldSelected);
                notifyItemChanged(selectedPosition);
                listener.onItemClick(odontologo);
            }
        });

        holder.radioButton.setOnClickListener(v -> {
            if (selectedPosition != holder.getAdapterPosition()) {
                int oldSelected = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldSelected);
                notifyItemChanged(selectedPosition);
                listener.onItemClick(odontologo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return odontologos.size();
    }

    static class OdontologoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreOdontologo;
        RadioButton radioButton;

        public OdontologoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreOdontologo = itemView.findViewById(R.id.nombre_odontologo);
            radioButton = itemView.findViewById(R.id.radio_button);
        }

        public void bind(final OdontologoResponse odontologo, boolean isSelected) {
            nombreOdontologo.setText(odontologo.getNombre() + " " + odontologo.getApellido());
            radioButton.setChecked(isSelected);
        }
    }
}
