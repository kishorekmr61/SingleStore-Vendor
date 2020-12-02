package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Viewholder> {
    ArrayList<VendorList>vendorlist;
    Context mcontext;
    Retro retrofits;
    SharedPreferences sp;
    String branchid="",vendorid="",statusid="";
    ProgressDialog progressDialog;


    public MainAdapter(Context applicationContext, ArrayList<VendorList> vendorlist) {
        this.mcontext=applicationContext;
        this.vendorlist=vendorlist;
        sp = mcontext.getSharedPreferences("config_info", 0);
        branchid=sp.getString("branchid","");
        vendorid=sp.getString("vendorid","");
        progressDialog=new ProgressDialog(mcontext);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view=LayoutInflater.from(mcontext).inflate(R.layout.row_main,parent,false);
      Viewholder vh=new Viewholder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        final VendorList vendor=vendorlist.get(position);
       holder.txtview_name.setText(vendor.getCustomerName());
       holder.txtview_mobileno.setText(vendor.getMobileno());
       holder.txtview_orderid.setText(vendor.getOrderID());
       holder.txtview_distance.setText(vendor.getDistance());
       holder.txtview_ordertype.setText(vendor.getServiceName());
       if(vendor.getOrderStatusID().equalsIgnoreCase("3"))
       {

          holder.btn_packed.setVisibility(View.VISIBLE);
          statusid="4";
       }
       else if(vendor.getOrderStatusID().equalsIgnoreCase("4"))
       {
           holder.btn_packed.setVisibility(View.GONE);
           holder.imgview_details.setVisibility(View.GONE);
           if(!vendor.DeliveryAssigned.equals(""))
           {
               holder.txtview_assignedto.setVisibility(View.VISIBLE);
               holder.btn_assign.setVisibility(View.GONE);
               holder.btn_track.setVisibility(View.VISIBLE);
               holder.txtview_assignedto.setText(vendor.DeliveryAssigned);
           }
           else
           {
               holder.txtview_assignedto.setVisibility(View.GONE);
               holder.btn_assign.setVisibility(View.VISIBLE);
           }
           //holder.btn_packed.setVisibility(View.VISIBLE);
          // holder.btn_packed.setText("Assign");
          // statusid="5";
       }
       else if(vendor.getOrderStatusID().equalsIgnoreCase("5"))
       {
           holder.imgview_details.setVisibility(View.GONE);
       }
       holder.imgview_details.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(mcontext,ViewDetailsActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("orderid",vendor.getOrderID());
               intent.putExtra("statusid",vendor.getOrderStatusID());
               intent.putExtra("dcharges",vendor.getDeliveryCharages());
               mcontext.startActivity(intent);
           }
       });

       holder.btn_packed.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               progressDialog.show();
               InvokeServiceOrder(vendor.OrderID,statusid);

           }
       });

       holder.btn_assign.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(mcontext,DelieveryAssignActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("customername",vendor.getCustomerName());
               intent.putExtra("mobileno",vendor.getMobileno());
               intent.putExtra("orderid",vendor.getOrderID());
               intent.putExtra("ordertype",vendor.getServiceName());
               mcontext.startActivity(intent);
           }
       });

       holder.btn_track.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email=vendor.getDeliveryboyemail();
               String firebaseid=vendor.getDeliverboyfirebaseid();
               String mobileno=vendor.getDeliveryboymobileno();
             Intent intent=new Intent(mcontext,TrackerActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             intent.putExtra("email",email);
             intent.putExtra("firebaseid",firebaseid);
             intent.putExtra("mobileno",mobileno);
               intent.putExtra("dboyname",vendor.DeliveryAssigned);
             intent.putExtra("latitude",vendor.getLatitude());
             intent.putExtra("longitude",vendor.getLongitude());
             mcontext.startActivity(intent);
           }
       });
    }


    @Override
    public int getItemCount() {
        return vendorlist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView txtview_name,txtview_mobileno,txtview_orderid,txtview_distance,txtview_ordertype,txtview_assignedto;
        ImageView imgview_details;
        Button btn_packed,btn_assign,btn_track;
        public Viewholder(View itemView) {
            super(itemView);
            txtview_name=itemView.findViewById(R.id.txtview_name);
            txtview_mobileno=itemView.findViewById(R.id.txtview_mobileno);
            txtview_orderid=itemView.findViewById(R.id.txtview_orderid);
            txtview_distance=itemView.findViewById(R.id.txtview_distance);
            txtview_ordertype=itemView.findViewById(R.id.txtview_ordertype);
            imgview_details=itemView.findViewById(R.id.imgview_details);
            btn_packed= itemView.findViewById(R.id.btn_packed);
            btn_assign=itemView.findViewById(R.id.btn_assign);
            txtview_assignedto=itemView.findViewById(R.id.txtview_assignedto);
            btn_track=itemView.findViewById(R.id.btn_track);
        }
    }

    public void InvokeServiceOrder(String orderid,String statusid) {
        retrofits=new Retro();
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GrocOrderStatus(orderid,statusid,vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strresponse=response.body().string();
                    Toast.makeText(mcontext,"Order is packed successfully!!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);


                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(mcontext, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
