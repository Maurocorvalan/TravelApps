package com.example.travelapps.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelapps.R;
import com.example.travelapps.models.Viaje;

import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private RecyclerView recyclerView;
    private ViajeItinerarioAdapter viajeItinerarioAdapter;
    private TextView mensajeVacio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        // Inicializar el RecyclerView y el nuevo adapter
        recyclerView = root.findViewById(R.id.recyclerViewSlideshow);
        mensajeVacio = root.findViewById(R.id.mensajeVacio);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viajeItinerarioAdapter = new ViajeItinerarioAdapter(); // Cambia a ViajeItinerarioAdapter
        recyclerView.setAdapter(viajeItinerarioAdapter);

        viajeItinerarioAdapter.setOnItinerarioButtonClickListener(new ViajeItinerarioAdapter.OnItinerarioButtonClickListener() {
            @Override
            public void onItinerarioButtonClick(Viaje viaje) {
                // Aquí manejas el clic en el botón "Ver itinerario"
                // Navegar a ItinerariosFragment y pasar el idViaje como argumento
                ItinerariosFragment itinerariosFragment = new ItinerariosFragment();

                // Crear un Bundle y agregar el idViaje
                Bundle args = new Bundle();
                args.putInt("idViaje", viaje.getIdViaje()); // Suponiendo que `viaje` tiene un `idViaje`
                itinerariosFragment.setArguments(args);

                // Realizar la navegación con NavController
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_nav_slideshow_to_nav_itinerarios, args);
            }
        });

        // Obtener el ViewModel
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        // Observar los cambios en la lista de viajes
        slideshowViewModel.getViajes().observe(getViewLifecycleOwner(), new Observer<List<Viaje>>() {
            @Override
            public void onChanged(List<Viaje> viajes) {
                if (viajes != null && !viajes.isEmpty()) {
                    viajeItinerarioAdapter.setViajes(viajes); // Usamos el nuevo adaptador
                    recyclerView.setVisibility(View.VISIBLE);
                    mensajeVacio.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    mensajeVacio.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }
}
