package com.project.vendorsapp.Notification;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.project.vendorsapp.R;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class NotificationActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    Toolbar mToolbar;
    ImageView imgview_logo;
    TextView screenname_txt;
    LinearLayoutManager mlinearlayoutmanager;
    MessageAdapter messageAdapter;
    ArrayList<NotificationMessage> messagelist=new ArrayList<>();
    Gson gson;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=findViewById(R.id.recyclerview);
        mToolbar=findViewById(R.id.toolbar);
        preferences = getSharedPreferences("config_info", 0);
        editor=preferences.edit();
        gson=new Gson();
        String message=preferences.getString("MyObject",null);
        if(message!=null)
        {
          Type messagetype =new TypeToken<ArrayList<NotificationMessage>>(){}.getType();
          messagelist.clear();
          messagelist=gson.fromJson(message,messagetype);

        }
        configuretoolbar();
        Build();


    }

    public void Build()
    {
       mlinearlayoutmanager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false) ;
       messageAdapter=new MessageAdapter(getApplicationContext(),messagelist);
       recyclerView.setLayoutManager(mlinearlayoutmanager);
       recyclerView.setAdapter(messageAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position=viewHolder.getAdapterPosition();
                messagelist.remove(position);
                String json = gson.toJson(messagelist);
                editor.putString("MyObject", json);
                editor.commit();
                messageAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @SuppressLint("RestrictedApi")
    private void configuretoolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        imgview_logo=mToolbar.findViewById(R.id.imgview_logo);
        Glide.with(NotificationActivity.this).load(R.mipmap.molslogo).into(imgview_logo);

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
