package com.example.travelapps.ui.gallery;

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

public class ViajeGalleryAdapter extends RecyclerView.Adapter<ViajeGalleryAdapter.ViajeViewHolder> {
    private List<Viaje> viajes = new ArrayList<>();
    private OnGalleryButtonClickListener galleryButtonClickListener;

    public interface OnGalleryButtonClickListener {
        void onGalleryButtonClick(Viaje viaje);
    }

    public void setViajes(List<Viaje> viajes) {
        this.viajes = viajes;
        notifyDataSetChanged();
    }

    public void setOnGalleryButtonClickListener(OnGalleryButtonClickListener listener) {
        this.galleryButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viaje_gallery, parent, false);
        return new ViajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViajeViewHolder holder, int position) {
        Viaje viaje = viajes.get(position);
        holder.nombreTextView.setText(viaje.getNombre());
        holder.descripcionTextView.setText(viaje.getDescripcion());

        // Configurar el botón "Mirar galería"
        holder.galleryButton.setOnClickListener(v -> {
            if (galleryButtonClickListener != null) {
                galleryButtonClickListener.onGalleryButtonClick(viaje);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viajes.size();
    }

    static class ViajeViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView;
        Button galleryButton;

        ViajeViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreViaje);
            descripcionTextView = itemView.findViewById(R.id.descripcionViaje);
            galleryButton = itemView.findViewById(R.id.btnVerGaleria); // Asegúrate de tener este botón en el XML
        }
    }
}
