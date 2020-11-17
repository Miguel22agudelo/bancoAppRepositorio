package com.example.bancoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class transferAdapter extends RecyclerView.Adapter<transferAdapter.transferViewHolder>{
    ArrayList<transferencia> listadoTransferencias;

    public transferAdapter(ArrayList<transferencia> listadoTransferencias){
        this.listadoTransferencias = listadoTransferencias;
    }

    @NonNull
    @Override
    public transferAdapter.transferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transferencias,null,false);
        return new transferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull transferAdapter.transferViewHolder holder, int position) {
        holder.numeroCuentaDestino.setText(listadoTransferencias.get(position).getCuentaDestino());
        holder.hora.setText(listadoTransferencias.get(position).getHora());
        holder.fecha.setText(listadoTransferencias.get(position).getFecha());
        holder.valor.setText(listadoTransferencias.get(position).getValor());
    }

    @Override
    public int getItemCount() {
        return listadoTransferencias.size();
    }

    public class transferViewHolder extends RecyclerView.ViewHolder {
        TextView numeroCuentaDestino, hora, fecha, valor;

        public transferViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroCuentaDestino = itemView.findViewById(R.id.etCuentaDestino);
            hora = itemView.findViewById(R.id.etHoraTransferencia);
            fecha = itemView.findViewById(R.id.etFechaTransferencia);
            valor = itemView.findViewById(R.id.etValorTransferencia);
        }
    }
}
