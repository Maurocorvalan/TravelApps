package com.example.travelapps.ui.gastos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.travelapps.R;
import com.example.travelapps.models.Gasto;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AñadirGastoFragment extends DialogFragment {

    private Spinner spinnerCategoria;
    private EditText etMonto, etDescripcion;
    private int idViaje;

    public static AñadirGastoFragment newInstance(int idViaje) {
        AñadirGastoFragment fragment = new AñadirGastoFragment();
        Bundle args = new Bundle();
        args.putInt("idViaje", idViaje);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anadir_gasto, container, false);

        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        etMonto = view.findViewById(R.id.edtMonto);
        etDescripcion = view.findViewById(R.id.edtDescripcion);
        Button btnGuardarGasto = view.findViewById(R.id.btnGuardarGasto);

        // Configurar el spinner con las categorías
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // Obtener el ID del viaje desde los argumentos
        if (getArguments() != null) {
            idViaje = getArguments().getInt("idViaje", -1);
        }

        btnGuardarGasto.setOnClickListener(v -> {
            String categoria = spinnerCategoria.getSelectedItem().toString();
            String monto = etMonto.getText().toString();
            String descripcion = etDescripcion.getText().toString();

            if (monto.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                crearGasto(idViaje, categoria, Double.parseDouble(monto), descripcion);
            }
        });

        return view;
    }

    private void crearGasto(int idViaje, String categoria, double monto, String descripcion) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Gasto gasto = new Gasto();
        gasto.setIdViaje(idViaje);
        gasto.setCategoria(categoria);
        gasto.setMonto(monto);
        gasto.setDescripcion(descripcion);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", null);

        Call<Gasto> call = apiService.crearGasto(gasto, "Bearer " + token);

        call.enqueue(new Callback<Gasto>() {
            @Override
            public void onResponse(Call<Gasto> call, Response<Gasto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Gasto añadido con éxito", Toast.LENGTH_SHORT).show();
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 1, null);

                    dismiss(); // Cierra el modal
                } else {
                    Toast.makeText(getContext(), "Error al crear gasto: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Gasto> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
