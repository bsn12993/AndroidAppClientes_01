package com.bryansilverio.serviciorest.com.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bryansilverio.serviciorest.R;
import com.bryansilverio.serviciorest.com.models.Cliente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bryan Silverio on 18/12/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ClienteViewHolder>{

    private List<Cliente> clientes;
    public List<Integer> integerList=null;
    public int idSelected=0;

    public RVAdapter(){}

    public RVAdapter(List<Cliente> clientes) {
        this.clientes=clientes;
    }

    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        ClienteViewHolder cvh=new ClienteViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final ClienteViewHolder holder, final int position) {
        integerList=new ArrayList<>();
        holder.cv.setTag(this.clientes.get(position).getId());
        holder.nombre.setText(this.clientes.get(position).getNombre());
        holder.telefono.setText(this.clientes.get(position).getTelefono());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    integerList.add(Integer.parseInt(holder.cv.getTag().toString()));
                    idSelected=Integer.parseInt(holder.cv.getTag().toString());
                }else {
                    integerList.remove(position);
                    idSelected=0;
                    holder.checkBox.setChecked(false);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return this.clientes.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder{

        private CardView cv;
        private TextView nombre;
        private TextView telefono;
        private ImageView foto;
        private CheckBox checkBox;
        private TextView Id;

        public ClienteViewHolder(View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cv);
            nombre=(TextView)itemView.findViewById(R.id.nombre);
            telefono=(TextView)itemView.findViewById(R.id.telefono);
            foto=(ImageView)itemView.findViewById(R.id.foto);
            checkBox=(CheckBox)itemView.findViewById(R.id.chk);
        }
    }
}
