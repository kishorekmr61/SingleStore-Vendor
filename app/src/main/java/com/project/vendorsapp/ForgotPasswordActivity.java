package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.project.vendorsapp.Utilities.CommonUtilities;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edttxt_mobileno;
    Button btn_submit;
    AwesomeValidation validation;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        edttxt_mobileno=findViewById(R.id.edttxt_mobileno);
        //CommonUtilities.url="https://molsindia.fadelsoft.com/";
        btn_submit=findViewById(R.id.btn_submit);
        progressDialog=CommonUtilities.dialog(ForgotPasswordActivity.this);
        validation=new AwesomeValidation(ValidationStyle.BASIC);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(edttxt_mobileno.getText().toString().length()==10)
        {
            validation.addValidation(ForgotPasswordActivity.this, R.id.edttxt_mobileno, RegexTemplate.NOT_EMPTY
                    , R.string.mobileerror);
            if(validation.validate())
            {
                Intent intent=new Intent(ForgotPasswordActivity.this,GetPasswordActivity.class) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mobileno",edttxt_mobileno.getText().toString());
                startActivity(intent);
            }
        }
      else{
            Toast.makeText(ForgotPasswordActivity.this,"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
        }

    }
}
