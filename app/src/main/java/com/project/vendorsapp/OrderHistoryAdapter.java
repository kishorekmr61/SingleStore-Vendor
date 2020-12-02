package com.project.vendorsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter <OrderHistoryAdapter.Viewholder>{
    ArrayList<VendorList>list;
    Context mcontext;

    public OrderHistoryAdapter(Context context, ArrayList<VendorList> vendorlist) {
     this.list=vendorlist;
     this.mcontext=context;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.row_orderhistory,viewGroup,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, int position) {

        VendorList data=list.get(position);
        viewholder.txtview_orderid.setText(data.getOrderID());
        viewholder.txtview_date.setText(data.getOrderDate());
        viewholder.txtview_name.setText(data.getCustomerName());
        viewholder.txtview_mobileno.setText(data.getMobileno());
        viewholder.txtview_orderamount.setText(data.getOrderAmount());
        viewholder.txtview_orderstatus.setText(data.getOrderStatus());
        viewholder.txtview_paymenttype.setText(data.getPaymentType());
        viewholder.cardView.setAnimation(AnimationUtils.makeInAnimation(mcontext,true));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_orderid,txtview_date,txtview_name,txtview_mobileno,txtview_orderamount,txtview_orderstatus,txtview_paymenttype;
        CardView cardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            txtview_orderid=itemView.findViewById(R.id.txtview_orderid);
            txtview_date=itemView.findViewById(R.id.txtview_date);
            txtview_name=itemView.findViewById(R.id.txtview_name);
            txtview_mobileno=itemView.findViewById(R.id.txtview_mobileno);
            txtview_orderamount=itemView.findViewById(R.id.txtview_orderamount);
            txtview_orderstatus=itemView.findViewById(R.id.txtview_ordersatatus);
            txtview_paymenttype=itemView.findViewById(R.id.txtview_paymenttype);
            cardView=itemView.findViewById(R.id.cardview);

        }
    }
}
