package com.example.travelapps.ui.gastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.MainActivity;
import com.example.travelapps.R;
import com.example.travelapps.models.Gasto;

import java.util.ArrayList;
import java.util.List;

public class GastosAdapter extends RecyclerView.Adapter<GastosAdapter.GastoViewHolder> {
    private List<Gasto> gastos = new ArrayList<>();
    private final OnGastoDeleteListener onGastoDeleteListener;

    public GastosAdapter(OnGastoDeleteListener onGastoDeleteListener) {
        this.onGastoDeleteListener = onGastoDeleteListener;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = gastos.get(position);
        holder.categoriaTextView.setText(gasto.getCategoria());
        holder.montoTextView.setText(String.valueOf(gasto.getMonto()));
        holder.descripcionTextView.setText(gasto.getDescripcion());
        holder.btnEditar.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("idGasto", gasto.getIdGasto());
            bundle.putString("categoria", gasto.getCategoria());
            bundle.putDouble("monto", gasto.getMonto());
            bundle.putString("descripcion", gasto.getDescripcion());

            NavController navController = Navigation.findNavController(holder.itemView);
            navController.navigate(R.id.nav_editar_gasto, bundle);
        });
        holder.btnEliminar.setOnClickListener(v -> {
            onGastoDeleteListener.onGastoDelete(gasto.getIdGasto());
        });
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    static class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView categoriaTextView, montoTextView, descripcionTextView;
        Button btnEditar, btnEliminar;

        GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriaTextView = itemView.findViewById(R.id.textViewCategoria);
            montoTextView = itemView.findViewById(R.id.textViewMonto);
            descripcionTextView = itemView.findViewById(R.id.textViewDescripcion);
            btnEditar = itemView.findViewById(R.id.btnEditarGasto);
            btnEliminar = itemView.findViewById(R.id.btnEliminarGasto);
        }
    }

    public interface OnGastoDeleteListener {
        void onGastoDelete(int idGasto);
    }
}