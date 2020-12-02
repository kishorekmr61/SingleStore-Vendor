package com.project.vendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.vendorsapp.Utilities.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register2Activity extends AppCompatActivity {


    RadioButton radiobtn_saleperson;
    Spinner spinner_salesperson;
    Retro retrofits;
    ArrayList<String> salespersonlist;
    ArrayList<Datalist> Salespersonlist;
    ArrayList<Register> registerlist;
    String salepersonid = "";
    Button btn_register;
    JSONObject jsonObject;
    JSONArray jsonArray;
    MultipartBody.Part body;
    ArrayList<Uri> pathlist;
    String extension;
    ProgressDialog progressDialog;
    ImageView imgview_document1, imgview_document2;
    int REQUEST_SELECT_PICTURE1 = 1;
    int REQUEST_SELECT_PICTURE2 = 2;
    String lat, lang;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        radiobtn_saleperson = findViewById(R.id.radiobtn_salesperson);
        spinner_salesperson = findViewById(R.id.spinner_salesperson);
        imgview_document1 = findViewById(R.id.imgview_document1);
        imgview_document2 = findViewById(R.id.imgview_document2);
        btn_register = findViewById(R.id.btn_register);
        retrofits = new Retro();
        salespersonlist = new ArrayList<>();
        Salespersonlist = new ArrayList<>();
        registerlist = new ArrayList<>();
        pathlist = new ArrayList<>();
        registerlist = getIntent().getParcelableArrayListExtra("list");
        progressDialog = new ProgressDialog(Register2Activity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        lat = getIntent().getStringExtra("lat");
        lang = getIntent().getStringExtra("lang");
        imgview_document1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery(REQUEST_SELECT_PICTURE1);

            }
        });

        imgview_document2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery(REQUEST_SELECT_PICTURE2);
            }
        });
        radiobtn_saleperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radiobtn_saleperson.isChecked()) {
                    spinner_salesperson.setVisibility(View.VISIBLE);
                    InvokeService();
                } else {
                    spinner_salesperson.setVisibility(View.GONE);
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonObject();

            }
        });


    }

    private void pickFromGallery(int request) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PICTURE1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            pathlist.add(0, selectedImage);
            try {
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor coursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                coursor.moveToFirst();
                int columnIndex = coursor.getColumnIndex(filePath[0]);
                String picturePath = coursor.getString(columnIndex);
                File imageFile = new File(picturePath);
                imgview_document1.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_SELECT_PICTURE2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            pathlist.add(1, selectedImage);
            try {
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor coursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                coursor.moveToFirst();
                int columnIndex = coursor.getColumnIndex(filePath[0]);
                String picturePath = coursor.getString(columnIndex);
                File imageFile = new File(picturePath);
                imgview_document2.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURIPath(Uri contentURI) {
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) {
//            return contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            String document_id = cursor.getString(0);
//            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//            cursor.close();
//            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID
//                    + "=?", new String[]{document_id}, null);
//            //  int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            // return cursor.getString(idx);
//            cursor.moveToFirst();
//            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            cursor.close();
//            return path;
//        }
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(contentURI, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }


    public void InvokeService() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetSalesPersons();
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse = response.body().string();
                    salespersonlist.clear();
                    Salespersonlist.clear();
                    JSONArray jsonArray = new JSONArray(strresponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String salesemployeeid = object.getString("SALESEMPLOYEEID");
                        String salesperson = object.getString("EMPLOYEENAME");
                        salespersonlist.add(salesperson);
                        Salespersonlist.add(new Datalist(salesemployeeid, salesperson));
                    }
                    SpinnerBinding();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(Register2Activity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register2Activity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void SpinnerBinding() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(Register2Activity.this, android.R.layout.simple_list_item_1, salespersonlist);
        spinner_salesperson.setAdapter(arrayAdapter);
        spinner_salesperson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                salepersonid = Salespersonlist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

 /*   public void InvokeServiceRegister() {
        try {

            Retrofit retrofit = retrofits.call();
            RegisterService service = retrofit.create(RegisterService.class);
            Call<RegisterResponse> response = service.PostVendor(getRegisterdata());
            response.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            Toast.makeText(Register2Activity.this, "Registration is done successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register2Activity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(Register2Activity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(Register2Activity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register2Activity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

  private RegisterRecords getRegisterdata() {
        RegisterRecords registerRecords = new RegisterRecords();
        try {
            registerRecords.EconomyPaymentType = 1;
            registerRecords.Amount = 1.0;
            registerRecords.SalesManID = Integer.parseInt(salepersonid);
            registerRecords.PaymentTypeID = 1;
            registerRecords.AccountTypeID = 1;
            registerRecords.SmsPlanID = 1;
            registerRecords.MemoryPlanID = 1;
            List<RegisterRecords.Documents> documents= new ArrayList<>();
            RegisterRecords.Documents documents1 = new RegisterRecords.Documents();
            documents1.Buffer = CommonUtilities.encodeToBase64(converttobitmap(), Bitmap.CompressFormat.JPEG, 80);
            documents1.FileName = registerlist.get(0).getImagepath().substring(registerlist.get(0).getImagepath().lastIndexOf("/") + 1);
            documents1.MediaType = "image/*";
            documents.add(documents1);
            registerRecords.setDocuments(documents);
            registerRecords.MapGeoLocationID = "1";
            registerRecords.MobileDeviceID = "1";
            registerRecords.BranchID = 30093;
            RegisterRecords.Avatar avatar = new RegisterRecords.Avatar();
            avatar.Buffer = CommonUtilities.encodeToBase64(converttobitmap(), Bitmap.CompressFormat.JPEG, 80);
            avatar.FileName = registerlist.get(0).getImagepath().substring(registerlist.get(0).getImagepath().lastIndexOf("/") + 1);
            avatar.MediaType = "image/*";
            registerRecords.Avatar = avatar;
            registerRecords.GeoLatitude = "22.25";
            registerRecords.GeoLongitude = "22.25";
            registerRecords.CategoryID = registerlist.get(0).getCategoryid();
            registerRecords.Zipcode = registerlist.get(0).getZipcode();
            registerRecords.CountryID = registerlist.get(0).getCountrycode();
            registerRecords.CityID = registerlist.get(0).getCityid();
            registerRecords.aliascategoryid = 1;
            registerRecords.StateID = registerlist.get(0).getStatecode();
            registerRecords.AddressLine2 = registerlist.get(0).getAddressline2();
            registerRecords.AddressLine1 = registerlist.get(0).getAddressline1();
            registerRecords.BusinessGSTNo = registerlist.get(0).getGstno();
            registerRecords.BusinessTIDNo = registerlist.get(0).getTidno();
            registerRecords.BusinessMIDNo = registerlist.get(0).getMidno();
            registerRecords.BusinessTANNo = registerlist.get(0).getTinno();
            registerRecords.SellerTypeID = registerlist.get(0).getSellertypeid();
            registerRecords.BusinessName = registerlist.get(0).getBussinessname();
            registerRecords.AltMobileNo = registerlist.get(0).getAltrmobileno();
            registerRecords.MobileNo =  "+91" + registerlist.get(0).getMobileno();
            registerRecords.OwnerName = registerlist.get(0).getOwnername();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return registerRecords;
    }*/


    private Bitmap converttobitmap(String path) {
        Bitmap bitmap = null;
        try {
            File selected = new File(path);
            Uri selectedUri = Uri.fromFile(selected);
            return BitmapFactory.decodeFile(selectedUri.getPath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;


    }

    public void JsonObject() {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("BusinessName", registerlist.get(0).getBussinessname());
            object.addProperty("OwnerName", registerlist.get(0).getOwnername());
            object.addProperty("MobileNo", "+91" + registerlist.get(0).getMobileno());
            object.addProperty("AltMobileNo", registerlist.get(0).getAltrmobileno());
            object.addProperty("SellerTypeID", registerlist.get(0).getSellertypeid());
            object.addProperty("BusinessTANNo", registerlist.get(0).getTinno());
            object.addProperty("BusinessMIDNo", registerlist.get(0).getMidno());
            object.addProperty("BusinessTIDNo", registerlist.get(0).getTidno());
            object.addProperty("BusinessGSTNo", registerlist.get(0).getGstno());
            object.addProperty("AddressLine1", registerlist.get(0).getAddressline1());
            object.addProperty("AddressLine2", registerlist.get(0).getAddressline2());
            object.addProperty("BranchID", "30093");
            object.addProperty("StateID", registerlist.get(0).getStatecode());
            object.addProperty("aliascategoryid", "1");
            object.addProperty("CityID", registerlist.get(0).getCityid());
            object.addProperty("CountryID", registerlist.get(0).getCountrycode());
            object.addProperty("Zipcode", registerlist.get(0).getZipcode());
            object.addProperty("CategoryID", registerlist.get(0).getCategoryid());
            object.addProperty("GeoLongitude", String.valueOf(lang));
            object.addProperty("GeoLatitude", String.valueOf(lat));
            object.addProperty("MobileDeviceID", "1");
            object.addProperty("MapGeoLocationID", "1");
            object.addProperty("MemoryPlanID", "1");
            object.addProperty("SmsPlanID", "1");
            object.addProperty("AccountTypeID", "1");
            object.addProperty("PaymentTypeID", "1");
            object.addProperty("SalesManID", salepersonid);
            object.addProperty("Amount", "1");
            object.addProperty("EconomyPaymentType", "1");
            JsonArray jsonArray = new JsonArray();
            if(pathlist.size()>0) {
                for (int i = 0; i < 2; i++) {
                    JsonObject jsonObject = new JsonObject();
                    extension = getRealPathFromURIPath(pathlist.get(i)).substring(getRealPathFromURIPath(pathlist.get(i)).lastIndexOf(".") + 1);
                    jsonObject.addProperty("FileName", getRealPathFromURIPath(pathlist.get(i)).substring(getRealPathFromURIPath(pathlist.get(i)).lastIndexOf("/") + 1));
                    jsonObject.addProperty("MediaType", extension);
                    jsonObject.addProperty("Buffer", CommonUtilities.encodeToBase64(converttobitmap(getRealPathFromURIPath(pathlist.get(i))), Bitmap.CompressFormat.JPEG, 80));
                    jsonArray.add(jsonObject);
                }

            }

            JsonObject jsonObject1 = new JsonObject();
            if(!registerlist.get(0).getImagepath().equalsIgnoreCase("null")) {
                jsonObject1.addProperty("FileName", registerlist.get(0).getImagepath().substring(registerlist.get(0).getImagepath().lastIndexOf("/") + 1));
                jsonObject1.addProperty("MediaType", extension);
                jsonObject1.addProperty("Buffer", CommonUtilities.encodeToBase64(converttobitmap(registerlist.get(0).getImagepath()), Bitmap.CompressFormat.JPEG, 80));
            }
            object.add("Avatar", jsonObject1);
            object.add("Documents", jsonArray);
            progressDialog.show();
            InvokeServiceRegister1(object);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void InvokeServiceRegister1(JsonObject object) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.PostVendor(object);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(Register2Activity.this, "Registration is done successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register2Activity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Register2Activity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }


}
