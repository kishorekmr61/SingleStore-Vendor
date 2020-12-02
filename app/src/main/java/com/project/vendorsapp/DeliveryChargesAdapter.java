package com.project.vendorsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.vendorsapp.Classes.DeliveryCharges;

import java.util.ArrayList;

public class DeliveryChargesAdapter extends RecyclerView.Adapter<DeliveryChargesAdapter.Viewholder> {

    ArrayList<DeliveryCharges>deliverylist;
    Context mcontext;

    public DeliveryChargesAdapter(Context applicationContext, ArrayList<DeliveryCharges> itemlist) {
        this.mcontext=applicationContext;
        this.deliverylist=itemlist;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.deliverycharges_row,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        DeliveryCharges deliveryCharges=deliverylist.get(position);
        holder.txtview_distance.setText("Distance:"+deliveryCharges.getTitle());
        holder.txtview_charge.setText("Rs"+deliveryCharges.getCharges());

    }

    @Override
    public int getItemCount() {
        return deliverylist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_distance,txtview_charge;

        public Viewholder(View itemView) {
            super(itemView);
            txtview_distance=itemView.findViewById(R.id.txtview_distance);
            txtview_charge=itemView.findViewById(R.id.txtview_charges);

        }
    }
}
