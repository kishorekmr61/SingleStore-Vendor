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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.vendorsapp.Utilities.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoreInfoActivity extends AppCompatActivity {
    Spinner spinner_opentime,spinner_closetime;
    RecyclerView recyclerView;
    EditText edttxt_email;
    Button btn_save;
    String opentimelist[]={"Select Opening Time","06:00 AM","06:30 AM","07:00 AM","07:30 AM","08:00 AM","08:30 AM","09:00 AM","09:30 AM","10:00 AM","10:30 AM","11:00 AM","11:30 AM","12:00 PM"};
    String closetimelist[]={"Select Closing Time","12:30 PM","01:00 PM","01:30 PM","02:00 PM","02:30 PM","03:00 PM","03:30 PM","04:00 PM","04:30 PM","05:00 PM","05:30 PM","06:00 PM","06:30 PM","07:00 PM","07:30 PM","08:00 PM","08:30 PM","09:00 PM","09:30 PM","10:00 PM","10:30 PM","11:00 PM","11:30 PM","12:00 PM"};
    String Closetimelist[]={"Select Closing Time","12:30:00","13:00:00","13:30:00","14:00:00","14:30:00","15:00:00","15:30:00","16:00:00","16:30:00","17:00:00","17:30:00","18:00:00","18:30:00","19:00:00","19:30:00","20:00:00","20:30:00","21:00:00","21:30:00","22:00:00","22:30:00","23:00:00","23:30:00","24:00:00"};
    ArrayList<WeekDays>weeklist;
    SharedPreferences sp;
    String vendorid,branchid,vendorname;
    Toolbar toolbar;
    ImageView imgview_store;
    boolean b=false;
    CheckBox checkBox;
    Retro retrofits;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeinfo);
        spinner_opentime=findViewById(R.id.spinner_opentime);
        spinner_closetime=findViewById(R.id.spinner_closetime);
        imgview_store=findViewById(R.id.imgview_store);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerview);
        edttxt_email=findViewById(R.id.edttxt_email);
        checkBox=findViewById(R.id.checkbox);
        btn_save=findViewById(R.id.btn_save);
        retrofits=new Retro();
        progressDialog= CommonUtilities.dialog(StoreInfoActivity.this);
        weeklist=new ArrayList<>();
        weeklist.add(new WeekDays("MO","0",2));
        weeklist.add(new WeekDays("TU","0",4));
        weeklist.add(new WeekDays("WE","0",8));
        weeklist.add(new WeekDays("Th","0",16));
        weeklist.add(new WeekDays("FR","0",32));
        weeklist.add(new WeekDays("SA","0",64));
        weeklist.add(new WeekDays("SU","0",1));
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        vendorname=sp.getString("vendorname","");
        configuretoolbar();
        Spinner_Binding(spinner_opentime,opentimelist);
        Spinner_Binding(spinner_closetime,closetimelist);
       Week_Days();
       progressDialog.show();
        InvokeService();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WeekDays>list=weeklist;
                int totaldays=0;
                for (int i=0;i<list.size();i++)
                {
                  if(list.get(i).getId().equalsIgnoreCase("1"))
                  {
                     totaldays=totaldays+list.get(i).getcode();
                  }
                }
                if(checkBox.isChecked())
                {
                    b=true;
                }
                String opentime=spinner_opentime.getSelectedItem().toString().replace("AM","").trim();
                String closetime=Closetimelist[spinner_closetime.getSelectedItemPosition()];
                InvokeService(edttxt_email.getText().toString(),opentime,closetime,String.valueOf(totaldays));
            }
        });

    }

    public void Spinner_Binding(Spinner s, String[] time)
    {
        ArrayAdapter arrayAdapter=new ArrayAdapter(StoreInfoActivity.this,android.R.layout.simple_list_item_1,time);
        s.setAdapter(arrayAdapter);

    }

    public void Week_Days()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL,false);
        WeekAdapter adapter=new WeekAdapter(getApplicationContext(),weeklist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void InvokeService(String email,String opentime,String closetime,String workingdays) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.PostVendorSettings(Integer.parseInt(vendorid),Integer.parseInt(branchid),email,opentime,closetime,Integer.parseInt(workingdays),"1",b);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(strresponse.equalsIgnoreCase("\"SUCCESS\""))

                    {
                        Toast.makeText(getApplicationContext(),"Data is saved successfully",Toast.LENGTH_SHORT).show();
                        recreate();
                    }


                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StoreInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoreInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void InvokeService() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetSettingInfo(vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strresponse=response.body().string();
                   if(response.isSuccessful())
                   {
                       JSONObject jsonObject=new JSONObject(strresponse);
                       String Email=jsonObject.getString("Email");
                       if(Email.equals("null"))
                       {
                           Email="";
                       }
                       String url="http://167.86.86.78/SingleStoreApi/MolsFiles/Vendors//"+vendorid+"/"+branchid+"/"+jsonObject.getString("BranchAvatar");
                       String HasBranch=jsonObject.getString("HasBranch");
                      // String[] workingdays=jsonObject.getString("workingdays").split("(?!^)");
                       String workingdays=jsonObject.getString("workingdays");
                       if(workingdays.equals("null"))
                       {
                           workingdays="0";
                       }
                       String CloseTime=jsonObject.getString("CloseTime");
                       String OpenTime=jsonObject.getString("OpenTime");
                       Glide.with(StoreInfoActivity.this).load(url).placeholder(R.mipmap.molslogo).into(imgview_store);
                       edttxt_email.setText(Email);
                       if(!HasBranch.equalsIgnoreCase("false"))
                       {
                           checkBox.setChecked(true);
                       }

                       if(!OpenTime.equals("00:00:00") && OpenTime.equals("null") ) {

                           OpenTime(OpenTime);
                       }

                       if(!CloseTime.equals("00:00:00") && CloseTime.equals("null")) {
                           CloseTime(CloseTime);
                       }

                       int workingday=Integer.valueOf(workingdays);

                     if(workingday>0) {
                         for (int i = 0; i < weeklist.size(); i++) {

                             int iDay = weeklist.get(i).getcode() & workingday;
                             if (iDay > 0) {
                                 weeklist.get(i).setId("1");
                             } else {
                                 weeklist.get(i).setId("0");
                             }

                         }
                         Week_Days();
                     }



                   }


                }
                catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                } catch (ParseException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StoreInfoActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoreInfoActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void OpenTime(String opentime) throws ParseException {
        java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        //java.util.Date date1 = df.parse("12:00:00");
        java.util.Date date2 = df.parse(opentime);
        for(int i=1;i<opentimelist.length;i++)
        {
            String time=opentimelist[i].replace("AM","").trim()+":00";
            java.util.Date date1 = df.parse(time);
            if(date1.equals(date2))
            {
              spinner_opentime.setSelection(i);
              break;
            }

        }
    }

    public void CloseTime(String closetime) throws ParseException {
        java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        //java.util.Date date1 = df.parse("12:00:00");
        java.util.Date date2 = df.parse(closetime);
        for(int i=1;i<Closetimelist.length;i++)
        {
            String closetimes=Closetimelist[i];
            java.util.Date date1 = df.parse(closetimes);
            if(date1.equals(date2))
            {
                spinner_closetime.setSelection(i);
                return;
            }
        }
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
