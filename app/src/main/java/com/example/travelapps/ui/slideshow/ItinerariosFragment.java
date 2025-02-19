package com.example.travelapps.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelapps.R;
import com.example.travelapps.models.Itinerario;
import android.util.Log;

import com.example.travelapps.ui.slideshow.ItinerarioAdapter;

import java.util.ArrayList;
import java.util.List;

public class ItinerariosFragment extends Fragment {

    private ItinerariosViewModel itinerariosViewModel;
    private RecyclerView recyclerView;
    private ItinerarioAdapter itinerarioAdapter;
    private TextView mensajeVacio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_itinerarios, container, false);

        // Inicializa el RecyclerView y el adapter
        recyclerView = root.findViewById(R.id.recyclerViewItinerarios);
        mensajeVacio = root.findViewById(R.id.mensajeVacio);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itinerarioAdapter = new ItinerarioAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(itinerarioAdapter);

        // Obtener el ViewModel
        itinerariosViewModel = new ViewModelProvider(this).get(ItinerariosViewModel.class);

        // Observar los cambios en la lista de itinerarios
        itinerariosViewModel.getItinerarios().observe(getViewLifecycleOwner(), new Observer<List<Itinerario>>() {
            @Override
            public void onChanged(List<Itinerario> itinerarios) {
                Log.d("ItinerariosFragment", "onChanged: Lista de itinerarios actualizada");

                if (itinerarios != null && !itinerarios.isEmpty()) {
                    Log.d("ItinerariosFragment", "Lista de itinerarios no vacía, tamaño: " + itinerarios.size());

                    itinerarioAdapter.setItinerarios(itinerarios);
                    recyclerView.setVisibility(View.VISIBLE);
                    mensajeVacio.setVisibility(View.GONE);
                } else {
                    Log.d("ItinerariosFragment", "Lista de itinerarios vacía");

                    recyclerView.setVisibility(View.GONE);
                    mensajeVacio.setVisibility(View.VISIBLE);
                }
            }
        });

        // Cargar los itinerarios al fragmento
        if (getArguments() != null) {
            int idViaje = getArguments().getInt("idViaje");
            Log.d("ItinerariosFragment", "idViaje recibido: " + idViaje);
            Button btnAñadirItinerario = root.findViewById(R.id.btnAñadirItinerario);

            btnAñadirItinerario.setOnClickListener(v -> {
                // Muestra el modal
                AddItinerarioDialogFragment dialog = AddItinerarioDialogFragment.newInstance(idViaje);
                dialog.show(getChildFragmentManager(), "AddItinerario");
            });
            itinerariosViewModel.cargarItinerarios(idViaje);
        }

        return root;
    }
}
