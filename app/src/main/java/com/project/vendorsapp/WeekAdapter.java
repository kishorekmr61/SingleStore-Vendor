package com.project.vendorsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WeekAdapter extends RecyclerView.Adapter <WeekAdapter.Viewholder>{
    ArrayList<WeekDays>list;
    Context mcontext;

    public WeekAdapter(Context context, ArrayList<WeekDays> weeklist) {
     this.list=weeklist;
     this.mcontext=context;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.week_row,viewGroup,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, int position) {
        final WeekDays days=list.get(position);

        if(days.id.equalsIgnoreCase("0")) {
            viewholder.txtview_name.setTextColor(mcontext.getResources().getColor(R.color.dimblack));
            viewholder.lnrlayout.setBackgroundColor(mcontext.getResources().getColor(R.color.white));

        }
        else if(days.id.equalsIgnoreCase("1"))
        {
            viewholder.txtview_name.setTextColor(mcontext.getResources().getColor(R.color.white));
            viewholder.lnrlayout.setBackgroundColor(mcontext.getResources().getColor(R.color.darkorange));



        }

        viewholder.lnrlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(days.id.equalsIgnoreCase("0")) {
                    viewholder.txtview_name.setTextColor(mcontext.getResources().getColor(R.color.white));
                    viewholder.lnrlayout.setBackgroundColor(mcontext.getResources().getColor(R.color.darkorange));
                    days.setId("1");
                }
                else if(days.id.equalsIgnoreCase("1"))
                {
                    viewholder.txtview_name.setTextColor(mcontext.getResources().getColor(R.color.dimblack));
                    viewholder.lnrlayout.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                    days.setId("0");

                }

            }
        });

        viewholder.txtview_name.setText(days.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_name;
        LinearLayout lnrlayout;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txtview_name=itemView.findViewById(R.id.txtview_name);
            lnrlayout=itemView.findViewById(R.id.lnrlayout);
        }
    }
}
