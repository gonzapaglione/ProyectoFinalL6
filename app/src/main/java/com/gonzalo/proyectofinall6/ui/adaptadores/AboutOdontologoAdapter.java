package com.gonzalo.proyectofinall6.ui.adaptadores;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.color.MaterialColors;
import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.data.remote.dto.EspecialidadResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.PromedioValoracionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class AboutOdontologoAdapter extends RecyclerView.Adapter<AboutOdontologoAdapter.ViewHolder> {

    private final List<OdontologoResponse> odontologos = new ArrayList<>();
    private final Map<Integer, PromedioValoracionResponse> promedios = new HashMap<>();

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<OdontologoResponse> items) {
        odontologos.clear();
        if (items != null) {
            odontologos.addAll(items);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitPromedios(Map<Integer, PromedioValoracionResponse> nuevosPromedios) {
        promedios.clear();
        if (nuevosPromedios != null) {
            promedios.putAll(nuevosPromedios);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_odontologo_about, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OdontologoResponse odontologo = odontologos.get(position);
        PromedioValoracionResponse promedio = promedios.get(odontologo.getId());
        holder.bind(odontologo, promedio);
    }

    @Override
    public int getItemCount() {
        return odontologos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNombre;
        private final TextView tvPromedioLabel;
        private final RatingBar rbPromedio;
        private final ChipGroup chipGroup;
        private final ImageView ivAvatar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPromedioLabel = itemView.findViewById(R.id.tvPromedioLabel);
            rbPromedio = itemView.findViewById(R.id.rbPromedio);
            chipGroup = itemView.findViewById(R.id.chipGroupEspecialidades);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }

        void bind(OdontologoResponse odontologo, PromedioValoracionResponse promedio) {
            String nombreCompleto = odontologo.getNombre() + " " + odontologo.getApellido();
            tvNombre.setText(nombreCompleto);

            ivAvatar.setImageResource(getPhotoResId(odontologo));

            if (promedio != null && promedio.getPromedio() != null && promedio.getCantidad() != null
                    && promedio.getCantidad() > 0) {
                tvPromedioLabel.setVisibility(View.VISIBLE);
                rbPromedio.setVisibility(View.VISIBLE);
                tvPromedioLabel.setText(String.format(Locale.getDefault(), "Promedio %.1f", promedio.getPromedio()));
                rbPromedio.setRating(promedio.getPromedio().floatValue());
            } else {
                tvPromedioLabel.setVisibility(View.GONE);
                rbPromedio.setVisibility(View.GONE);
            }

            chipGroup.removeAllViews();
            List<EspecialidadResponse> especialidades = odontologo.getEspecialidades();
            if (especialidades == null || especialidades.isEmpty()) {
                return;
            }

            for (int i = 0; i < especialidades.size(); i++) {
                EspecialidadResponse esp = especialidades.get(i);
                Chip chip = new Chip(itemView.getContext());
                chip.setText(esp.getNombre());
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setFocusable(false);

                applyChipColors(chip, i);
                chipGroup.addView(chip);
            }
        }

        private int getPhotoResId(OdontologoResponse odontologo) {

            int[] fotos = new int[] {
                    R.drawable.img_ana,
                    R.drawable.img_diego,
                    R.drawable.img_laura

            };

            int id = odontologo.getId();
            int index = Math.abs(id) % fotos.length;
            return fotos[index];
        }

        private void applyChipColors(Chip chip, int index) {
            // Alterna entre colores del theme para parecerse a la imagen sin hardcodear
            // colores.
            int bgAttr;
            int fgAttr;
            int mod = index % 3;
            if (mod == 0) {
                bgAttr = com.google.android.material.R.attr.colorPrimaryContainer;
                fgAttr = com.google.android.material.R.attr.colorOnPrimaryContainer;
            } else if (mod == 1) {
                bgAttr = com.google.android.material.R.attr.colorSecondaryContainer;
                fgAttr = com.google.android.material.R.attr.colorOnSecondaryContainer;
            } else {
                bgAttr = com.google.android.material.R.attr.colorTertiaryContainer;
                fgAttr = com.google.android.material.R.attr.colorOnTertiaryContainer;
            }

            int bg = MaterialColors.getColor(chip, bgAttr);
            int fg = MaterialColors.getColor(chip, fgAttr);
            chip.setChipBackgroundColor(ColorStateList.valueOf(bg));
            chip.setTextColor(fg);
        }
    }
}
