package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
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

public class OrderHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SharedPreferences sp;
    String vendorid="",branchid="",vendorname;
    Retro retrofits;
    Toolbar toolbar;
    ArrayList<VendorList> vendorlist;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);
        recyclerView=findViewById(R.id.recyclerview);
        toolbar=findViewById(R.id.toolbar);
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        vendorname=sp.getString("vendorname","");
        vendorlist=new ArrayList<>();
        retrofits=new Retro();
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        configuretoolbar();
        if(retrofits.isNetworkConnected(getApplicationContext())) {
            dialog.show();
            InvokeService();
        }
    }

    public void InvokeService() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetOrders(vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    String strresponse=response.body().string();
                    vendorlist.clear();
                    JSONArray array=new JSONArray(strresponse);
                    for(int i=0;i<array.length();i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String CustomerID = jsonObject.getString("CustomerID");
                        String CustomerName = jsonObject.getString("CustomerName");
                        String Latitude = jsonObject.getString("Latitude");
                        String Longitude = jsonObject.getString("Longitude");
                        String Distance = jsonObject.getString("Distance");
                        String Mobileno = jsonObject.getString("MobileNo");
                        String PurchaseTypeID = jsonObject.getString("PurchaseTypeID");
                        String OrderStatusID = jsonObject.getString("OrderStatusID");
                        String OrderStatus = jsonObject.getString("OrderStatus");
                        String PaymentType = jsonObject.getString("PaymentType");
                        String Paymentstatus = jsonObject.getString("paymentstatus");
                        String ServiceName = jsonObject.getString("ServiceName");
                        String OrderID = jsonObject.getString("OrderID");
                        String[] OrderDate = jsonObject.getString("OrderDate").split("T");
                        String DeliveryCharages = jsonObject.getString("DeliveryCharages");
                        String DeliveryAssigned = jsonObject.getString("DeliveryAssigned");
                        String FirebaseId=jsonObject.getString("FirebaseId");
                        String DeliveryBoyEmail=jsonObject.getString("DeliveryBoyEmail");
                        String DeliveryBoyMobile=jsonObject.getString("DeliveryBoyMobile");
                        String OrderAmount=jsonObject.getString("OrderAmount");
                        vendorlist.add(new VendorList(CustomerID,CustomerName,Latitude,Longitude,Distance,Mobileno,PurchaseTypeID,OrderStatusID,OrderStatus,PaymentType,Paymentstatus,ServiceName,OrderID,OrderDate[0],DeliveryCharages,DeliveryAssigned,DeliveryBoyEmail,FirebaseId,DeliveryBoyMobile,OrderAmount));
                    }

                    LinearLayoutManager layoutManager=new LinearLayoutManager(OrderHistoryActivity.this,LinearLayoutManager.VERTICAL,false);
                    OrderHistoryAdapter adapter=new OrderHistoryAdapter(OrderHistoryActivity.this,vendorlist);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OrderHistoryActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderHistoryActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void configuretoolbar() {
        setSupportActionBar(toolbar);
        TextView textView=toolbar.findViewById(R.id.txtview_heading);
        textView.setText(vendorname);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (menu.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }

    }
}
