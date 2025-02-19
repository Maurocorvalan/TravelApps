package com.example.travelapps.ui.home;

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

public class ViajeAdapter extends RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder> {
    private List<Viaje> viajes = new ArrayList<>();

    private Context context;
    private OnEditButtonClickListener editButtonClickListener;

    // Interfaz para manejar el clic en el botón "Editar"
    public interface OnEditButtonClickListener {
        void onEditButtonClick(Viaje viaje);
    }

    public void setViajes(List<Viaje> viajes) {
        this.viajes = viajes;
        notifyDataSetChanged();
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viaje, parent, false);
        return new ViajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViajeViewHolder holder, int position) {
        Viaje viaje = viajes.get(position);
        holder.nombreTextView.setText(viaje.getNombre());
        holder.descripcionTextView.setText(viaje.getDescripcion());

        // Configurar el botón de editar
        holder.editarButton.setOnClickListener(v -> {
            if (editButtonClickListener != null) {
                editButtonClickListener.onEditButtonClick(viaje);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viajes.size();
    }

    static class ViajeViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView;
        Button editarButton;

        ViajeViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreViaje);
            descripcionTextView = itemView.findViewById(R.id.descripcionViaje);
            editarButton = itemView.findViewById(R.id.btnEditarViaje); // Asegúrate de tener este botón en el XML
        }
    }
}