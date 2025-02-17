package com.example.travelapps.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.travelapps.R;
import com.example.travelapps.models.Viaje;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditViajeFragment extends Fragment {
    private EditText nombreEditText, descripcionEditText, fechaInicioEditText, fechaFinEditText;
    private Button saveButton;
    private Viaje viaje;

    private ApiService apiService;

    public EditViajeFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_viaje, container, false);

        // Inicializar Retrofit y el servicio de API
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        // Obtener el objeto viaje del Bundle
        if (getArguments() != null) {
            viaje = (Viaje) getArguments().getSerializable("viaje");
        }

        // Inicializar las vistas
        nombreEditText = root.findViewById(R.id.nombreEditText);
        descripcionEditText = root.findViewById(R.id.descripcionEditText);
        fechaInicioEditText = root.findViewById(R.id.fechaInicioEditText);
        fechaFinEditText = root.findViewById(R.id.fechaFinEditText);
        saveButton = root.findViewById(R.id.saveButton);

        // Rellenar los campos con los datos del viaje
        if (viaje != null) {
            nombreEditText.setText(viaje.getNombre());
            descripcionEditText.setText(viaje.getDescripcion());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            if (viaje.getFechaInicio() != null) {
                fechaInicioEditText.setText(dateFormat.format(viaje.getFechaInicio()));
            }
            if (viaje.getFechaFin() != null) {
                fechaFinEditText.setText(dateFormat.format(viaje.getFechaFin()));
            }
        }

        // Configurar el botón de guardar
        saveButton.setOnClickListener(v -> guardarCambios());

        return root;
    }

    private void guardarCambios() {
        String nombre = nombreEditText.getText().toString();
        String descripcion = descripcionEditText.getText().toString();
        String fechaInicioStr = fechaInicioEditText.getText().toString();
        String fechaFinStr = fechaFinEditText.getText().toString();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(fechaInicioStr) || TextUtils.isEmpty(fechaFinStr)) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir String a Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date fechaInicio = dateFormat.parse(fechaInicioStr);
            Date fechaFin = dateFormat.parse(fechaFinStr);

            // Actualizar los datos del viaje
            viaje.setIdViaje(viaje.getIdViaje());
            viaje.setNombre(nombre);
            viaje.setDescripcion(descripcion);
            viaje.setFechaInicio(fechaInicio);
            viaje.setFechaFin(fechaFin);
            Log.d("Guardar Cambios", "ID: " + viaje.getIdViaje() + ", Nombre: " + viaje.getNombre() + ", Descripción: " + viaje.getDescripcion() +
                    ", Fecha Inicio: " + fechaInicio + ", Fecha Fin: " + fechaFin);
            actualizarViaje(viaje);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Error en el formato de fecha. Usa yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void actualizarViaje(Viaje viaje) {
        // Realizar la llamada para actualizar el viaje en la base de datos
        Call<Viaje> call = apiService.actualizarViaje(viaje.getIdViaje(), viaje);
        call.enqueue(new Callback<Viaje>() {
            @Override
            public void onResponse(Call<Viaje> call, Response<Viaje> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Viaje actualizado correctamente", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack(); // Regresar al fragmento anterior
                } else {
                    // Registra detalles más específicos si la respuesta no es exitosa
                    Log.d("Error actualizar viaje", "Código de estado: " + response.code());

                    // Intenta obtener el mensaje de error en el cuerpo de la respuesta (si existe)
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
                        Log.d("Error actualizar viaje", "Mensaje de error: " + errorMessage);
                    } catch (IOException e) {
                        Log.e("Error actualizar viaje", "No se pudo leer el mensaje de error", e);
                    }

                    // Muestra un mensaje más detallado en la interfaz
                    Toast.makeText(getContext(), "Error al actualizar el viaje. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Viaje> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                Log.e("Error actualizar viaje", "Error en la llamada a la API", t);
            }
        });
    }
}
