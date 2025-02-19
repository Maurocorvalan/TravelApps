package com.example.travelapps.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelapps.R;
import com.example.travelapps.models.Itinerario;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarItinerarioFragment extends Fragment {

    private EditText edtActividad, edtUbicacion;
    private Button btnGuardar;
    private int idItinerario, idViaje;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editar_itinerario, container, false);

        edtActividad = root.findViewById(R.id.edtActividad);
        edtUbicacion = root.findViewById(R.id.edtUbicacion);
        btnGuardar = root.findViewById(R.id.btnGuardar);

        if (getArguments() != null) {
            idViaje = getArguments().getInt("idViaje");
            idItinerario = getArguments().getInt("idItinerario");
            edtActividad.setText(getArguments().getString("actividad"));
            edtUbicacion.setText(getArguments().getString("ubicacion"));
        }

        btnGuardar.setOnClickListener(v -> actualizarItinerario());

        return root;
    }

    private void actualizarItinerario() {
        String nuevaActividad = edtActividad.getText().toString();
        String nuevaUbicacion = edtUbicacion.getText().toString();

        Itinerario itinerarioActualizado = new Itinerario();
        itinerarioActualizado.setIdItinerario(idItinerario);
        itinerarioActualizado.setActividad(nuevaActividad);
        itinerarioActualizado.setUbicacion(nuevaUbicacion);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.actualizarItinerario(idViaje, idItinerario, itinerarioActualizado);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Itinerario actualizado", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    Log.d("salida", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
