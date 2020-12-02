package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.vendorsapp.Classes.Document;

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

public class DocumentActivity extends AppCompatActivity {
    SharedPreferences sp;
    String vendorid="",branchid="";
    Retro retrofits;
    Toolbar toolbar;
    RecyclerView recyclerview_document;
    ArrayList<Document>documentlist;
    ImageView imgview_document;
    String path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        toolbar=findViewById(R.id.toolbar);
        recyclerview_document=findViewById(R.id.recyclerview_document);
        imgview_document=findViewById(R.id.imgview_document);
        sp = getSharedPreferences("config_info", 0);
        vendorid=sp.getString("vendorid","");
        branchid=sp.getString("branchid","");
        path=getIntent().getStringExtra("path");
        if(path!=null)
        {
            imgview_document.setVisibility(View.VISIBLE);
            Glide.with(DocumentActivity.this).load(path).into(imgview_document);
        }
        retrofits=new Retro();
        configuretoolbar();
        InvokeService();

    }

    public void InvokeService() {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetVendorDocumentInfo(vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                documentlist=new ArrayList<>();
                if(response.isSuccessful())
                {
                    try {
                        String strresponse=response.body().string();
                        if(!strresponse.equals("[]")) {
                            JSONArray jsonArray = new JSONArray(strresponse);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String Documentid = jsonObject.getString("DocumentID");
                                String Title = jsonObject.getString("DocTitle");
                                String Imagepath = "http://167.86.86.78/SingleStoreApi/" + jsonObject.getString("DocPath").replace("~", "") + "/" + Title;
                                documentlist.add(new Document(Documentid, Title, Imagepath));
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.VERTICAL, false);
                            DocumentAdapter documentAdapter = new DocumentAdapter(DocumentActivity.this, documentlist);
                            recyclerview_document.setLayoutManager(layoutManager);
                            recyclerview_document.setAdapter(documentAdapter);
                        }
                        else
                        {
                            Toast.makeText(DocumentActivity.this,"No document added to your profile",Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (IOException e) {
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

    @SuppressLint("RestrictedApi")
    private void configuretoolbar() {
        setSupportActionBar(toolbar);
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
