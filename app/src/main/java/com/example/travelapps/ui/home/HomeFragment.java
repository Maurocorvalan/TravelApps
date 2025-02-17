package com.example.travelapps.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.R;
import com.example.travelapps.databinding.FragmentHomeBinding;
import com.example.travelapps.models.Viaje;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private ViajeAdapter viajesAdapter;
    private TextView mensajeVacio;
    private ApiService apiService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        mensajeVacio = root.findViewById(R.id.textoVacio);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viajesAdapter = new ViajeAdapter();
        recyclerView.setAdapter(viajesAdapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getViajes().observe(getViewLifecycleOwner(), new Observer<List<Viaje>>() {
            @Override
            public void onChanged(List<Viaje> viajes) {
                if (viajes != null && !viajes.isEmpty()) {
                    viajesAdapter.setViajes(viajes);
                    recyclerView.setVisibility(View.VISIBLE);
                    mensajeVacio.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    mensajeVacio.setVisibility(View.VISIBLE);
                }
            }
        });

        // Establecer el listener para el botón de editar
        viajesAdapter.setOnEditButtonClickListener(new ViajeAdapter.OnEditButtonClickListener() {
            @Override
            public void onEditButtonClick(Viaje viaje) {
                // Obtener el id del viaje y realizar la llamada para obtener los detalles
                obtenerDetallesViaje(viaje.getIdViaje());
            }
        });
        Button btnAñadirViaje = root.findViewById(R.id.btnAñadirViaje);
        btnAñadirViaje.setOnClickListener(v -> {
            Log.d("CrearViajeFragment", "Botón de guardar presionado");

            // Redirigir al fragmento para crear un nuevo viaje
            NavController navController = NavHostFragment.findNavController(HomeFragment.this);
            navController.navigate(R.id.action_home_to_create); // Este es el ID de tu acción en el nav_graph
        });


        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        return root;
    }

    private void obtenerDetallesViaje(int viajeId) {
        Call<Viaje> call = apiService.obtenerViaje(viajeId);
        call.enqueue(new Callback<Viaje>() {
            @Override
            public void onResponse(Call<Viaje> call, Response<Viaje> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Viaje viaje = response.body();
                    Log.d("HomeFragment", "Viaje obtenido: " + viaje);
                    showEditFragment(viaje);
                } else {
                    Toast.makeText(getContext(), "No se encontró el viaje", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Viaje> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditFragment(Viaje viaje) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("viaje", viaje);

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_home_to_edit, bundle);
    }

}