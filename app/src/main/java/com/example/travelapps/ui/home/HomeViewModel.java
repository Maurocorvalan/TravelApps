package com.example.travelapps.ui.home;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapps.models.Viaje;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Viaje>> viajesLiveData = new MutableLiveData<>();
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<String> getError() {
        return errorLiveData;
    }


    public HomeViewModel(Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("AppPrefs", Application.MODE_PRIVATE);
        cargarViajes();
    }

    public LiveData<List<Viaje>> getViajes() {
        return viajesLiveData;
    }

    private void cargarViajes() {
        String token = sharedPreferences.getString("jwt_token", null);
        if (token == null) {
            return; // No hay token, no se pueden obtener los viajes
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Viaje>> call = apiService.obtenerViajes("Bearer " + token);

        call.enqueue(new Callback<List<Viaje>>() {
            @Override
            public void onResponse(Call<List<Viaje>> call, Response<List<Viaje>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    viajesLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Viaje>> call, Throwable t) {
                errorLiveData.setValue("Error al obtener los viajes: " + t.getMessage());
                Log.e("API Error", "Error al obtener los viajes", t);

            }
        });
    }
}