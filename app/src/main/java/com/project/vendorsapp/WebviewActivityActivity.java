package com.project.vendorsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class WebviewActivityActivity extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferences sp;
    String vendorid, branchid;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_activity);
        WebView browser = (WebView) findViewById(R.id.webview);

        dialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
        TextView txtview_heading = findViewById(R.id.txtview_heading);
        txtview_heading.setText("Inventory");
        sp = getSharedPreferences("config_info", 0);
        vendorid = sp.getString("vendorid", "");
        branchid = sp.getString("branchid", "");
        configuretoolbar();
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().getDefaultFontSize();
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getScale();
        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        browser.loadUrl("http://vendor.shopcircle.in/VendorAdmin/addinventoryapp.html?vendorid=" + vendorid + "&branchid=" + branchid);

    }

    @SuppressLint("RestrictedApi")
    private void configuretoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }


}
