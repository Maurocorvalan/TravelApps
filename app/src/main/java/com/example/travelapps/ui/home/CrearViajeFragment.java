package com.example.travelapps.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.travelapps.R;
import com.example.travelapps.models.Viaje;
import com.example.travelapps.models.ViajeDTO;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CrearViajeFragment extends Fragment {
    private EditText nombreEditText, descripcionEditText, fechaInicioEditText, fechaFinEditText;
    private Button guardarButton;
    private ApiService apiService;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crear_viaje, container, false);

        // Inicializar vistas
        nombreEditText = root.findViewById(R.id.nombreEditText);
        descripcionEditText = root.findViewById(R.id.descripcionEditText);
        fechaInicioEditText = root.findViewById(R.id.fechaInicioEditText);
        fechaFinEditText = root.findViewById(R.id.fechaFinEditText);
        guardarButton = root.findViewById(R.id.guardarButton);

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        guardarButton.setOnClickListener(v -> {
            String nombre = nombreEditText.getText().toString();
            String descripcion = descripcionEditText.getText().toString();
            String fechaInicio = fechaInicioEditText.getText().toString();
            String fechaFin = fechaFinEditText.getText().toString();

            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(fechaInicio) || TextUtils.isEmpty(fechaFin)) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            String token = obtenerToken();
            if (token == null) {
                Toast.makeText(getContext(), "No se encontró el token de usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            Viaje nuevoViaje = new Viaje();

            nuevoViaje.setNombre(nombre);
            nuevoViaje.setDescripcion(descripcion);
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date fechaInicioDate = formatoFecha.parse(fechaInicio);
                Date fechaFinDate = formatoFecha.parse(fechaFin);

                nuevoViaje.setFechaInicio(fechaInicioDate);
                nuevoViaje.setFechaFin(fechaFinDate);

                crearViaje(nuevoViaje, token);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al convertir las fechas", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void crearViaje(Viaje viaje, String token) {
        if (token == null) {
            Toast.makeText(getContext(), "No se encontró el token de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un DTO solo con los campos necesarios
        ViajeDTO viajeDTO = new ViajeDTO();
        viajeDTO.setNombre(viaje.getNombre());
        viajeDTO.setDescripcion(viaje.getDescripcion());
        viajeDTO.setFechaInicio(viaje.getFechaInicio());
        viajeDTO.setFechaFin(viaje.getFechaFin());

        // Crear la llamada para crear el viaje, pasando el token en el encabezado Authorization
        Call<ViajeDTO> call = apiService.crearViaje("Bearer " + token, viajeDTO);

        Log.d("CrearViajeFragment", viajeDTO.toString());

        call.enqueue(new Callback<ViajeDTO>() {
            @Override
            public void onResponse(Call<ViajeDTO> call, Response<ViajeDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Viaje creado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("CrearViajeFragment", response.message());
                    Toast.makeText(getContext(), "Error al crear el viaje", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViajeDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String obtenerToken() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("jwt_token", null);  // Recupera el token
    }
}