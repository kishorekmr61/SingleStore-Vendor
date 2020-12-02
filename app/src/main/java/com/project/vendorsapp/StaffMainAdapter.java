package com.project.vendorsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.vendorsapp.Classes.Document;
import com.project.vendorsapp.Classes.Staff;

import java.util.ArrayList;

public class StaffMainAdapter extends RecyclerView.Adapter<StaffMainAdapter.Viewholder> {

    ArrayList<Staff>stafflist;
    Context mcontext;



    public StaffMainAdapter(Context applicationContext, ArrayList<Staff> itemlist) {
        this.mcontext=applicationContext;
        this.stafflist=itemlist;

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.row_staffmain,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Staff staff=stafflist.get(position);

     /*   if(position%2==0)
        {
            holder.cardview_staff.setCardBackgroundColor(mcontext.getResources().getColor(R.color.grey));
        }

        else
        {
            holder.cardview_staff.setCardBackgroundColor(mcontext.getResources().getColor(R.color.lightgrey));
        }*/

        holder.txtview_staffrole.setText(staff.getStaffrole());
        holder.txtview_staffname.setText(staff.getStaffname());
        holder.txtview_staffmobileno.setText("+91"+staff.getStaffmobileno());
        holder.txtview_staffaddress.setText(staff.getStaffaddress());
        holder.txtview_staffreportingto.setText("Reporting to "+staff.getStaffreportingto());


    }

    @Override
    public int getItemCount() {
        return stafflist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_staffrole,txtview_staffname,txtview_staffmobileno,txtview_staffaddress,txtview_staffreportingto;
        CardView cardview_staff;

        public Viewholder(View itemView) {
            super(itemView);

            txtview_staffrole=itemView.findViewById(R.id.txtview_staffrole);
            txtview_staffname=itemView.findViewById(R.id.txtview_staffname);
            txtview_staffmobileno=itemView.findViewById(R.id.txtview_staffmobileno);
            txtview_staffaddress=itemView.findViewById(R.id.txtview_staffaddress);
            txtview_staffreportingto=itemView.findViewById(R.id.txtview_staffreportingto);
            cardview_staff=itemView.findViewById(R.id.carview_staff);

        }
    }
}
