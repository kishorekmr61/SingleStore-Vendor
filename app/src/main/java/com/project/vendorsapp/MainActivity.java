package com.project.vendorsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.project.vendorsapp.Notification.NotificationActivity;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtview_ordered,txtview_packed,txtview_shipped,txtview_delivered,txtview_vendorname,txtview_branchname;
    String strvendorid="";
    String strbranchid="";
    Retro retrofits;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ArrayList<VendorList>vendorlist;
    String id="1,2,7";
    String strvendorname="",strMobibleno="";
    String strbranchname="";
    FrameLayout frameLayout;
    RelativeLayout mainlayout;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle barDrawerToggle;
    LinearLayoutManager layoutManager;
    MainAdapter mainAdapter;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtview_ordered=findViewById(R.id.txtview_ordered);
        txtview_packed=findViewById(R.id.txtview_packed);
        txtview_shipped=findViewById(R.id.txtview_shipped);
        txtview_delivered=findViewById(R.id.txtview_delivered);
        txtview_vendorname=findViewById(R.id.txtview_vendorname);
        txtview_branchname=findViewById(R.id.txtview_branchname);
        frameLayout=findViewById(R.id.framelayout);
        mainlayout=findViewById(R.id.mainlayout);
        recyclerView=findViewById(R.id.recyclerview);
        drawerLayout=findViewById(R.id.drawerlayout);
        toolbar=findViewById(R.id.toptoolbar);
        navigationView=findViewById(R.id.navigationview);
        setSupportActionBar(toolbar);
        barDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        retrofits=new Retro();
        navigationView.setNavigationItemSelectedListener(this);
        vendorlist=new ArrayList<>();
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        sp = getSharedPreferences("config_info", 0);
        editor=sp.edit();
        strvendorname=sp.getString("vendorname","");
        strbranchname=sp.getString("branchname","");
        strvendorid=sp.getString("vendorid","");
        strbranchid=sp.getString("branchid","");
        strMobibleno=sp.getString("mobileno","");
        txtview_vendorname.setText(strvendorname);
        txtview_branchname.setText(strbranchname);
        View view= getLayoutInflater().inflate(R.layout.header,null,false);
        final ImageView imgview_header=view.findViewById(R.id.imgview_header);
        Glide.with(getApplicationContext())
                .load(R.drawable.molslogo)
                .placeholder(R.mipmap.ic_launcher)
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imgview_header.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        navigationView.addHeaderView(view);
       if(retrofits.isNetworkConnected(getApplicationContext())) {
           txtview_ordered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkorange));
           dialog.show();
           InvokeService(id);
       }

    }

    public void Clicklistener(View view)
    {
        switch (view.getId())
        {
            case R.id.txtview_ordered: id="1,2,7";

                if(retrofits.isNetworkConnected(getApplicationContext())) {
                    txtview_ordered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkorange));
                    txtview_packed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_shipped.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_delivered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    dialog.show();
                    InvokeService(id);
                }

                break;


            case R.id.txtview_packed: id="3";
                if(retrofits.isNetworkConnected(getApplicationContext())) {
                    txtview_packed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkorange));
                    txtview_ordered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_shipped.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_delivered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    dialog.show();
                    InvokeService(id);
                }

                break;

            case R.id.txtview_shipped:id="4";
                if(retrofits.isNetworkConnected(getApplicationContext())) {
                    txtview_shipped.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkorange));
                    txtview_packed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_ordered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_delivered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    dialog.show();
                    InvokeService(id);
                }

                break;

            case R.id.txtview_delivered:id="5" ;
                if(retrofits.isNetworkConnected(getApplicationContext())) {
                    txtview_delivered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkorange));
                    txtview_packed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_shipped.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    txtview_ordered.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.orange));
                    dialog.show();
                    InvokeService(id);
                }


        }

    }

    public void InvokeService(String id)
    {
        Retrofit retrofit = retrofits.call();
        final String[] ids=id.split(",");
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetOrders(strvendorid,strbranchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    vendorlist.clear();
                    String strresponse=response.body().string();
                    JSONArray array=new JSONArray(strresponse);
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject jsonObject=array.getJSONObject(i);
                        String CustomerID=jsonObject.getString("CustomerID");
                        String CustomerName=jsonObject.getString("CustomerName");
                        String Latitude=jsonObject.getString("Latitude");
                        String Longitude=jsonObject.getString("Longitude");
                        String Distance=jsonObject.getString("Distance");
                        String Mobileno=jsonObject.getString("MobileNo");
                        String PurchaseTypeID=jsonObject.getString("PurchaseTypeID");
                        String OrderStatusID=jsonObject.getString("OrderStatusID");
                        String OrderStatus=jsonObject.getString("OrderStatus");
                        String PaymentType=jsonObject.getString("PaymentType");
                        String Paymentstatus=jsonObject.getString("paymentstatus");
                        String ServiceName=jsonObject.getString("ServiceName");
                        String OrderID=jsonObject.getString("OrderID");
                        String OrderDate=jsonObject.getString("OrderDate");
                        String DeliveryCharages=jsonObject.getString("DeliveryCharages");
                        String DeliveryAssigned=jsonObject.getString("DeliveryAssigned");
                        String FirebaseId=jsonObject.getString("FirebaseId");
                        String DeliveryBoyEmail=jsonObject.getString("DeliveryBoyEmail");
                        String DeliveryBoyMobile=jsonObject.getString("DeliveryBoyMobile");
                        for(int j=0;j<ids.length;j++)
                        {
                            if(OrderStatusID.equalsIgnoreCase(ids[j]))
                            {
                                vendorlist.add(new VendorList(CustomerID,CustomerName,Latitude,Longitude,Distance,Mobileno,PurchaseTypeID,OrderStatusID,OrderStatus,PaymentType,Paymentstatus,ServiceName,OrderID,OrderDate,DeliveryCharages,DeliveryAssigned,DeliveryBoyEmail,FirebaseId,DeliveryBoyMobile,""));
                            }
                        }
                    }
                    layoutManager=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
                    mainAdapter=new MainAdapter(MainActivity.this,vendorlist);
                    recyclerView.setAdapter(mainAdapter);
                    recyclerView.setLayoutManager(layoutManager);


                }

                catch (IOException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(MainActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

       AlertDialog();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.item_home:
                drawerLayout.closeDrawers();
                Intent intent6=getIntent();
                intent6.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent6);
                break;


            case R.id.item_logout:
                drawerLayout.closeDrawers();
                editor.putString("vendorid","");
                editor.putString("branchid","");
                editor.putString("mobileno","");
                editor.commit();
               Intent intent=new Intent(MainActivity.this,LoginActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();
                break;


            case R.id.item_setting:drawerLayout.closeDrawers();
                Intent intent1=new Intent(MainActivity.this,SettingActivity.class);
                intent1.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;

            case R.id.item_orderhistory:drawerLayout.closeDrawers();
                Intent intent2=new Intent(MainActivity.this,OrderHistoryActivity.class);
                intent2.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;

            case R.id.item_staff:drawerLayout.closeDrawers();
                Intent intent3=new Intent(MainActivity.this,StaffMainActivity.class);
                intent3.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                break;

            case R.id.item_inventory:drawerLayout.closeDrawers();
                Intent intent5=new Intent(MainActivity.this,WebviewActivityActivity.class);
                intent5.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent5);
                break;

            case R.id.item_changepassword:
                drawerLayout.closeDrawers();
                Intent intent4=new Intent(MainActivity.this, ChangePasswordActivity.class);
                intent4.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent4);
                break;




        }

        return true;

    }

    public void AlertDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure you want to close the App");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_notification:Intent intent=new Intent(MainActivity.this, NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }

        return true;
    }


}
