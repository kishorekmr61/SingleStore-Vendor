package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewDetailsActivity extends AppCompatActivity {
    TextView txtview_name,txtview_orderid,txtview_paymentmode,txtview_address,txtview_totalamount,txtview_dcharges,txtview_discount,txtview_total;
    Button btn_approve,btn_cancel;
    RecyclerView recyclerView;
    Retro retrofits;
    ArrayList<ItemList>itemlist;
    LinearLayoutManager layoutManager;
    ViewDetailAdapter detailAdapter;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    float toatalamount=0;
    ProgressDialog dialog;
    String discountamount="",discountpercent="",deliverycharges="",orderid="",statusid="",vendorid="",branchid="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_viewdetails);
        txtview_name=findViewById(R.id.txtview_name);
        txtview_orderid=findViewById(R.id.txtview_orderid);
        txtview_paymentmode=findViewById(R.id.txtview_paymentmode);
        txtview_address=findViewById(R.id.txtview_address);
        txtview_totalamount=findViewById(R.id.txtview_totalamount);
        txtview_dcharges=findViewById(R.id.txtview_delieverycharges);
        txtview_discount=findViewById(R.id.txtview_discount);
        txtview_total=findViewById(R.id.txtview_total);
        recyclerView=findViewById(R.id.recyclerview_details);
        btn_approve=findViewById(R.id.btn_approve);
        btn_cancel=findViewById(R.id.btn_cancel);
        sp = getSharedPreferences("config_info", 0);
        editor=sp.edit();
        orderid=getIntent().getStringExtra("orderid");
        deliverycharges=getIntent().getStringExtra("dcharges");
        statusid=getIntent().getStringExtra("statusid");
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        retrofits=new Retro();
        itemlist=new ArrayList<>();
        if(statusid.equalsIgnoreCase("3"))
        {
            btn_approve.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(retrofits.isNetworkConnected(getApplicationContext())) {
                    InvokeServiceOrder("6");
                    dialog.show();
                }
            }
        });
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(retrofits.isNetworkConnected(getApplicationContext())) {
                  InvokeServiceOrder("3");
                  dialog.show();
                }
            }
        });
        InvokeService();
    }

        public void InvokeService()
        {
            Retrofit retrofit = retrofits.call();
            ApiService service = retrofit.create(ApiService.class);
            Call<ResponseBody> response =service.GetOrderDetails(orderid);
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        dialog.dismiss();
                        itemlist.clear();
                        String customername="",address="",paymenttype="";
                        String strresponse=response.body().string();
                        JSONArray array=new JSONArray(strresponse);
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject jsonObject=array.getJSONObject(i);
                             customername=jsonObject.getString("CustomerName");
                             address=jsonObject.getString("AddressLine1");
                             paymenttype=jsonObject.getString("PaymentType");
                             discountpercent=jsonObject.getString("DiscountPercent");
                             discountamount=jsonObject.getString("DiscountOnAmountAbove");
                            JSONArray jsonArray=jsonObject.getJSONArray("listorders");
                            for(int j=0;j<jsonArray.length();j++)
                            {
                                JSONObject object=jsonArray.getJSONObject(j);
                                String quantity=object.getString("Quantity");
                                String itemname=object.getString("ItemTitle");
                                String unitprice=object.getString("UnitPrice");
                                String weight=object.getString("itemquantity");
                                float price=Float.parseFloat(quantity)*Float.parseFloat(unitprice);
                                toatalamount=toatalamount+price;
                                itemlist.add(new ItemList(quantity,itemname,unitprice,weight,price));
                            }
                        }
                       txtview_name.setText(customername);
                        txtview_orderid.setText(orderid);
                        txtview_paymentmode.setText(paymenttype);
                        txtview_address.setText(address);
                        layoutManager=new LinearLayoutManager(ViewDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
                        detailAdapter=new ViewDetailAdapter(getApplicationContext(),itemlist);
                        recyclerView.setAdapter(detailAdapter);
                        recyclerView.setLayoutManager(layoutManager);
                        calculate();

                    }

                    catch (IOException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                           dialog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(ViewDetailsActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewDetailsActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    public void calculate()
    {
     String Amount=String.valueOf(toatalamount);
        float discount = 0;
        float amount = 0;
     if(!discountamount.equalsIgnoreCase("null") && !discountpercent.equalsIgnoreCase(null)) {
         float damount = Float.valueOf(discountamount);
         float dpercentage = Float.valueOf(discountpercent);
         discount = 0;
         amount = 0;
         if (toatalamount > damount) {
             discount = (dpercentage / 100) * toatalamount;
         }


     }
        if (deliverycharges.equalsIgnoreCase("null")) {
            deliverycharges = "0";
        }
     amount=toatalamount+Float.valueOf(deliverycharges)-discount;
     txtview_totalamount.setText("Rs. "+Amount);
     txtview_dcharges.setText("Rs. "+deliverycharges);
     txtview_discount.setText("Rs. "+String.valueOf(discount));
     txtview_total.setText("Rs. "+String.valueOf(amount));
    }

    public void InvokeServiceOrder(final String statusid) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GrocOrderStatus(orderid,statusid,vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    String strresponse=response.body().string();
                    if(!strresponse.isEmpty()) {
                        if(statusid.equals("6")) {
                            Toast.makeText(ViewDetailsActivity.this, "Order is cancelled successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ViewDetailsActivity.this, "Order is approved successfully", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(ViewDetailsActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                } catch (IOException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ViewDetailsActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewDetailsActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
