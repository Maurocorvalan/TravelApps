package com.example.travelapps.ui.gallery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelapps.R;
import com.example.travelapps.models.Foto;
import java.util.ArrayList;
import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoViewHolder> {
    private List<Foto> fotos = new ArrayList<>();
    private OnFotoDeleteListener deleteListener;

    public interface OnFotoDeleteListener {
        void onFotoDelete(Foto foto);
    }

    public void setOnFotoDeleteListener(OnFotoDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
        notifyDataSetChanged();
    }
    public void eliminarFoto(Foto foto) {
        fotos.remove(foto);
        notifyDataSetChanged(); // Notifica que los datos han cambiado
    }
    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        Foto foto = fotos.get(position);
        String BASE_URL = "http://10.0.2.2:5173/";
        String url = BASE_URL + foto.getUrl();

        // 🔹 Agrega este Log para ver la URL en la consola
        Log.d("salida", url);
        Glide.with(holder.imageView.getContext())
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imageView);

        holder.btnEliminar.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onFotoDelete(foto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button btnEliminar;

        public FotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewFoto);
            btnEliminar = itemView.findViewById(R.id.btnEliminarFoto);
        }
    }
}
