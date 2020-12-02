package com.project.vendorsapp.Notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.project.vendorsapp.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private ArrayList<NotificationMessage> messagelist = new ArrayList<>();
    public Context mcontext;

    public MessageAdapter(Context context, ArrayList<NotificationMessage> list) {
      this.mcontext= context;
      this.messagelist=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view=LayoutInflater.from(mcontext).inflate(R.layout.notification_msg_row,parent,false);
     ViewHolder vh=new ViewHolder(view);
     return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationMessage notificationMessage=messagelist.get(position);
        holder.txtview_title.setText(notificationMessage.getTitle());
        holder.txtview_message.setText(notificationMessage.getNotificationmessage());
        holder.txtview_time.setText(notificationMessage.getTime());
        holder.txtview_date.setText(notificationMessage.getDate());
        Glide.with(mcontext).load(R.mipmap.molslogo).into(holder.imgview);

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtview_title,txtview_message,txtview_time,txtview_date;
        ImageView imgview;
        public ViewHolder(View itemView) {
            super(itemView);
            txtview_title=itemView.findViewById(R.id.txtview_title);
            txtview_message=itemView.findViewById(R.id.txtview_message);
            txtview_time=itemView.findViewById(R.id.txtview_time);
            txtview_date=itemView.findViewById(R.id.txtview_date);
            imgview=itemView.findViewById(R.id.imgview);

        }
    }
}
