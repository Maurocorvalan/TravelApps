package com.example.travelapps;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapps.models.LoginResponse;
import com.example.travelapps.models.UsuarioLoginDTO;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getToken() {
        return token;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String correo, String contrasena) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        UsuarioLoginDTO usuarioDto = new UsuarioLoginDTO();
        usuarioDto.setCorreo(correo);
        usuarioDto.setContrasena(contrasena);
        Log.d("LoginViewModel", "Enviando solicitud con correo: " + correo + " y contraseña: " + contrasena);

        apiService.login(usuarioDto).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    token.setValue(response.body().getToken());
                } else {

                    errorMessage.setValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Mostrar el error en los logs


                // También puedes mostrar el mensaje de error en la interfaz de usuario si es necesario
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }

        });
    }
}