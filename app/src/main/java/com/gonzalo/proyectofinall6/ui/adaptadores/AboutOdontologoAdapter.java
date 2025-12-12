package com.gonzalo.proyectofinall6.ui.adaptadores;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.color.MaterialColors;
import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.data.remote.dto.EspecialidadResponse;
import com.gonzalo.proyectofinall6.data.remote.dto.OdontologoResponse;

import java.util.ArrayList;
import java.util.List;

public class AboutOdontologoAdapter extends RecyclerView.Adapter<AboutOdontologoAdapter.ViewHolder> {

    private final List<OdontologoResponse> odontologos = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<OdontologoResponse> items) {
        odontologos.clear();
        if (items != null) {
            odontologos.addAll(items);
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
        holder.bind(odontologos.get(position));
    }

    @Override
    public int getItemCount() {
        return odontologos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNombre;
        private final ChipGroup chipGroup;
        private final ImageView ivAvatar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            chipGroup = itemView.findViewById(R.id.chipGroupEspecialidades);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }

        void bind(OdontologoResponse odontologo) {
            String nombreCompleto = odontologo.getNombre() + " " + odontologo.getApellido();
            tvNombre.setText(nombreCompleto);

            ivAvatar.setImageResource(getPhotoResId(odontologo));

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
