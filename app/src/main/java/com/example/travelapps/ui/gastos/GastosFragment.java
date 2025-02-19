package com.example.travelapps.ui.gastos;

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

public class GastosFragment extends Fragment {

    private GastosViewModel gastosViewModel;
    private RecyclerView recyclerView;
    private ViajeGastoAdapter viajeGastoAdapter;
    private TextView mensajeVacio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gastos, container, false);

        // Inicializar el RecyclerView y el nuevo adapter
        recyclerView = root.findViewById(R.id.recyclerViewGastos);
        mensajeVacio = root.findViewById(R.id.mensajeVacio);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viajeGastoAdapter = new ViajeGastoAdapter();
        recyclerView.setAdapter(viajeGastoAdapter);

        viajeGastoAdapter.setOnGastoButtonClickListener(new ViajeGastoAdapter.OnGastoButtonClickListener() {
            @Override
            public void onGastoButtonClick(Viaje viaje) {
                // Aquí manejas el clic en el botón "Ver gastos"
                Bundle args = new Bundle();
                args.putInt("idViaje", viaje.getIdViaje()); // Suponiendo que `viaje` tiene un `idViaje`

                // Realizar la navegación con NavController
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_nav_gastos_to_nav_ver_gastos, args);
            }
        });

        // Obtener el ViewModel
        gastosViewModel = new ViewModelProvider(this).get(GastosViewModel.class);

        // Observar los cambios en la lista de viajes
        gastosViewModel.getViajes().observe(getViewLifecycleOwner(), new Observer<List<Viaje>>() {
            @Override
            public void onChanged(List<Viaje> viajes) {
                if (viajes != null && !viajes.isEmpty()) {
                    viajeGastoAdapter.setViajes(viajes);
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
