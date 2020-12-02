package com.project.vendorsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

public class GetServiceAdapter extends RecyclerView.Adapter<GetServiceAdapter.Viewholder> {

    ArrayList<Datalist>list;
    Context mcontext;
    Retro retrofits;


    public GetServiceAdapter(Context applicationContext, ArrayList<Datalist> itemlist) {
        this.mcontext=applicationContext;
        this.list=itemlist;
        retrofits=new Retro();

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.getservice_row,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
      final Datalist data=list.get(position) ;
      holder.checkBox.setText(data.getMessage());
      holder.checkBox.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(data.getMessageactive().equalsIgnoreCase("false"))
              {
                 data.setMessageactive("true");
              }

              else
              {
                 data.setMessageactive("false");
              }
          }
      });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
      CheckBox checkBox;

        public Viewholder(View itemView) {
            super(itemView);
           checkBox=itemView.findViewById(R.id.checkbox);
        }
    }

    public void InvokeService(String messageid, String currentmessageid) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.UpdateStoreMessages(messageid,currentmessageid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                   if(Integer.parseInt(strresponse)>0)
                   {
                       Toast.makeText(mcontext,"Message has been set as store message",Toast.LENGTH_SHORT).show();
                       Intent intent=new Intent(mcontext,StoreMessageActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       mcontext.startActivity(intent);

                   }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void InvokeService(String messageid) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.DeleteStoreMessages(messageid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(strresponse.equals("\"SUCCESS\""))
                    {
                        Toast.makeText(mcontext,"Message deleted successfully",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(mcontext,StoreMessageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    Toast.makeText(mcontext,mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


}
