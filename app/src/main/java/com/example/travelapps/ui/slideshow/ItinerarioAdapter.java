package com.example.travelapps.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelapps.R;
import com.example.travelapps.models.Itinerario;
import com.example.travelapps.request.ApiClient;
import com.example.travelapps.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItinerarioAdapter extends RecyclerView.Adapter<ItinerarioAdapter.ViewHolder> {

    private List<Itinerario> itinerarios;
    private Context context;
    private OnItinerarioClickListener listener;

    public interface OnItinerarioClickListener {
        void onItinerarioClick(Itinerario itinerario);
    }
    public ItinerarioAdapter(Context context, List<Itinerario> itinerarios) {
        this.context = context;
        this.itinerarios = itinerarios;
    }

    public void setItinerarios(List<Itinerario> itinerarios) {
        this.itinerarios = itinerarios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itinerario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Itinerario itinerario = itinerarios.get(position);

        holder.actividad.setText(itinerario.getActividad());
        holder.ubicacion.setText(itinerario.getUbicacion());

        // Botón Editar
        holder.btnEditar.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("idViaje", itinerario.getIdViaje());
            bundle.putInt("idItinerario", itinerario.getIdItinerario());
            bundle.putString("actividad", itinerario.getActividad());
            bundle.putString("ubicacion", itinerario.getUbicacion());

            NavController navController = Navigation.findNavController((FragmentActivity) context, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_edit_itinerario, bundle);
        });

        // Botón Eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            eliminarItinerario(itinerario.getIdViaje(), itinerario.getIdItinerario());

            // Aquí puedes hacer una llamada a la API para eliminar el itinerario
            // Y luego actualizar la lista
        });
    }

    @Override
    public int getItemCount() {
        return (itinerarios != null) ? itinerarios.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView actividad, ubicacion;
        Button btnEditar, btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            actividad = itemView.findViewById(R.id.actividad);
            ubicacion = itemView.findViewById(R.id.ubicacion);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
    private void eliminarItinerario(int idViaje, int idItinerario) {
        // Usa el ApiClient para obtener la instancia de Retrofit
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Llama al servicio para eliminar el itinerario
        Call<Void> call = apiService.eliminarItinerario(idViaje, idItinerario);

        // Realiza la llamada en segundo plano
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Itinerario eliminado con exito", Toast.LENGTH_SHORT).show();

                    // Si la respuesta es exitosa, elimina el itinerario de la lista y actualiza la vista
                    itinerarios.removeIf(itinerario -> itinerario.getIdItinerario() == idItinerario);
                    notifyDataSetChanged();
                    // Puedes mostrar un mensaje de éxito aquí
                } else {
                    // Maneja los errores de la API
                    Toast.makeText(context, "Error al eliminar el itinerario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Maneja fallos de la red
                Log.d("salida", t.getMessage() + call.toString());
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


}