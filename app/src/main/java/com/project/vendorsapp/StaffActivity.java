package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class StaffActivity extends AppCompatActivity implements TextWatcher {
    EditText edttxt_employeename, edttxt_mobileno, edttxt_address, edttxt_email;
    Spinner spinner_role, spinner_staff;
    Button btn_sendotp, btn_save;
    EditText edttxt_code1, edttxt_code2, edttxt_code3, edttxt_code4, edttxt_code5, edttxt_code6;
    ImageView imgview_logo;
    TextView txtview_mobileno;
    Button btn_submit;
    Toolbar toolbar;
    Retro retrofits;
    SharedPreferences sp;
    AwesomeValidation validation;
    ArrayList<String> rolelist;
    ArrayList<Datalist> Rolelist;
    ArrayList<String> stafflist;
    ArrayList<Datalist> Stafflist;
    String vendorid = "", branchid = "", staffid = "", roleid = "", vendorname;
    AlertDialog dialog;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    JsonObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        edttxt_employeename = findViewById(R.id.edttxt_employeename);
        spinner_role = findViewById(R.id.spinner_role);
        spinner_staff = findViewById(R.id.spinner_staff);
        btn_sendotp = findViewById(R.id.btn_sendotp);
        btn_sendotp.setVisibility(View.VISIBLE);
        edttxt_mobileno = findViewById(R.id.edttxt_mobilenumber);
        edttxt_email = findViewById(R.id.edttxt_email);
        btn_save = findViewById(R.id.btn_save);
        edttxt_address = findViewById(R.id.edttxt_address);
        toolbar = findViewById(R.id.toolbar);
        //  mDatabase = FirebaseDatabase.getInstance().getReference();
        sp = getSharedPreferences("config_info", 0);
        vendorid = sp.getString("vendorid", "");
        branchid = sp.getString("branchid", "");
        vendorname = sp.getString("vendorname", "");
        retrofits = new Retro();
        rolelist = new ArrayList<>();
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        Rolelist = new ArrayList<>();
        stafflist = new ArrayList<>();
        Stafflist = new ArrayList<>();
        progressDialog = CommonUtilities.dialog(StaffActivity.this);
        configuretoolbar();
        if (retrofits.isNetworkConnected(StaffActivity.this)) {
            InvokeService("1");
            progressDialog.show();
        } else {
            Toast.makeText(StaffActivity.this, "You are not connected to any network", Toast.LENGTH_SHORT).show();
        }

        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation.addValidation(StaffActivity.this, R.id.edttxt_mobilenumber, RegexTemplate.NOT_EMPTY
                        , R.string.mobileerror);
                if (validation.validate()) {
                    Invokesevice_IsStaffExist(edttxt_mobileno.getText().toString(), edttxt_email.getText().toString());
                }
                else
                {
                    Toast.makeText(StaffActivity.this, "Validation Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject object = new JsonObject();
                object.addProperty("FileName", "");
                object.addProperty("MediaType", "");
                object.addProperty("Buffer", "");
                jsonObject = new JsonObject();
                jsonObject.addProperty("StaffID", "");
                jsonObject.addProperty("VendorStaffID", "");
                jsonObject.addProperty("StaffName", edttxt_employeename.getText().toString());
                jsonObject.addProperty("MobileNo", edttxt_mobileno.getText().toString());
                jsonObject.addProperty("RoleID", roleid);
                jsonObject.addProperty("VendorID", vendorid);
                jsonObject.addProperty("BranchID", branchid);
                jsonObject.addProperty("Timestamp", "");
                jsonObject.addProperty("ReportingTo", staffid);
                jsonObject.addProperty("IsActive", true);
                jsonObject.addProperty("email", edttxt_email.getText().toString());
                jsonObject.addProperty("Address", edttxt_address.getText().toString());
                jsonObject.add("Avatar", object);
                register();

            }
        });
    }

    public void AlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.activity_otp, null, false);
        edttxt_code1 = view.findViewById(R.id.edttxt_code1);
        edttxt_code2 = view.findViewById(R.id.edttxt_code2);
        edttxt_code3 = view.findViewById(R.id.edttxt_code3);
        edttxt_code4 = view.findViewById(R.id.edttxt_code4);
        edttxt_code5 = view.findViewById(R.id.edttxt_code5);
        edttxt_code6 = view.findViewById(R.id.edttxt_code6);
        imgview_logo = view.findViewById(R.id.imgview_logo);
        txtview_mobileno = view.findViewById(R.id.txtview_mobileno);
        btn_submit = view.findViewById(R.id.btn_submit);
        Glide.with(getApplicationContext())
                .load(R.mipmap.molslogo)
                .into(imgview_logo);
        txtview_mobileno.setText("to" + " " + edttxt_mobileno.getText().toString());
        edttxt_code1.addTextChangedListener(StaffActivity.this);
        edttxt_code2.addTextChangedListener(StaffActivity.this);
        edttxt_code3.addTextChangedListener(StaffActivity.this);
        edttxt_code4.addTextChangedListener(StaffActivity.this);
        edttxt_code5.addTextChangedListener(StaffActivity.this);
        edttxt_code6.addTextChangedListener(StaffActivity.this);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edttxt_code1.getText().toString() + edttxt_code2.getText().toString() + edttxt_code3.getText().toString() + edttxt_code4.getText().toString() + edttxt_code5.getText().toString() + edttxt_code6.getText().toString();
                InvokeService(edttxt_mobileno.getText().toString(), otp);
            }
        });
        builder.setView(view);
        dialog = builder.show();


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (edttxt_code1.getText().toString().length() == 1) {

            if (edttxt_code2.getText().toString().length() == 1) {
                if (edttxt_code3.getText().toString().length() == 1) {
                    if (edttxt_code4.getText().toString().length() == 1) {
                        if (edttxt_code5.getText().toString().length() == 1) {
                            edttxt_code6.requestFocus();
                        } else {
                            edttxt_code5.requestFocus();
                        }
                    } else {
                        edttxt_code4.requestFocus();
                    }
                } else {
                    edttxt_code3.requestFocus();
                }
            } else {
                edttxt_code2.requestFocus();
            }

        }

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

    public void InvokeService(String categoryid) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetRoles(categoryid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strresponse = response.body().string();
                    rolelist.clear();
                    Rolelist.clear();
                    rolelist.add("Select Role");
                    Rolelist.add(new Datalist("1080", "Select Role"));
                    if (response.isSuccessful()) {
                        JSONArray jsonArray = new JSONArray(strresponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("StaffRoleID");
                            String name = jsonObject.getString("RoleTitle");
                            rolelist.add(name);
                            Rolelist.add(new Datalist(id, name));
                        }
                        BindSpinner();

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
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StaffActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void InvokeSendOTP(String mobileno) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.SendOTP(mobileno);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse = response.body().string();
                    if (strresponse.equalsIgnoreCase("\"success\"")) {
                        AlertDialog();
                    }
                    else
                    {
                        Toast.makeText(StaffActivity.this, "Contact support team", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StaffActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void BindSpinner() {
        ArrayAdapter adapter = new ArrayAdapter(StaffActivity.this, android.R.layout.simple_list_item_1, rolelist);
        spinner_role.setAdapter(adapter);
        InvokeGettingStaff();
        spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleid = Rolelist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void InvokeService(String mobileno, String otp) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.VerifyOTP(mobileno, otp);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse = response.body().string();
                    if (strresponse.equalsIgnoreCase("\"success\"")) {
                        dialog.dismiss();
                        Toast.makeText(StaffActivity.this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                        btn_sendotp.setVisibility(View.GONE);
                        btn_save.setVisibility(View.VISIBLE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StaffActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void InvokeGettingStaff() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetStaff(vendorid, branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse = response.body().string();
                    stafflist.clear();
                    Stafflist.clear();
                    stafflist.add("Select Reporting Staff");
                    Stafflist.add(new Datalist("1080", "Select Reporting Staff"));
                    JSONArray jsonArray = new JSONArray(strresponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String staffid = jsonObject.getString("StaffID");
                        String staffname = jsonObject.getString("StaffName");
                        stafflist.add(staffname);
                        Stafflist.add(new Datalist(staffid, staffname));
                    }
                    BindSpinnerStaff();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StaffActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @SuppressLint("RestrictedApi")
    private void configuretoolbar() {
        setSupportActionBar(toolbar);
        TextView textView = toolbar.findViewById(R.id.txtview_heading);
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

    public void BindSpinnerStaff() {
        ArrayAdapter adapter = new ArrayAdapter(StaffActivity.this, android.R.layout.simple_list_item_1, stafflist);
        spinner_staff.setAdapter(adapter);
        spinner_staff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                staffid = Stafflist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void InvokeSaveStaff(JsonObject object) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.PostStaff(object);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strresponse = response.body().string();
                    int a = 1;
                    if (strresponse.equalsIgnoreCase("\"SUCCESS\"")) {
                        Toast.makeText(StaffActivity.this, "Staff is added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StaffActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        CommonUtilities.ToastMessage(StaffActivity.this, strresponse);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(StaffActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StaffActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void Invokesevice_IsStaffExist(String mobileno, String email) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://167.86.86.78/SingleStoreUi/")
                .addConverterFactory(GsonConverterFactory.create()).client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.IsStaffExists(mobileno, email);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String strresponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(strresponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("100")) {
                            InvokeSendOTP(edttxt_mobileno.getText().toString());
                        } else {
                            Toast.makeText(StaffActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void register() {
        if (roleid.equalsIgnoreCase("6")) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(edttxt_email.getText().toString(), edttxt_mobileno.getText().toString()).addOnCompleteListener(StaffActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(StaffActivity.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(StaffActivity.this, "Authentication Successfull" + task.getException(),
                                Toast.LENGTH_SHORT).show();
                        writeNewUser(edttxt_mobileno.getText().toString());
                    }
                }
            });
        } else {
            jsonObject.addProperty("FirebaseId", "");
            progressDialog.show();
            InvokeSaveStaff(jsonObject);
        }
    }

    private void writeNewUser(String mobileno) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("locations").push();

        //  mDatabase.child(strNewKey).child("mobileno").setValue(mobileno);

        mDatabase.child("latitude").setValue(17.440081);
        mDatabase.child("longitude").setValue(78.348915);
        mDatabase.child("mobileno").setValue(mobileno);
        String strNewKey = mDatabase.getKey();
        jsonObject.addProperty("FirebaseId", strNewKey);
        progressDialog.show();
        InvokeSaveStaff(jsonObject);


        //  mDatabase.child("locations").push().setValue(mobileno.substring(0,4));
        // mDatabase.child("locations").child(mobileno.substring(0,4)).child("latitude").setValue("17.440081");
        //   mDatabase.child("locations").child(mobileno.substring(0,4)).child("longitude").setValue("78.348915");

    }

}
