package com.example.travelapps.ui.gastos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.travelapps.R;
import com.example.travelapps.models.Gasto;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarGastoFragment extends Fragment {

    private EditText  editTextMonto, editTextDescripcion;
    private Button buttonGuardar;
    private Spinner spinnerCategorias;

    private int idGasto;

    public static EditarGastoFragment newInstance(Gasto gasto) {
        EditarGastoFragment fragment = new EditarGastoFragment();
        Bundle args = new Bundle();
        args.putInt("idGasto", gasto.getIdGasto());
        args.putString("categoria", gasto.getCategoria());
        args.putDouble("monto", gasto.getMonto());
        args.putString("descripcion", gasto.getDescripcion());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_gasto, container, false);

        spinnerCategorias = view.findViewById(R.id.spinnerCategorias);
        editTextMonto = view.findViewById(R.id.editTextMonto);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);
        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        // Cargar las categorías en el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapter);
        // Obtener los datos del gasto
        if (getArguments() != null) {
            idGasto = getArguments().getInt("idGasto", -1);
            String categoria = getArguments().getString("categoria");
            int position = adapter.getPosition(categoria);
            spinnerCategorias.setSelection(position);
            editTextMonto.setText(String.valueOf(getArguments().getDouble("monto")));
            editTextDescripcion.setText(getArguments().getString("descripcion"));
        }

        buttonGuardar.setOnClickListener(v -> actualizarGasto(spinnerCategorias));

        return view;
    }

    private void actualizarGasto(Spinner spinnerCategorias) {
        String categoria = spinnerCategorias.getSelectedItem().toString();
        double monto = Double.parseDouble(editTextMonto.getText().toString());
        String descripcion = editTextDescripcion.getText().toString();

        Gasto gasto = new Gasto();
        gasto.setCategoria(categoria);
        gasto.setMonto(monto);
        gasto.setDescripcion(descripcion);

        String token = getActivity().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE)
                .getString("jwt_token", null);
        if (token == null) {
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Gasto> call = apiService.actualizarGasto(idGasto, gasto, "Bearer " + token);

        call.enqueue(new Callback<Gasto>() {
            @Override
            public void onResponse(Call<Gasto> call, Response<Gasto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Gasto actualizado", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack(); // Regresar al fragmento anterior
                } else {
                    Log.e("API_ERROR", "Error al actualizar el gasto: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Gasto> call, Throwable t) {
                Log.e("API_FAILURE", "Fallo en la conexión: " + t.getMessage());
            }
        });
    }
}
