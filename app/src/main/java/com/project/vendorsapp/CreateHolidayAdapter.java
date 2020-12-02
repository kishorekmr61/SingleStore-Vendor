package com.project.vendorsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.vendorsapp.Classes.DeliveryCharges;
import com.project.vendorsapp.Classes.Holiday;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateHolidayAdapter extends RecyclerView.Adapter<CreateHolidayAdapter.Viewholder> {

    ArrayList<Holiday>holidaylist;
    Context mcontext;
    Retro retrofits;

    public CreateHolidayAdapter(Context applicationContext, ArrayList<Holiday> itemlist) {
        this.mcontext=applicationContext;
        this.holidaylist=itemlist;
        retrofits=new Retro();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.createholiday_row,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        final Holiday holiday=holidaylist.get(position);
        holder.txtview_description.setText(holiday.getHolidayDescription());
        holder.txtview_date.setText(holiday.getHolidayDate());
        holder.imgview_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvokeService(holiday.getHolidayID());
            }
        });


    }

    @Override
    public int getItemCount() {
        return holidaylist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_description,txtview_date;
        ImageView imgview_delete;

        public Viewholder(View itemView) {
            super(itemView);
            txtview_description=itemView.findViewById(R.id.txtview_description);
            txtview_date=itemView.findViewById(R.id.txtview_date);
            imgview_delete=itemView.findViewById(R.id.imgview_delete);

        }
    }

    public void InvokeService(String holidayid) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.DeleteHoliday(holidayid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    try {
                        String strresponse=response.body().string();
                        if(strresponse.equals("\"SUCCESS\""))
                        {
                            Toast.makeText(mcontext,"Holiday deleted successfully!!",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(mcontext,CreateHolidaysActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            mcontext.startActivity(intent);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}

