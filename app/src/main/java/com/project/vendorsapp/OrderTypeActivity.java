package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.project.vendorsapp.Utilities.CommonUtilities;

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

public class OrderTypeActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences sp;
    String vendorid="",branchid="",vendorname;
    Retro retrofits;
    ArrayList<Datalist>servicelist;
    ArrayList<Datalist>getservicelist;
    RecyclerView recyclerview_getservice;
    Button btn_add;
    int value=0;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordertype);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerview_getservice=findViewById(R.id.recyclerview_getservice);
        btn_add=findViewById(R.id.btn_add);
        retrofits=new Retro();
        servicelist=new ArrayList<>();
        getservicelist=new ArrayList<>();
        dialog= CommonUtilities.dialog(OrderTypeActivity.this);
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        vendorname=sp.getString("vendorname","");
        configuretoolbar();
        if(retrofits.isNetworkConnected(OrderTypeActivity.this))
        {
            InvokeService(vendorid,branchid);
            dialog.show();
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Datalist>list=getservicelist;
                JsonArray array=new JsonArray();
                for(int i=0;i<list.size();i++)
                {
                    JsonObject object=new JsonObject();
                    if(list.get(i).getMessageactive().equalsIgnoreCase("true"))
                    {
                        try {
                           object.addProperty("ServiceID",list.get(i).getMessageid());
                           object.addProperty("ServiceName",list.get(i).getMessage());
                            array.add(object);
                        } catch (JsonIOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                JsonObject jsonObject=new JsonObject();
                try {
                   jsonObject.addProperty("VendorID",Integer.parseInt(vendorid));
                   jsonObject.addProperty("BranchId",Integer.parseInt(branchid));
                   jsonObject.add("Services",array);
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }
                dialog.show();
                InvokeService(jsonObject);


            }
        });
    }

    public void InvokeService(String vendorid,String branchid) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetGrocVendorServices(vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    String strresponse=response.body().string();
                    servicelist.clear();
                    JSONArray jsonArray=new JSONArray(strresponse);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                       JSONObject jsonObject=jsonArray.getJSONObject(i) ;
                        String name=jsonObject.getString("ServiceName");
                        String id=jsonObject.getString("ID");
                        servicelist.add(new Datalist(id,name));
                    }
                    LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    OrderTypeAdapter adapter=new OrderTypeAdapter(getApplicationContext(),servicelist);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                    InvokeService();
                }

                catch (IOException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
            }
        });


    }

    public void InvokeService(JsonObject value) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.PostGrocVendorServices(value);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"SUCCESS\""))
                    {
                        Toast.makeText(OrderTypeActivity.this,"Service added successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }

    public void InvokeService() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetGrocServices();
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    getservicelist.clear();
                    JSONArray jsonArray=new JSONArray(strresponse);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("ServiceID");
                        String name=jsonObject.getString("ServiceName");
                        getservicelist.add(new Datalist(name,id,"false"));
                    }

                    LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                    GetServiceAdapter adapter=new GetServiceAdapter(getApplicationContext(),getservicelist);
                    recyclerview_getservice.setAdapter(adapter);
                    recyclerview_getservice.setLayoutManager(layoutManager);


                }

                catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OrderTypeActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderTypeActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
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
