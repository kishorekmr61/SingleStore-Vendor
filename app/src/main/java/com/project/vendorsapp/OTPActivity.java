package com.project.vendorsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class OTPActivity extends AppCompatActivity implements TextWatcher {
    EditText edttxt_code1, edttxt_code2, edttxt_code3, edttxt_code4, edttxt_code5, edttxt_code6;
    TextView txtview_mobileno;
    ImageView imgview_logo;
    Retro retrofits;
    Button btn_submit;
    ArrayList<Register> registerlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        edttxt_code1 = findViewById(R.id.edttxt_code1);
        edttxt_code2 = findViewById(R.id.edttxt_code2);
        edttxt_code3 = findViewById(R.id.edttxt_code3);
        edttxt_code4 = findViewById(R.id.edttxt_code4);
        edttxt_code5 = findViewById(R.id.edttxt_code5);
        edttxt_code6 = findViewById(R.id.edttxt_code6);
        imgview_logo = findViewById(R.id.imgview_logo);
        txtview_mobileno = findViewById(R.id.txtview_mobileno);
        btn_submit = findViewById(R.id.btn_submit);
        retrofits = new Retro();
        registerlist = getIntent().getParcelableArrayListExtra("list");
        Glide.with(getApplicationContext())
                .load(R.mipmap.molslogo)
                .into(imgview_logo);
        txtview_mobileno.setText("to" + " " + getIntent().getStringExtra("mobileno"));
        edttxt_code1.addTextChangedListener(this);
        edttxt_code2.addTextChangedListener(this);
        edttxt_code3.addTextChangedListener(this);
        edttxt_code4.addTextChangedListener(this);
        edttxt_code5.addTextChangedListener(this);
        edttxt_code6.addTextChangedListener(this);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edttxt_code1.getText().toString() + edttxt_code2.getText().toString() + edttxt_code3.getText().toString() + edttxt_code4.getText().toString() + edttxt_code5.getText().toString() + edttxt_code6.getText().toString();
                InvokeService(getIntent().getStringExtra("mobileno"), otp);
            }
        });
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

    public void InvokeService(String mobileno, String otp) {
        try {
            Retrofit retrofit = retrofits.call();
            ApiService service = retrofit.create(ApiService.class);
            Call<ResponseBody> response = service.VerifyOTP(mobileno, otp);
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String strresponse = response.body().string();
                        if (strresponse.equalsIgnoreCase("\"success\"")) {
                            Intent intent = new Intent(OTPActivity.this,GetLocationActivity.class);
                            intent.putParcelableArrayListExtra("list", registerlist);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
