package com.example.travelapps.ui.slideshow;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.travelapps.R;
import com.example.travelapps.models.Itinerario;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItinerarioDialogFragment extends DialogFragment {

    private EditText etActividad, etUbicacion;
    private int idViaje;

    public static AddItinerarioDialogFragment newInstance(int idViaje) {
        AddItinerarioDialogFragment fragment = new AddItinerarioDialogFragment();
        Bundle args = new Bundle();
        args.putInt("idViaje", idViaje);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inicializa el dialogo
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment, null);

        etActividad = view.findViewById(R.id.etActividad);
        etUbicacion = view.findViewById(R.id.etUbicacion);
        Button btnGuardarItinerario = view.findViewById(R.id.btnGuardarItinerario);

        if (getArguments() != null) {
            idViaje = getArguments().getInt("idViaje");
        }

        btnGuardarItinerario.setOnClickListener(v -> {
            String actividad = etActividad.getText().toString();
            String ubicacion = etUbicacion.getText().toString();

            if (actividad.isEmpty() || ubicacion.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                crearItinerario(idViaje, actividad, ubicacion);
            }
        });

        dialog.setContentView(view);
        return dialog;
    }

    private void crearItinerario(int idViaje, String actividad, String ubicacion) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Itinerario itinerario = new Itinerario();
        itinerario.setActividad(actividad);
        itinerario.setUbicacion(ubicacion);

        Call<Itinerario> call = apiService.crearItinerario(idViaje, itinerario);

        call.enqueue(new Callback<Itinerario>() {
            @Override
            public void onResponse(Call<Itinerario> call, Response<Itinerario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Itinerario añadido con éxito", Toast.LENGTH_SHORT).show();
                    dismiss(); // Cierra el modal
                } else {
                    Toast.makeText(getContext(), "Error al crear itinerario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Itinerario> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
