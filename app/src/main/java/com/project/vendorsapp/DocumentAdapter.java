package com.project.vendorsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.vendorsapp.Classes.DeliveryCharges;
import com.project.vendorsapp.Classes.Document;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.Viewholder> {

    ArrayList<Document>documentlist;
    Context mcontext;



    public DocumentAdapter(Context applicationContext, ArrayList<Document> itemlist) {
        this.mcontext=applicationContext;
        this.documentlist=itemlist;

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.document_row,parent,false);
        Viewholder vh=new Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
       final Document document=documentlist.get(position);
       holder.txtview_doctitle.setText(document.getTitle());
       holder.imgview_view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              Intent intent=new Intent(mcontext,DocumentActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
              intent.putExtra("path",document.getImagepath());
              mcontext.startActivity(intent);
           }
       });


    }

    @Override
    public int getItemCount() {
        return documentlist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_doctitle;
        ImageView imgview_view;
        public Viewholder(View itemView) {
            super(itemView);
            txtview_doctitle=itemView.findViewById(R.id.txtview_doctitle);
            imgview_view=itemView.findViewById(R.id.imgview_view);
        }
    }
}
