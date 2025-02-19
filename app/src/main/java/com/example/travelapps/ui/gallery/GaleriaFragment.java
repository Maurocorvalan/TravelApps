package com.example.travelapps.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelapps.R;
import com.example.travelapps.models.Foto;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GaleriaFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;


    private RecyclerView recyclerView;
    private FotoAdapter fotoAdapter;
    private int idViaje;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galeria, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fotoAdapter = new FotoAdapter();
        recyclerView.setAdapter(fotoAdapter);
        fotoAdapter.setOnFotoDeleteListener(foto -> eliminarFoto(foto));

        FloatingActionButton fabAddImage = view.findViewById(R.id.fabAddImage);
        // Configura el botón flotante
        fabAddImage.setOnClickListener(v -> {
            // Lógica para abrir la galería
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
        });




        if (getArguments() != null) {
            idViaje = getArguments().getInt("idViaje");
            obtenerFotos(idViaje);
        }

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            // Aquí puedes subir la imagen
            subirFoto(idViaje, imageUri);
        }
    }
    private void subirFoto(int idViaje, Uri imageUri) {
        // Verifica que el Uri no sea nulo
        if (imageUri == null) {
            Toast.makeText(getContext(), "Error: la imagen no está disponible.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Usa un ContentResolver para abrir un InputStream
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            // Crear un archivo temporal o procesar el InputStream como desees
            // Ejemplo: convertir InputStream a un File (puedes necesitar permisos de almacenamiento)
            File file = new File(getContext().getCacheDir(), "temp_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

            // Ahora puedes proceder a subir el archivo
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("archivo", file.getName(), requestFile);
            RequestBody descripcion = RequestBody.create(MediaType.parse("text/plain"), "Descripción de la imagen");

            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Call<Foto> call = apiService.subirFoto(idViaje, body, descripcion);

            call.enqueue(new Callback<Foto>() {
                @Override
                public void onResponse(Call<Foto> call, Response<Foto> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "La foto se subió con éxito.", Toast.LENGTH_SHORT).show();
                        obtenerFotos(idViaje);
                    } else {
                        Toast.makeText(getContext(), "Error al subir la imagen.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Foto> call, Throwable t) {
                    Log.d("error", call.toString() + " error t " + t.toString());
                    Toast.makeText(getContext(), "Error en la solicitud.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: archivo no encontrado.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al leer el archivo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerFotos(int idViaje) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Foto>> call = apiService.obtenerFotos(idViaje);

        call.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fotoAdapter.setFotos(response.body());
                    Log.d("salida", response.toString());
                } else {
                    Toast.makeText(getContext(), "No se pudieron cargar las fotos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener fotos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void eliminarFoto(Foto foto) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.eliminarFoto(foto.getIdFoto());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fotoAdapter.eliminarFoto(foto);
                    Log.d("fotosi",response.toString());
                    Toast.makeText(getContext(), "Foto eliminada correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("salida",response.toString());
                    Toast.makeText(getContext(), "Error al eliminar la foto.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la solicitud.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
