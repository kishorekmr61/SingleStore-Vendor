package com.project.vendorsapp;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class StoreMessageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    SharedPreferences sp;
    String vendorid="",branchid="",vendorname;
    Retro retrofits;
    ArrayList<Datalist>messagelist;
    Button btn_savemessage;
    EditText edttxt_message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storemessage);
        recyclerView=findViewById(R.id.recyclerview);
        toolbar=findViewById(R.id.toolbar);
        btn_savemessage=findViewById(R.id.btn_savemessage);
        edttxt_message=findViewById(R.id.edttxt_message);
        retrofits=new Retro();
        messagelist=new ArrayList<>();
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        vendorname=sp.getString("vendorname","");
        InvokeService();
        configuretoolbar();
        btn_savemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvokeService(edttxt_message.getText().toString());
            }
        });
    }

    public void InvokeService() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetStoreMessages(vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    messagelist.clear();
                    String strresponse=response.body().string();
                    JSONArray jsonArray=new JSONArray(strresponse);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String messageid=jsonObject.getString("MessageInfoID");
                        String message=jsonObject.getString("MessageInfo");
                        String isactive=jsonObject.getString("IsActive");
                       messagelist.add(new Datalist(message,messageid,isactive));
                    }
                    LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    MessageAdapter messageAdapter=new MessageAdapter(getApplicationContext(),messagelist);
                    recyclerView.setAdapter(messageAdapter);
                    recyclerView.setLayoutManager(layoutManager);
                    messageAdapter.notifyDataSetChanged();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StoreMessageActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoreMessageActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void InvokeService(String message) {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.PostStoreMessages(message,vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    if(Integer.parseInt(strresponse)>0)
                    {
                        Toast.makeText(StoreMessageActivity.this,"Message saved successfully",Toast.LENGTH_SHORT).show();
                        edttxt_message.getText().clear();
                        recreate();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(StoreMessageActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoreMessageActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
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
