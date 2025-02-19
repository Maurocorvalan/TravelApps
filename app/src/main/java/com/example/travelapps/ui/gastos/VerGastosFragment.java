package com.example.travelapps.ui.gastos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.R;
import com.example.travelapps.models.Gasto;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerGastosFragment extends Fragment {

    private RecyclerView recyclerView;
    private GastosAdapter gastosAdapter;
    private Button btnAñadirGasto;
    private TextView textoSinGastos;
    private int idViaje;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ver_gastos, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewGastos);
        btnAñadirGasto = root.findViewById(R.id.btnAñadirGasto);
        textoSinGastos = root.findViewById(R.id.textoSinGastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gastosAdapter = new GastosAdapter(this::eliminarGasto);
        recyclerView.setAdapter(gastosAdapter);

        // Obtener el ID del viaje desde los argumentos
        idViaje = getArguments().getInt("idViaje", -1);
        cargarGastos(idViaje);

        btnAñadirGasto.setOnClickListener(v -> {
            AñadirGastoFragment dialog = AñadirGastoFragment.newInstance(idViaje);
            dialog.setTargetFragment(VerGastosFragment.this, 1); // Establecer el target
            dialog.show(getParentFragmentManager(), "AñadirGastoFragment");
        });

        return root;
    }

    private void cargarGastos(int idViaje) {
        String token = getActivity().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE)
                .getString("jwt_token", null);
        if (token == null) {
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Gasto>> call = apiService.obtenerGastos(idViaje, "Bearer " + token);

        call.enqueue(new Callback<List<Gasto>>() {
            @Override
            public void onResponse(Call<List<Gasto>> call, Response<List<Gasto>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        List<Gasto> gastos = response.body();
                        if (gastos.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            textoSinGastos.setText("No se posee ningún gasto.");
                            textoSinGastos.setVisibility(View.VISIBLE);
                        } else {
                            gastosAdapter.setGastos(gastos);
                            recyclerView.setVisibility(View.VISIBLE);
                            textoSinGastos.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e("API_ERROR", "Error en la respuesta: " + response.message());
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textoSinGastos.setText("No se posee ningún gasto.");
                    textoSinGastos.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Gasto>> call, Throwable t) {
                Log.e("API_FAILURE", "Fallo en la conexión: " + t.getMessage());
            }
        });
    }

    private void eliminarGasto(int idGasto) {
        String token = getActivity().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE)
                .getString("jwt_token", null);
        if (token == null) {
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.eliminarGasto(idGasto, "Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cargarGastos(idViaje);
                } else {
                    Log.e("API_ERROR", "Error al eliminar el gasto: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_FAILURE", "Fallo en la conexión al eliminar: " + t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            cargarGastos(idViaje);
        }
    }
}
