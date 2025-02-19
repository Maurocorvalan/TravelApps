package com.example.travelapps.ui.gallery;

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

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RecyclerView recyclerView;
    private ViajeGalleryAdapter viajeGalleryAdapter;
    private TextView mensajeVacio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Inicializar el RecyclerView y el adaptador
        recyclerView = root.findViewById(R.id.recyclerViewGallery);
        mensajeVacio = root.findViewById(R.id.mensajeVacio);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viajeGalleryAdapter = new ViajeGalleryAdapter();
        recyclerView.setAdapter(viajeGalleryAdapter);

        viajeGalleryAdapter.setOnGalleryButtonClickListener(new ViajeGalleryAdapter.OnGalleryButtonClickListener() {
            @Override
            public void onGalleryButtonClick(Viaje viaje) {
                // Aquí manejas el clic en el botón "Mirar galería"
                // Navegar a la Galería correspondiente y pasar el idViaje como argumento
                Bundle args = new Bundle();
                args.putInt("idViaje", viaje.getIdViaje());
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_nav_gallery_to_nav_galeria, args);
            }
        });

        // Obtener el ViewModel
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        // Observar los cambios en la lista de viajes
        galleryViewModel.getViajes().observe(getViewLifecycleOwner(), new Observer<List<Viaje>>() {
            @Override
            public void onChanged(List<Viaje> viajes) {
                if (viajes != null && !viajes.isEmpty()) {
                    viajeGalleryAdapter.setViajes(viajes);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null); // Limpiar el adaptador al destruir la vista
    }
}
