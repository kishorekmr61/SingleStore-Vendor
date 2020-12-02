package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.JsonObject;
import com.project.vendorsapp.Utilities.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetPasswordActivity extends AppCompatActivity {
    TextView txtview_mobileno;
    Spinner spinner;
    Button getpassword;
    Retro retrofits;
    ArrayList<String>namelist;
    ArrayList<VendorDetail>vendorlist;
    String vendorid;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpassword);
        txtview_mobileno=findViewById(R.id.txtview_mobileno);
        spinner=findViewById(R.id.spinner);
        getpassword=findViewById(R.id.btn_getpassword);
        txtview_mobileno.setText(getIntent().getStringExtra("mobileno"));
        namelist=new ArrayList<>();
        vendorlist=new ArrayList<>();
        progressDialog= CommonUtilities.dialog(GetPasswordActivity.this);
       InvokeService(txtview_mobileno.getText().toString());

       getpassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               InvokeService(vendorid,"+91"+txtview_mobileno.getText().toString());
               progressDialog.show();

           }
       });
    }

    public void InvokeService(String object) {
        CommonUtilities.url= "http://167.86.86.78/SingleStoreUi/";
        retrofits=new Retro();
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.MolsExtended(object);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    namelist.clear();
                    vendorlist.clear();
                    String strresponse=response.body().string();
                    if(!strresponse.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(strresponse);
                        JSONArray array = jsonObject.getJSONArray("table");
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject object1=array.getJSONObject(i);
                            String vendorid=object1.getString("vendorID");
                            String bussinessname=object1.getString("businessName");
                            namelist.add(bussinessname);
                            vendorlist.add(new VendorDetail(vendorid,bussinessname));
                        }

                    }

                    SpinnerBinding();

                }
                catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(GetPasswordActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GetPasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SpinnerBinding()
    {
        ArrayAdapter arrayAdapter=new ArrayAdapter(GetPasswordActivity.this,android.R.layout.simple_spinner_dropdown_item,namelist);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vendorid=vendorlist.get(position).getVendorID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void InvokeService(String vendorid,String mobileno) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(80000, TimeUnit.SECONDS)
                .readTimeout(80000, TimeUnit.SECONDS).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://167.86.86.78/SingleStoreApi/")
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.ForgotPassword(vendorid,"",mobileno);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"success\""))
                    {
                        Toast.makeText(GetPasswordActivity.this,"Password will sent to your mobile",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(GetPasswordActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(GetPasswordActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GetPasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
