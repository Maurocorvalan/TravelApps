package com.example.travelapps.request;

import com.example.travelapps.models.LoginResponse;
import com.example.travelapps.models.UsuarioLoginDTO;
import com.example.travelapps.models.Viaje;
import com.example.travelapps.models.ViajeDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body UsuarioLoginDTO usuarioDto);

    @GET("api/viajes")
    Call<List<Viaje>> obtenerViajes(@Header("Authorization") String token);

    @GET("api/viajes/{id}")
    Call<Viaje> obtenerViaje(@Path("id") int id);

    @PUT("api/viajes/{id}")
    Call<Viaje> actualizarViaje(@Path("id") int id, @Body Viaje viaje);


    @POST("api/viajes")
    Call<ViajeDTO> crearViaje(@Header("Authorization") String authorization, @Body ViajeDTO viajeDTO);
}