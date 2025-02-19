package com.example.travelapps.request;

import com.example.travelapps.models.Foto;
import com.example.travelapps.models.Gasto;
import com.example.travelapps.models.Itinerario;
import com.example.travelapps.models.LoginResponse;
import com.example.travelapps.models.UsuarioLoginDTO;
import com.example.travelapps.models.Viaje;
import com.example.travelapps.models.ViajeDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body UsuarioLoginDTO usuarioDto);

    @GET("api/viajes/viajesuser")
    Call<List<Viaje>> obtenerViajes(@Header("Authorization") String token);

    @GET("api/viajes/{id}")
    Call<Viaje> obtenerViaje(@Path("id") int id);

    @PUT("api/viajes/{id}")
    Call<Viaje> actualizarViaje(@Path("id") int id, @Body Viaje viaje);


    @POST("api/viajes")
    Call<ViajeDTO> crearViaje(@Header("Authorization") String authorization, @Body ViajeDTO viajeDTO);

    @GET("api/viajes/{idViaje}/itinerarios")
    Call<List<Itinerario>> obtenerItinerarios(@Path("idViaje") int idViaje);

    @PUT("api/viajes/{idViaje}/itinerarios/{idItinerario}")
    Call<Void> actualizarItinerario(
            @Path("idViaje") int idViaje,
            @Path("idItinerario") int idItinerario,
            @Body Itinerario itinerario
    );
    @DELETE("api/viajes/{idViaje}/itinerarios/{id}")
    Call<Void> eliminarItinerario(@Path("idViaje") int idViaje, @Path("id") int id);
    @POST("api/viajes/{idViaje}/itinerarios")
    Call<Itinerario> crearItinerario(@Path("idViaje") int idViaje, @Body Itinerario itinerario);

    @GET("api/gastos/{idViaje}")
    Call<List<Gasto>> obtenerGastos(@Path("idViaje") int idViaje, @Header("Authorization") String token);

    @DELETE("api/gastos/{id}")
    Call<Void> eliminarGasto(@Path("id") int gastoId, @Header("Authorization") String token);

    @POST("api/gastos")
    Call<Gasto> crearGasto(@Body Gasto gasto, @Header("Authorization") String token);

    @PUT("api/gastos/{id}")
    Call<Gasto> actualizarGasto(@Path("id") int id, @Body Gasto gasto, @Header("Authorization") String token);


    @GET("api/fotos/{idViaje}")
    Call<List<Foto>> obtenerFotos(@Path("idViaje") int idViaje);


    @DELETE("api/fotos/{id}")
    Call<Void> eliminarFoto(@Path("id") int id);


    @Multipart
    @POST("api/fotos/{idViaje}")
    Call<Foto> subirFoto(@Path("idViaje") int idViaje, @Part MultipartBody.Part archivo, @Part("descripcion") RequestBody descripcion);


}

