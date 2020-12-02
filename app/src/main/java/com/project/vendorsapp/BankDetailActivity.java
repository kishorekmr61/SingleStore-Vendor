package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BankDetailActivity extends AppCompatActivity {
    EditText edttxt_bankname,edttxt_accnumber,edttxt_reaccnumber,edttxt_ifsccode,edttxt_holdername;
    TextView txtview_substring;
    Retro retrofits;
    AwesomeValidation validation;
    Toolbar toolbar;
    SharedPreferences sp;
    String vendorid="",branchid="",vendorname;
    Button btn_save;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankdetail);
        edttxt_bankname=findViewById(R.id.edttxt_bankname);
        edttxt_accnumber=findViewById(R.id.edttxt_accountnumber);
        edttxt_reaccnumber=findViewById(R.id.edttxt_reenternumber);
        edttxt_ifsccode=findViewById(R.id.edttxt_ifsccode);
        edttxt_holdername=findViewById(R.id.edttxt_holdername);
        toolbar=findViewById(R.id.toolbar);
        btn_save=findViewById(R.id.btn_save);
        retrofits=new Retro();
        txtview_substring=new TextView(BankDetailActivity.this);
        progressDialog=new ProgressDialog(BankDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        vendorname=sp.getString("vendorname","");
        configuretoolbar();
        validation=new AwesomeValidation(ValidationStyle.BASIC);
        InvokeService(branchid);
        progressDialog.show();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation.addValidation(BankDetailActivity.this, R.id.edttxt_bankname, RegexTemplate.NOT_EMPTY
                        , R.string.banknameerror);
                validation.addValidation(BankDetailActivity.this, R.id.edttxt_accountnumber, RegexTemplate.NOT_EMPTY
                        , R.string.accounterror);
                validation.addValidation(BankDetailActivity.this, R.id.edttxt_reenternumber, RegexTemplate.NOT_EMPTY
                        , R.string.reenteraccounterror);

                validation.addValidation(BankDetailActivity.this, R.id.edttxt_ifsccode, RegexTemplate.NOT_EMPTY
                        , R.string.ifsccodeerror);


                validation.addValidation(BankDetailActivity.this, R.id.edttxt_holdername, RegexTemplate.NOT_EMPTY
                        , R.string.holdernameerror);

                if(edttxt_accnumber.getText().toString().equals(edttxt_reaccnumber.getText().toString())) {

                    if(edttxt_ifsccode.getText().toString().length()==11) {

                        if (validation.validate()) {
                            InvokeService(edttxt_accnumber.getText().toString(), edttxt_holdername.getText().toString(), edttxt_bankname.getText().toString(), edttxt_ifsccode.getText().toString());
                            progressDialog.show();
                        }

                    }
                    else
                    {
                        Toast.makeText(BankDetailActivity.this,"Invalid IFSC code,IFSC code should contain 11 alpanumeric character",Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(BankDetailActivity.this,"Account number do not match",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void InvokeService(String accno ,String holdername,String Bank,String ifsccode) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.UpdateAccountDetails(accno,branchid,holdername,Bank,ifsccode);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"SUCCESS\""))
                    {
                        Toast.makeText(BankDetailActivity.this,"Bank Deatils added successfully!!",Toast.LENGTH_SHORT).show();
                        recreate();
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
                    Toast.makeText(BankDetailActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BankDetailActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void InvokeService(String branchid) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetAccountDetails(branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    progressDialog.dismiss();
                    String strresponse=response.body().string();
                    if(!strresponse.isEmpty()) {
                        JSONObject object = new JSONObject(strresponse);
                        String Accholdername =object.getString("AccountHolderName");
                        String Accountnumber=object.getString("AccountNumber");
                        String BankName=object.getString("BankName");
                        String  IFSCCode=object.getString("IFSCCode");
                        edttxt_bankname.setText(BankName);
                        edttxt_bankname.setSelection(edttxt_bankname.getText().length());
                        edttxt_accnumber.setText(Accountnumber);
                        edttxt_reaccnumber.setText(Accountnumber);
                       // edttxt_accnumber.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                        edttxt_ifsccode.setText(IFSCCode);
                        edttxt_holdername.setText(Accholdername);
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
                    Toast.makeText(BankDetailActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BankDetailActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
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
