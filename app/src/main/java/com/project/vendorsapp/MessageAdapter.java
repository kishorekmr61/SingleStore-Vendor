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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Viewholder> {

    ArrayList<Datalist>list;
    Context mcontext;
    Retro retrofits;
    String currentid="";

    public MessageAdapter(Context applicationContext, ArrayList<Datalist> itemlist) {
        this.mcontext=applicationContext;
        this.list=itemlist;
        retrofits=new Retro();

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.message_row,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        final Datalist data=list.get(position);
       holder.txtview_message.setText(data.getMessage());
       if(data.Messageactive.equalsIgnoreCase("true"))
       {
           currentid=data.Messageid;
           holder.imgview_delete.setVisibility(View.GONE);
           holder.btn_showthis.setText("Store message");
       }
       holder.btn_showthis.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!currentid.equalsIgnoreCase(""))
               {
                   InvokeService(data.getMessageid(),currentid);
               }
               else
               {
                   InvokeService(data.getMessageid(),data.getMessageid());
               }


           }
       });

       holder.imgview_delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               InvokeService(data.getMessageid());
           }
       });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_message;
        Button btn_showthis;
        ImageView imgview_delete;

        public Viewholder(View itemView) {
            super(itemView);
            txtview_message=itemView.findViewById(R.id.text_message);
            btn_showthis=itemView.findViewById(R.id.btn_showthis);
            imgview_delete=itemView.findViewById(R.id.imgview_delete);
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
