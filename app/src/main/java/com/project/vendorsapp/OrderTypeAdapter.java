package com.project.vendorsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderTypeAdapter extends RecyclerView.Adapter<OrderTypeAdapter.Viewholder> {

    ArrayList<Datalist>list;
    Context mcontext;
    Retro retrofits;


    public OrderTypeAdapter(Context applicationContext, ArrayList<Datalist> itemlist) {
        this.mcontext=applicationContext;
        this.list=itemlist;
        retrofits=new Retro();

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.ordertype_row,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
       final Datalist data=list.get(position);
       holder.txtview.setText(data.getName());
       holder.imgview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               InvokeService(data.getId());
           }
       });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview;
        ImageView imgview;

        public Viewholder(View itemView) {
            super(itemView);
            txtview=itemView.findViewById(R.id.textview);
            imgview=itemView.findViewById(R.id.imageview);
        }
    }


    public void InvokeService(String serviceid) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.DeleteVendorService(serviceid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    int a=1;
                    if(strresponse.equals("\"SUCCESS\""))
                    {
                        Toast.makeText(mcontext,"Service deleted successfully",Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        Intent intent=new Intent(mcontext,OrderTypeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mcontext.startActivity(intent);

                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(mcontext, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


}
