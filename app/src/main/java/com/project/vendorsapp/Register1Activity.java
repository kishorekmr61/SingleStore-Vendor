package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register1Activity extends AppCompatActivity {
    ImageView imgview_logo;
    Retro retrofits;
    Spinner spinner_state,spinner_city;
    ArrayList<String>statelistname;
    ProgressDialog dialog;
    ArrayList<Datalist>Statelist;
    ArrayList<String>citynamelist;
    ArrayList<Datalist>Citylist;
    String stateid,cityid;
    Button btn_next;
    EditText edttxt_mobileno,edttxt_ownernsname,edttxt_addressline1,edttxt_addressline2,edttxt_zipcode,edttxt_altrmobileno;
    AwesomeValidation validation;
    ArrayList<Register>registerlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        imgview_logo=findViewById(R.id.imgview_logo);
        spinner_state=findViewById(R.id.spinner_state);
        spinner_city=findViewById(R.id.spinner_city);
        btn_next=findViewById(R.id.btn_next);
        edttxt_ownernsname=findViewById(R.id.edttxt_ownersname);
        edttxt_addressline1=findViewById(R.id.edttxt_addressline1);
        edttxt_addressline2=findViewById(R.id.edttxt_adressline2);
        edttxt_zipcode=findViewById(R.id.edttxt_zipcode);
        edttxt_altrmobileno=findViewById(R.id.edttxt_altrmobileno);
        edttxt_mobileno=findViewById(R.id.edttxt_mobileno);
        validation=new AwesomeValidation(ValidationStyle.BASIC);
        registerlist=new ArrayList<>();
        registerlist=getIntent().getParcelableArrayListExtra("list");
        Glide.with(getApplicationContext())
                .load(R.mipmap.molslogo)
                .into(imgview_logo);
        retrofits=new Retro();
        statelistname=new ArrayList<>();
        Statelist=new ArrayList<>();
        citynamelist=new ArrayList<>();
        Citylist=new ArrayList<>();
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        InvokeService();
        dialog.show();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edttxt_mobileno.getText().toString().length()==10)
                {
                   validation.addValidation(Register1Activity.this, R.id.edttxt_mobileno, RegexTemplate.NOT_EMPTY
                            , R.string.mobileerror);
                    validation.addValidation(Register1Activity.this, R.id.edttxt_ownersname, RegexTemplate.NOT_EMPTY
                            , R.string.ownersnameerror);
                    validation.addValidation(Register1Activity.this, R.id.edttxt_addressline1, RegexTemplate.NOT_EMPTY
                            , R.string.addresslineerror);
                    validation.addValidation(Register1Activity.this, R.id.edttxt_adressline2, RegexTemplate.NOT_EMPTY
                            , R.string.addresslineerror);
                    validation.addValidation(Register1Activity.this, R.id.edttxt_zipcode, RegexTemplate.NOT_EMPTY
                            , R.string.zipcodeerror);
                    if(validation.validate())
                    {
                        if(!stateid.equalsIgnoreCase("1080") && !cityid.equalsIgnoreCase("1080")) {
                            InvokeOtp(edttxt_mobileno.getText().toString());
                        }

                        else
                        {
                            Toast.makeText(Register1Activity.this,"Please select state and city",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(Register1Activity.this,"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
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
    public void InvokeService() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetStates("1");
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    String strresponse=response.body().string();
                    statelistname.clear();
                    Statelist.clear();
                    statelistname.add("Select State");
                    Statelist.add(new Datalist("1080","Select State"));
                    JSONArray jsonArray=new JSONArray(strresponse);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String Stateid=jsonObject.getString("StateID");
                        String Statename=jsonObject.getString("StateName");
                        statelistname.add(Statename);
                        Statelist.add(new Datalist(Stateid,Statename));
                    }

                    BindingSpinner(statelistname,spinner_state);

                    spinner_state.setSelection(getIndex(spinner_state, "TAMIL NADU"));

                    //private method of your class

                }

                catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public void BindingSpinner(ArrayList<String> list, Spinner spinner)
    {
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,list) ;
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateid=Statelist.get(position).getId();
                InvokeService("23");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void InvokeService(String stateid) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetCities(stateid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String  strresponse = response.body().string();
                    citynamelist.clear();
                    Citylist.clear();
                    citynamelist.add("Select City");
                    Citylist.add(new Datalist("1080","Select City"));
                    JSONArray jsonArray=new JSONArray(strresponse);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String cityid=jsonObject.getString("CityID");
                        String cityname=jsonObject.getString("CityName");

                        citynamelist.add(cityname);
                        Citylist.add(new Datalist(cityid,cityname));
                    }
                    CitySpinner();
                    spinner_city.setSelection(getIndex(spinner_city, "Chennai"));
                }

                catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void CitySpinner()
    {
        ArrayAdapter adapter1=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,citynamelist);
        spinner_city.setAdapter(adapter1);
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityid=Citylist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void InvokeOtp(String mobileno) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.SendOTP(mobileno);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"success\"")){
                        registerlist.get(0).setOwnername(edttxt_ownernsname.getText().toString());
                        registerlist.get(0).setCountrycode(1);
                        registerlist.get(0).setStatecode(Integer.parseInt(stateid));
                        registerlist.get(0).setCityid(Integer.parseInt(cityid));
                        registerlist.get(0).setAddressline1(edttxt_addressline1.getText().toString());
                        registerlist.get(0).setAddressline2(edttxt_addressline2.getText().toString());
                        registerlist.get(0).setZipcode(edttxt_zipcode.getText().toString());
                        registerlist.get(0).setMobileno(edttxt_mobileno.getText().toString());
                        registerlist.get(0).setAltrmobileno(edttxt_altrmobileno.getText().toString());
                       Intent intent=new Intent(Register1Activity.this,OTPActivity.class);
                       intent.putParcelableArrayListExtra("list",registerlist);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.putExtra("mobileno",edttxt_mobileno.getText().toString());
                       startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
