package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.project.vendorsapp.Classes.Holiday;
import com.project.vendorsapp.Utilities.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateHolidaysActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    EditText edttxt_calender,edttxt_description;
    Button btn_add;
    SharedPreferences sp;
    RecyclerView recyclerview_holiday;
    String vendorid="",branchid="",vendorname;
    String date,month,years;
    Retro retrofits;
    Toolbar toolbar;
    ArrayList<Holiday>holidaylist;
    CardView cardview_holiday;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createholiday);
        edttxt_calender=findViewById(R.id.edttxt_calender);
        btn_add=findViewById(R.id.btn_add);
        toolbar=findViewById(R.id.toolbar);
        recyclerview_holiday=findViewById(R.id.recyclerview_holiday);
        edttxt_description=findViewById(R.id.edttxt_description);
        cardview_holiday=findViewById(R.id.cardview_holiday);
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        vendorname=sp.getString("vendorname","");
        dialog= CommonUtilities.dialog(CreateHolidaysActivity.this);
        retrofits=new Retro();
        configuretoolbar();
        dialog.show();
        InvokeService();
        edttxt_calender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edttxt_calender.getRight() - edttxt_calender.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                          Calender();
                        return true;
                    }
                }
                return false;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dates=years+"/"+month+"/"+date;
                InvokeService(dates,edttxt_description.getText().toString());
                dialog.show();
            }
        });


    }

    public void Calender()
    {
        DialogFragment fragment = new DatePickerFragments();
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(), "timepicker");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int m, int dayOfMonth) {
      years=String.valueOf(year);
        if(dayOfMonth<10)
        {
            date="0"+String.valueOf(dayOfMonth);
        }
        else
        {
            date=String.valueOf(dayOfMonth);
        }
        if(m<10)
        {
            month = "0"+String.valueOf(m+1);
        }
        else
        {
            month = String.valueOf(m+1);
        }
      String  Date = String.valueOf(date)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
        edttxt_calender.setText(Date);
    }

    public void InvokeService() {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetVendorHolidays(branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                holidaylist=new ArrayList<>();
                if(response.isSuccessful())
                {
                    try {
                        String strresponse=response.body().string();
                        if(!strresponse.equals("[]")) {
                            JSONArray jsonArray = new JSONArray(strresponse);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String HolidayDescription = jsonObject.getString("HolidayDescription");
                                if(HolidayDescription.equals("null"))
                                {
                                    HolidayDescription="No Message";
                                }
                                String StoreStatus = jsonObject.getString("StoreStatus");
                                String HolidayDate = jsonObject.getString("HolidayDate");
                                String HolidayID = jsonObject.getString("HolidayID");
                                holidaylist.add(new Holiday(HolidayDescription, StoreStatus, HolidayDate, HolidayID));

                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(CreateHolidaysActivity.this, LinearLayoutManager.VERTICAL, false);
                            CreateHolidayAdapter createHolidayAdapter = new CreateHolidayAdapter(CreateHolidaysActivity.this, holidaylist);
                            recyclerview_holiday.setLayoutManager(layoutManager);
                            recyclerview_holiday.setAdapter(createHolidayAdapter);
                        }
                        else
                        {
                          cardview_holiday.setVisibility(View.GONE);
                        }
                    }

                    catch (IOException e) {
                        dialog.dismiss();
                        Toast.makeText(CreateHolidaysActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (JSONException e) {
                        dialog.dismiss();
                        Toast.makeText(CreateHolidaysActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateHolidaysActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateHolidaysActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



    public void InvokeService(String holiday,String desc) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.PostStoreHolidays(holiday,vendorid,branchid,desc);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    if(response.isSuccessful()) {
                        String strresponse = response.body().string();
                        if (Integer.parseInt(strresponse) > 0) {
                            Toast.makeText(CreateHolidaysActivity.this, "Store holiday is added successfully", Toast.LENGTH_SHORT).show();
                            edttxt_calender.getText().clear();
                            edttxt_description.getText().clear();
                            recreate();
                        }
                    }
                }

                catch (IOException e) {
                    dialog.dismiss();
                    Toast.makeText(CreateHolidaysActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                 dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateHolidaysActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateHolidaysActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
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
