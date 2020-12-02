package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class DelieveryAssignActivity extends AppCompatActivity {
    TextView txtview_customername,txtview_mobileno,txtview_orderid,txtview_ordertype,txtview_vendorname,txtview_branchname;
    Spinner spinner;
    Button btn_submit;
    Retro retrofits;
    SharedPreferences sp;
    ArrayList<String>deliverboylist;
    ArrayList<DeliverBoyList>deliverylist;
    ProgressDialog progressDialog;

    String Customername="",Mobileno="",Orderid="",Ordertype="",strvendorid="",strbranchid="",strvendorname="",strbranchname="",Staffid="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigndelivery);
        txtview_customername=findViewById(R.id.txtview_customername);
        txtview_mobileno=findViewById(R.id.txtview_mobileno);
        txtview_orderid=findViewById(R.id.txtview_orderid);
        txtview_ordertype=findViewById(R.id.txtview_ordertype);
        txtview_vendorname=findViewById(R.id.txtview_vendorname);
        txtview_branchname=findViewById(R.id.txtview_branchname);
        btn_submit=findViewById(R.id.btn_submit);
        spinner=findViewById(R.id.spinner);
        progressDialog= CommonUtilities.dialog(DelieveryAssignActivity.this);
        retrofits=new Retro();
        deliverboylist=new ArrayList<>();
        deliverylist=new ArrayList<>();
        Customername=getIntent().getStringExtra("customername");
        Mobileno=getIntent().getStringExtra("mobileno");
        Orderid=getIntent().getStringExtra("orderid");
        Ordertype=getIntent().getStringExtra("ordertype");
        txtview_customername.setText(Customername);
        txtview_mobileno.setText(Mobileno);
        txtview_orderid.setText(Orderid);
        txtview_ordertype.setText(Ordertype);
        sp = getSharedPreferences("config_info", 0);
        strvendorid=sp.getString("vendorid","");
        strbranchid=sp.getString("branchid","");
        strvendorname=sp.getString("vendorname","");
        strbranchname=sp.getString("branchname","");
        txtview_vendorname.setText(strvendorname);
        txtview_branchname.setText(strbranchname);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvokeAssignDeliverBoy();
                progressDialog.show();
            }
        });
        InvokeService();
        progressDialog.show();
    }


    public void InvokeService() {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetStaffByCategory(strvendorid,strbranchid,"Delivery Boy");
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    if(response.isSuccessful()) {
                        deliverboylist.clear();
                        deliverylist.clear();
                        String strresponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(strresponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("StaffID");
                            String name = jsonObject.getString("StaffName");
                            String MobileNo = jsonObject.getString("MobileNo");
                            String Address = jsonObject.getString("Address");
                            String CategoryID = jsonObject.getString("CategoryID");
                            deliverylist.add(new DeliverBoyList(id, name, MobileNo, Address, CategoryID));
                            deliverboylist.add(name);
                        }
                        BindSpinner();
                    }
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
                    Toast.makeText(DelieveryAssignActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DelieveryAssignActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void BindSpinner()
    {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,deliverboylist);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Staffid=deliverylist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void InvokeAssignDeliverBoy() {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.AssignStaffForDelivery(strvendorid,strbranchid,Staffid,Orderid,"ASSIGNSTAFF",Mobileno);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strresponse=response.body().string();
                    JSONObject object=new JSONObject(strresponse);
                    String responses=object.getString("Response");
                    if(responses.equalsIgnoreCase("100"))
                    {
                        Toast.makeText(DelieveryAssignActivity.this,"Order assign to a delivery Boy",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DelieveryAssignActivity.this,MainActivity.class));
                    }

                } catch (IOException e) {
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
                    Toast.makeText(DelieveryAssignActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DelieveryAssignActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
