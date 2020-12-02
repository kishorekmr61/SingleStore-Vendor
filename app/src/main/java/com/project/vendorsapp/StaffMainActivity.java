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
import android.widget.ImageView;
import android.widget.Toast;

import com.project.vendorsapp.Classes.Staff;

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

public class StaffMainActivity extends AppCompatActivity {
    RecyclerView recyclerview_staff;
    Retro retrofits;
    Toolbar toolbar;
    SharedPreferences sp;
    String vendorid,branchid;
    ArrayList<Staff>stafflist;
    ImageView imgview_addstaff;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffmain);
        toolbar=findViewById(R.id.toolbar);
        recyclerview_staff=findViewById(R.id.recyclerview_staff);
        imgview_addstaff=findViewById(R.id.imgview_addstaff);
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        retrofits=new Retro();
        configuretoolbar();
        progressDialog=new ProgressDialog(StaffMainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        InvokeGettingStaff();
        imgview_addstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StaffMainActivity.this,StaffActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void InvokeGettingStaff() {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetStaff(vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                stafflist=new ArrayList<>();
                if(response.isSuccessful())
                {
                    try {
                        String strresponse=response.body().string();
                        if(!strresponse.equals("[]")) {
                            JSONArray jsonArray = new JSONArray(strresponse);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String staffname = jsonObject.getString("StaffName");
                                String staffemail = jsonObject.getString("email");
                                String staffmobileno = jsonObject.getString("MobileNo");
                                String staffrole = jsonObject.getString("RoleTitle");
                                String staffaddress = jsonObject.getString("Address");
                                String staffreportingto = jsonObject.getString("ReportingTo");
                                if (staffreportingto.equals("null") || staffreportingto.equals("1080")) {
                                    staffreportingto = "_";
                                }
                                stafflist.add(new Staff(staffname, staffemail, staffmobileno, staffrole, staffaddress, staffreportingto));
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(StaffMainActivity.this, LinearLayoutManager.VERTICAL, false);
                            StaffMainAdapter staffMainAdapter = new StaffMainAdapter(StaffMainActivity.this, stafflist);
                            recyclerview_staff.setLayoutManager(layoutManager);
                            recyclerview_staff.setAdapter(staffMainAdapter);
                        }
                        else
                        {
                            Toast.makeText(StaffMainActivity.this,"No records found",Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (IOException e) {
                        progressDialog.dismiss();
                        Toast.makeText(StaffMainActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    catch (JSONException e) {
                        progressDialog.dismiss();
                        Toast.makeText(StaffMainActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StaffMainActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffMainActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @SuppressLint("RestrictedApi")
    private void configuretoolbar() {
        setSupportActionBar(toolbar);
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
