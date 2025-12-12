package com.gonzalo.proyectofinall6.ui.Inicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.ui.adaptadores.AboutOdontologoAdapter;
import com.gonzalo.proyectofinall6.ui.viewmodels.AboutUsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoAboutUs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoAboutUs extends Fragment {

    private AboutUsViewModel aboutUsViewModel;
    private AboutOdontologoAdapter adapter;

    public FragmentoAboutUs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_about_us, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rvOdontologosAbout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AboutOdontologoAdapter();
        recyclerView.setAdapter(adapter);

        aboutUsViewModel = new ViewModelProvider(this).get(AboutUsViewModel.class);

        aboutUsViewModel.getOdontologos().observe(getViewLifecycleOwner(), odontologos -> {
            adapter.submitList(odontologos);
        });

        aboutUsViewModel.getPromedios().observe(getViewLifecycleOwner(), promedios -> {
            adapter.submitPromedios(promedios);
        });

        aboutUsViewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });

        aboutUsViewModel.fetchOdontologos();
    }
}