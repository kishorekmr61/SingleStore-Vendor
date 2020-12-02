package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.vendorsapp.Classes.DeliveryCharges;
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

public class OtherInfoActivity extends AppCompatActivity {
    EditText edttxt_gstno,edttxt_gstpercentage,edttxt_aboveorder,edttxt_discountpercentage,edttxt_responsetime,edttxt_amount;
    Button btn_gstsave,btn_discountsave,btn_responsesave,btn_deliverysave;
    LinearLayout lnrlayout_gst,lnrlayout_discount,lnrlayout_gstmain,lnrlayout_deliverycharges;
    Spinner spinner_distance;
    RadioButton radiobtn_gst,radiobtn_discount;
    Toolbar toolbar;
    Retro retrofits;
    SharedPreferences sp;
    String vendorid="",branchid="";
    ArrayList<String>distancelist=new ArrayList<>();
    ArrayList<Datalist>Distancelist=new ArrayList<>();
    String distanceid="",vendorname;
    ProgressDialog dialog;
    int gstcount=1,discountcount=1;
    RecyclerView recyclerview_deliverycharges;
    ArrayList<DeliveryCharges>deliverycahrgeslist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherinfo);
        toolbar=findViewById(R.id.toolbar);
        edttxt_gstno=findViewById(R.id.edttxt_gstno);
        edttxt_gstpercentage=findViewById(R.id.edttxt_gstpercentage);
        edttxt_aboveorder=findViewById(R.id.edttxt_aboveorder);
        edttxt_discountpercentage=findViewById(R.id.edttxt_discountpercentage);
        edttxt_amount=findViewById(R.id.edttxt_amount);
        btn_deliverysave=findViewById(R.id.btn_deliverysave);
        lnrlayout_gst=findViewById(R.id.lnrlayout_gst);
        lnrlayout_discount=findViewById(R.id.lnrlayout_discount);
        radiobtn_discount=findViewById(R.id.radiobtn_discount);
        radiobtn_gst=findViewById(R.id.radiobtn_gst);
        btn_gstsave=findViewById(R.id.btn_gstsave);
        lnrlayout_gstmain=findViewById(R.id.lnrlayout_gstmain);
        spinner_distance=findViewById(R.id.spinner_distance);
        btn_discountsave=findViewById(R.id.btn_discountsave);
        edttxt_responsetime=findViewById(R.id.edttxt_responsetime);
        btn_responsesave=findViewById(R.id.btn_responsesave);
        lnrlayout_deliverycharges=findViewById(R.id.lnrlayout_deliverycharges);
        recyclerview_deliverycharges=findViewById(R.id.recyclerview_delivercharges);
        retrofits=new Retro();
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        vendorname=sp.getString("vendorname","");
        dialog= CommonUtilities.dialog(OtherInfoActivity.this);
        configuretoolbar();
        btn_gstsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radiobtn_gst.isChecked()) {
                    InvokeService(true,edttxt_gstpercentage.getText().toString(), edttxt_gstno.getText().toString());
                    recreate();
                }

                else
                {
                    InvokeService(false,"0","null");
                    recreate();
                }
            }
        });

        btn_discountsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radiobtn_discount.isChecked()) {
                    InvokeService(true,edttxt_aboveorder.getText().toString(), edttxt_discountpercentage.getText().toString(), vendorid);
                    recreate();
                }

                else
                {
                    InvokeService(false,"0.00","0",vendorid);
                    recreate();
                }
            }
        });

        btn_responsesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvokeService(edttxt_responsetime.getText().toString());
                recreate();
            }
        });

        btn_deliverysave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!distanceid.equalsIgnoreCase("1080"))
                {
                    InvokeService_Deliverycharges(distanceid,edttxt_amount.getText().toString());
                    recreate();

                }

                else
                {
                    Toast.makeText(OtherInfoActivity.this,"Select the distance",Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(retrofits.isNetworkConnected(OtherInfoActivity.this))
        {
            dialog.show();
            InvokeService();
        }

    radiobtn_gst.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(gstcount%2==0)
            {
                radiobtn_gst.setChecked(false);
                lnrlayout_gst.setVisibility(View.GONE);
                gstcount++;
            }
            else
            {
                radiobtn_gst.setChecked(true);
                lnrlayout_gst.setVisibility(View.VISIBLE);
                gstcount++;
            }
        }
    });

        radiobtn_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(discountcount%2==0)
                {
                    radiobtn_discount.setChecked(false);
                    lnrlayout_discount.setVisibility(View.GONE);
                    discountcount++;
                }
                else
                {
                    radiobtn_discount.setChecked(true);
                    lnrlayout_discount.setVisibility(View.VISIBLE);
                    discountcount++;
                }

            }
        });

    }



    public void InvokeService(boolean hasgst,String gstper,String gstno) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.UpdateVendorGST(hasgst,gstper,gstno,vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"SUCCESS\""))
                    {
                        Toast.makeText(OtherInfoActivity.this,"Gst setting added successfully",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OtherInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtherInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void InvokeService(boolean hasdiscount,String disprice,String disper,String vendorid) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.UpdateVendorDiscount(hasdiscount,disprice,disper,vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"SUCCESS\""))
                    {
                        Toast.makeText(OtherInfoActivity.this,"Discount setting added successfully",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OtherInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtherInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void InvokeService( String responsetime) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GrocStoreResponse(responsetime,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"SUCCESS\""))
                    {
                        Toast.makeText(OtherInfoActivity.this,"Response time setting added successfully",Toast.LENGTH_SHORT).show();
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OtherInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtherInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void InvokeService( ) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetDeliveryDistance();
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if(response.isSuccessful())
                {
                    try {
                        distancelist.clear();
                        Distancelist.clear();
                        distancelist.add("Select Distance");
                        Distancelist.add(new Datalist("1080","Select Distance"));
                        String strresponse=response.body().string();
                        JSONArray jsonArray=new JSONArray(strresponse);
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            String id=object.getString("DistanceID");
                            String name=object.getString("Title");
                           distancelist.add(name);
                           Distancelist.add(new Datalist(id,name));
                        }
                        DistanceBinding();
                        InvokeService_Setting();
                    }

                    catch (IOException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();

                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OtherInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtherInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void DistanceBinding()
    {
        ArrayAdapter arrayAdapter=new ArrayAdapter(OtherInfoActivity.this,android.R.layout.simple_list_item_1,distancelist);
        spinner_distance.setAdapter(arrayAdapter);
        spinner_distance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distanceid=Distancelist.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void InvokeService_Deliverycharges(String id,String charges ) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.PostDeliveryCharges(branchid,id,charges);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase(""))
                    {
                        Toast.makeText(OtherInfoActivity.this,"Charges added successfully",Toast.LENGTH_SHORT).show();
                    }

                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OtherInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtherInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void InvokeService_Setting() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetSettingInfo(vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if(response.isSuccessful())
                    {
                        String strresponse=response.body().string();
                        JSONObject jsonObject=new JSONObject(strresponse);
                        String gstnumber=jsonObject.getString("GSTNo");
                        String gstper=jsonObject.getString("GSTPercent");
                        String discountprice=jsonObject.getString("DiscountOnAmountAbove");
                        String discountpercent=jsonObject.getString("DiscountPercent");
                        String responsetime=jsonObject.getString("Response");
                        String hasgst=jsonObject.getString("HasGST");
                        String HasDiscountonOrder=jsonObject.getString("HasDiscountonOrder");
                        if(hasgst.equals("true"))
                        {
                            radiobtn_gst.setChecked(true);
                            lnrlayout_gst.setVisibility(View.VISIBLE);
                        }
                        if(HasDiscountonOrder.equals("true"))
                        {
                            radiobtn_discount.setChecked(true);
                            lnrlayout_discount.setVisibility(View.VISIBLE);
                        }
                        if(!gstnumber.equals("null")) {
                            edttxt_gstno.setText(gstnumber);
                            edttxt_gstpercentage.setText(gstper);
                        }

                        if(!discountpercent.equals("0"))
                        {
                            edttxt_aboveorder.setText(discountprice);
                            edttxt_discountpercentage.setText(discountpercent);
                        }

                       if(!responsetime.equals("null")) {
                           edttxt_responsetime.setText(responsetime);
                           edttxt_responsetime.setSelection(edttxt_responsetime.getText().length());
                       }
                        InvokeService_Deliverycharges();

                    }


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
                    Toast.makeText(OtherInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtherInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void InvokeService_Deliverycharges() {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetDeliveryCharges(branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                deliverycahrgeslist=new ArrayList<>();
                if(response.isSuccessful())
                {
                    try {
                        String strresponse=response.body().string();
                        JSONArray jsonArray=new JSONArray(strresponse);
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String Title=jsonObject.getString("Title");
                            String Charges=jsonObject.getString("Charges");
                            deliverycahrgeslist.add(new DeliveryCharges(Title,Charges));
                        }

                        LinearLayoutManager layoutManager=new LinearLayoutManager(OtherInfoActivity.this,LinearLayoutManager.VERTICAL,false);
                        DeliveryChargesAdapter deliveryChargesAdapter=new DeliveryChargesAdapter(OtherInfoActivity.this,deliverycahrgeslist);
                        recyclerview_deliverycharges.setLayoutManager(layoutManager);
                        recyclerview_deliverycharges.setAdapter(deliveryChargesAdapter);

                    }


                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else
                {
                    lnrlayout_deliverycharges.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
