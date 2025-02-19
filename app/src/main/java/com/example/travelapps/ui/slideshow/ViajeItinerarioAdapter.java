package com.example.travelapps.ui.slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.R;
import com.example.travelapps.models.Viaje;

import java.util.ArrayList;
import java.util.List;

public class ViajeItinerarioAdapter extends RecyclerView.Adapter<ViajeItinerarioAdapter.ViajeViewHolder> {
    private List<Viaje> viajes = new ArrayList<>();
    private Context context;
    private OnItinerarioButtonClickListener itinerarioButtonClickListener;

    // Interfaz para manejar el clic en el botón "Ver itinerario"
    public interface OnItinerarioButtonClickListener {
        void onItinerarioButtonClick(Viaje viaje);
    }

    public void setViajes(List<Viaje> viajes) {
        this.viajes = viajes;
        notifyDataSetChanged();
    }

    public void setOnItinerarioButtonClickListener(OnItinerarioButtonClickListener listener) {
        this.itinerarioButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viaje_itinerario, parent, false);
        return new ViajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViajeViewHolder holder, int position) {
        Viaje viaje = viajes.get(position);
        holder.nombreTextView.setText(viaje.getNombre());
        holder.descripcionTextView.setText(viaje.getDescripcion());

        // Configurar el botón "Ver itinerario"
        holder.itinerarioButton.setOnClickListener(v -> {
            if (itinerarioButtonClickListener != null) {
                itinerarioButtonClickListener.onItinerarioButtonClick(viaje);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viajes.size();
    }

    static class ViajeViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView;
        Button itinerarioButton;

        ViajeViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreViaje);
            descripcionTextView = itemView.findViewById(R.id.descripcionViaje);
            itinerarioButton = itemView.findViewById(R.id.btnVerItinerario); // Asegúrate de tener este botón en el XML
        }
    }
}