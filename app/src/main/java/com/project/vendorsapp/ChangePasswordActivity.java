package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.project.vendorsapp.Utilities.CommonUtilities;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends  AppCompatActivity {
    EditText edttxt_newpassword,edttxt_confirmpassword;
    Button btn_submit;
    SharedPreferences sp;
    Toolbar toolbar;
    SharedPreferences.Editor editor;
    String vendorid="",branchid="",mobileno="",vendorname;
    ProgressDialog progressDialog;
    AwesomeValidation validation;
    Retro retrofits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        edttxt_newpassword=findViewById(R.id.edttxt_newpassword);
        toolbar=findViewById(R.id.toolbar);
        edttxt_confirmpassword=findViewById(R.id.edttxt_confirmpassword);
        btn_submit=findViewById(R.id.btn_submit);
        sp = getSharedPreferences("config_info", 0);
        editor=sp.edit();
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        mobileno=sp.getString("mobileno","");
        vendorname=sp.getString("vendorname","");
        configuretoolbar();
        retrofits=new Retro();
        validation=new AwesomeValidation(ValidationStyle.BASIC);
        progressDialog=new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation.addValidation(ChangePasswordActivity.this, R.id.edttxt_newpassword, RegexTemplate.NOT_EMPTY
                        , R.string.newpassworderror);
                validation.addValidation(ChangePasswordActivity.this, R.id.edttxt_confirmpassword, RegexTemplate.NOT_EMPTY
                        , R.string.confirmpassworderror);
                if(edttxt_newpassword.getText().toString().equalsIgnoreCase(edttxt_confirmpassword.getText().toString()))
                {
                    if(validation.validate())
                    {
                         InvokeChangePassword(edttxt_newpassword.getText().toString());
                         progressDialog.show();
                    }

                }
                else
                {
                    Toast.makeText(ChangePasswordActivity.this,"Password do not match",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void InvokeChangePassword(String password) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS).build();
      Retrofit  retrofit = new Retrofit.Builder()
                .baseUrl(CommonUtilities.url1)
                .addConverterFactory(GsonConverterFactory.create()).client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.ChangePassword(mobileno,vendorid,branchid,password);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    if(response.isSuccessful()) {
                        String strresponse = response.body().string();
                        editor.putString("vendorid", "");
                        editor.putString("branchid", "");
                        editor.putString("mobileno", "");
                        editor.commit();
                        Toast.makeText(ChangePasswordActivity.this, "Your password has been updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ChangePasswordActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    Toast.makeText(ChangePasswordActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void Successmessage(String message)
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(ChangePasswordActivity.this);
        dialog.setTitle("Change Password");
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               // startActivity(new Intent(getContext(),LoginActivity.class));
                if(retrofits.isNetworkConnected(ChangePasswordActivity.this)) {
                    progressDialog.show();

                }
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
            }
        });
        dialog.show();
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
