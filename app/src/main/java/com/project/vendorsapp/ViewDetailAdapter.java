package com.project.vendorsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewDetailAdapter extends RecyclerView.Adapter<ViewDetailAdapter.Viewholder> {

    ArrayList<ItemList>list;
    Context mcontext;

    public ViewDetailAdapter(Context applicationContext, ArrayList<ItemList> itemlist) {
        this.mcontext=applicationContext;
        this.list=itemlist;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.row_vendordetail,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        ItemList itemList=list.get(position);
        String price="Rs. "+String.valueOf(itemList.getPrice());
        holder.txtview_itemname.setText(itemList.getItemname());
        holder.txtview_itemquantity.setText(itemList.getQuantity());
        holder.txtview_itemweight.setText(itemList.getWeight());
        holder.txtview_itemprice.setText(price);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_itemname,txtview_itemquantity,txtview_itemweight,txtview_itemprice;

        public Viewholder(View itemView) {
            super(itemView);
            txtview_itemname=itemView.findViewById(R.id.txtview_itemname);
            txtview_itemquantity=itemView.findViewById(R.id.txtview_itemquantity);
            txtview_itemweight=itemView.findViewById(R.id.txtview_itemweight);
            txtview_itemprice=itemView.findViewById(R.id.txtview_itemprice);
        }
    }
}
