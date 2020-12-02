package com.project.vendorsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lnrlayout_storeinfo,lnrlayout_storemessage,lnrlayout_ordertype,lnrlayout_otherinfo,lnrlayout_storeholiday,lnrlayout_bankdetails,lnrlayout_document;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        lnrlayout_storeinfo=findViewById(R.id.lnrlayout_storeinfo);
        lnrlayout_storemessage=findViewById(R.id.lnrlayout_storemsg);
        lnrlayout_ordertype=findViewById(R.id.lnrlayout_ordertype);
        lnrlayout_otherinfo=findViewById(R.id.lnrlayout_otherinfo);
        lnrlayout_storeholiday=findViewById(R.id.lnrlayout_storeholiday);
        lnrlayout_bankdetails=findViewById(R.id.lnrlayout_bankdetail);
        lnrlayout_document=findViewById(R.id.lnrlayout_documents);
        lnrlayout_storeinfo.setOnClickListener(this);
        lnrlayout_storemessage.setOnClickListener(this);
        lnrlayout_storeholiday.setOnClickListener(this);
        lnrlayout_ordertype.setOnClickListener(this);
        lnrlayout_otherinfo.setOnClickListener(this);
        lnrlayout_bankdetails.setOnClickListener(this);
        lnrlayout_document.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnrlayout_storeinfo:
            Intent intent = new Intent(SettingActivity.this, StoreInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            break;

            case R.id.lnrlayout_storemsg:

                Intent intent1 = new Intent(SettingActivity.this, StoreMessageActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;

            case R.id.lnrlayout_ordertype:

                Intent intent2 = new Intent(SettingActivity.this,OrderTypeActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;

            case R.id.lnrlayout_otherinfo:
                Intent intent3 = new Intent(SettingActivity.this,OtherInfoActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                break;


            case R.id.lnrlayout_storeholiday:
                Intent intent4 = new Intent(SettingActivity.this,CreateHolidaysActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent4);
                break;

            case R.id.lnrlayout_bankdetail:
                Intent intent5 = new Intent(SettingActivity.this,BankDetailActivity.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent5);
                break;

            case R.id.lnrlayout_documents:
                Intent intent6 = new Intent(SettingActivity.this,DocumentActivity.class);
                intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent6);
                break;

        }
    }
}
