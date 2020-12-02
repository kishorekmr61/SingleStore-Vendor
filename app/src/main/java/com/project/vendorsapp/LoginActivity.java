package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.Glide;
import com.project.vendorsapp.Utilities.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

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

public class LoginActivity extends AppCompatActivity {
    EditText edttxt_moblie, edttxt_password;
    TextView forgot_password, txtview_createaccount;
    Button btn_login;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Retro retrofits;
    ImageView imgview_logo;
    String deviceid = "";
    String strmobileno = "";
    AwesomeValidation validation;
    String vendorid = "", branchid = "";
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config_info", 0);
        vendorid = sp.getString("vendorid", "");
        branchid = sp.getString("branchid", "");
        if (!vendorid.equalsIgnoreCase("") && !branchid.equalsIgnoreCase("")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        edttxt_moblie = findViewById(R.id.edttxt_mobileno);
        edttxt_password = findViewById(R.id.edttxt_password);
        txtview_createaccount = findViewById(R.id.txtview_createaccount);
        forgot_password = findViewById(R.id.txtview_forgotpassword);
        imgview_logo = findViewById(R.id.imgview_logo);
        btn_login = findViewById(R.id.btn_login);
//        CommonUtilities.url = "http://167.86.86.78/SingleStoreApi/";
        retrofits = new Retro();
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        //  sp = getSharedPreferences("config_info", 0);
        editor = sp.edit();
        Glide.with(LoginActivity.this).load(R.drawable.molslogo).into(imgview_logo);

        //   vendorid=sp.getString("vendorid","");
        //  branchid=sp.getString("branchid","");
        strmobileno = sp.getString("mobileno", "");
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation.addValidation(LoginActivity.this, R.id.edttxt_mobileno, RegexTemplate.NOT_EMPTY
                        , R.string.mobileerror);
                validation.addValidation(LoginActivity.this, R.id.edttxt_password, RegexTemplate.NOT_EMPTY
                        , R.string.passworderror);
                if (validation.validate()) {
                    if (retrofits.isNetworkConnected(getApplicationContext())) {
                        strmobileno = "+91" + edttxt_moblie.getText().toString();
                        if (retrofits.isNetworkConnected(getApplicationContext())) {
                            dialog.show();
                            InvokeService(strmobileno, edttxt_password.getText().toString(), deviceid);
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, "You are not connected to network", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtview_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void InvokeService(final String mobileno, final String password, String deviceid) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS).build();
        Retrofit  retrofit = new Retrofit.Builder()
                .baseUrl(CommonUtilities.url)
                .addConverterFactory(GsonConverterFactory.create()).client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.Tokenlogin(mobileno, password, "");
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    dialog.dismiss();

                    //  String strresponse=response.body().string();
                    if (response.isSuccessful()) {
                        String strresponse = response.body().string().replace('"', ' ');
                        String Token = "Bearer" + strresponse;
                        InvokeLogin(mobileno, password, Token);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid mobile number and password", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void InvokeLogin(String mobileno, String password, String Token) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.LoginUser(Token, mobileno, password);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(strresponse);
                    String vendorid = jsonObject.getString("VendorID");
                    String branchid = jsonObject.getString("BranchID");
                    String BusinessName = jsonObject.getString("BusinessName");
                    String BranchName = jsonObject.getString("BranchName");
                    editor.putString("vendorid", vendorid);
                    editor.putString("branchid", branchid);
                    editor.putString("mobileno", strmobileno);
                    editor.putString("vendorname", BusinessName);
                    editor.putString("branchname", BranchName);
                    editor.commit();
                    InvokeService(branchid);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void InvokeService(String staffid) {
        deviceid = sp.getString("TOKEN", "");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://173.212.240.166:94/molsindia/")
                .baseUrl(CommonUtilities.url1)
                .addConverterFactory(GsonConverterFactory.create()).client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.UpdateStaffDeviceId(staffid, deviceid, "A", "2");
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String strresponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(strresponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("100")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (JSONException e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if (view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;

                if (viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if (rect.contains(x, y)) {
                        return consumed;
                    }
                } else if (viewNew instanceof EditText) {//|| viewNew instanceof CustomEditText
                    return consumed;
                }

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0);

                viewNew.clearFocus();

                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}