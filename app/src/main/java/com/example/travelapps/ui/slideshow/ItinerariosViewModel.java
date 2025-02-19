package com.example.travelapps.ui.slideshow;

import android.app.Application;
import android.util.Log;  // Importar Log para la depuración
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.travelapps.models.Itinerario;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItinerariosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Itinerario>> itinerariosLiveData = new MutableLiveData<>();

    public ItinerariosViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Itinerario>> getItinerarios() {
        return itinerariosLiveData;
    }

    public void cargarItinerarios(int idViaje) {
        Log.d("ItinerariosViewModel", "cargarItinerarios: Iniciando solicitud para idViaje = " + idViaje);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Itinerario>> call = apiService.obtenerItinerarios(idViaje);

        Log.d("ItinerariosViewModel", "cargarItinerarios: Realizando solicitud a la API");

        call.enqueue(new Callback<List<Itinerario>>() {
            @Override
            public void onResponse(Call<List<Itinerario>> call, Response<List<Itinerario>> response) {
                // Log para ver el código de estado HTTP
                Log.d("ItinerariosViewModel", "Código de respuesta: " + response.code());
                // Log para ver el cuerpo de la respuesta
                Log.d("ItinerariosViewModel", "Cuerpo de la respuesta: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ItinerariosViewModel", "onResponse: Respuesta exitosa, itinerarios recibidos: " + response.body().size());
                    itinerariosLiveData.setValue(response.body());
                } else {
                    Log.d("ItinerariosViewModel", "onResponse: Respuesta no exitosa o cuerpo vacío");
                }
            }

            @Override
            public void onFailure(Call<List<Itinerario>> call, Throwable t) {
                Log.e("ItinerariosViewModel", "onFailure: Error en la solicitud: " + t.getMessage(), t);
            }
        });
    }
}
