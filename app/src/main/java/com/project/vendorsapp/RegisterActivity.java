package com.project.vendorsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {
    ImageView imgview_store, imgview_logo;
    Button btn_upload, btn_next;
    //  private static int RESULT_LOAD_IMAGE = 123;
    // protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    EditText edttxt_bussinessname, edttxt_tinno, edttxt_midno, edttxt_tidno, edttxt_gstno;
    Spinner spinner_category, spinner_sellertype;
    ArrayList<String> categorylist;
    ArrayList<String> sellertypelist;
    ArrayList<Datalist> Categorylist;
    ArrayList<Datalist> Sellertypelist;
    ArrayList<Register> registerlist;
    AwesomeValidation validation;
    Retro retrofits;
    ProgressDialog dialog;
    String picturePath;
    String categoryid;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    String sellertypeid;
    Uri selectedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        checkForPermission();
        imgview_store = findViewById(R.id.imgview_store);
        btn_upload = findViewById(R.id.btn_upload);
        edttxt_bussinessname = findViewById(R.id.edttxt_businessname);
        edttxt_tinno = findViewById(R.id.edttxt_Tinno);
        edttxt_midno = findViewById(R.id.edttxt_midno);
        edttxt_tidno = findViewById(R.id.edttxt_tidno);
        edttxt_gstno = findViewById(R.id.edttxt_gstno);
        spinner_category = findViewById(R.id.spinner_category);
        spinner_sellertype = findViewById(R.id.spinner_sellertype);
        imgview_logo = findViewById(R.id.imgview_logo);
        btn_next = findViewById(R.id.btn_next);
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        retrofits = new Retro();
        categorylist = new ArrayList<>();
        sellertypelist = new ArrayList<>();
        Categorylist = new ArrayList<>();
        Sellertypelist = new ArrayList<>();
        registerlist = new ArrayList<>();
        Glide.with(getApplicationContext())
                .load(R.mipmap.molslogo)
                .into(imgview_logo);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        InvokeService();
        dialog.show();


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
                pickFromGallery();
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String path="null";
                    validation.addValidation(RegisterActivity.this, R.id.edttxt_businessname, RegexTemplate.NOT_EMPTY, R.string.busssinessnameerror);
                    //validation.addValidation(RegisterActivity.this, R.id.edttxt_Tinno, RegexTemplate.NOT_EMPTY, R.string.tinnoerror);
                    //validation.addValidation(RegisterActivity.this, R.id.edttxt_midno, RegexTemplate.NOT_EMPTY, R.string.midnoerror);
                    //validation.addValidation(RegisterActivity.this, R.id.edttxt_tidno, RegexTemplate.NOT_EMPTY, R.string.tidnoerror);
                    //validation.addValidation(RegisterActivity.this, R.id.edttxt_gstno, RegexTemplate.NOT_EMPTY, R.string.gstnoerror);
                    if (validation.validate()) {
                        if (!categoryid.equalsIgnoreCase("1080") && !sellertypeid.equalsIgnoreCase("1080")) {
                           // if (selectedImage != null) {
                                if(selectedImage != null) {
                                    path = getRealPathFromURIPath(selectedImage);
                                }
                                registerlist.add(new Register(0, 0, "", "", "", "", "", "", 0, 0, 0, "", "", "", "", "", ""));
                                registerlist.get(0).setBussinessname(edttxt_bussinessname.getText().toString());
                                registerlist.get(0).setCategoryid(Integer.parseInt(categoryid));
                                registerlist.get(0).setSellertypeid(Integer.parseInt(sellertypeid));
                                registerlist.get(0).setTinno(edttxt_tinno.getText().toString());
                                registerlist.get(0).setGstno(edttxt_gstno.getText().toString());
                                registerlist.get(0).setMidno(edttxt_midno.getText().toString());
                                registerlist.get(0).setTidno(edttxt_tidno.getText().toString());

                                    registerlist.get(0).setImagepath(path);


                                Intent intent = new Intent(RegisterActivity.this, Register1Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putParcelableArrayListExtra("list", registerlist);
                                startActivity(intent);
                           // } //else {
                               // Toast.makeText(RegisterActivity.this, "Please select Image", Toast.LENGTH_SHORT).show();
                           // }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Please select category and seller type", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    private void pickFromGallery() {

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             //requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
=======
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
>>>>>>> 9e5ddb166a52bdb23b5d31b5d4b3149d4e58d542
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_SELECT_PICTURE);
        }*/
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_PICTURE);
    }


    public void checkForPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Log.e("testing", "Permission is granted");
                } else {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION//,
                                    /*Manifest.permission.CAMERA*/}, 1);
                    //Log.e("testing", "Permission is revoked");
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                //Log.e("testing", "Permission is already granted");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, permission)) {

            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE//,
                            /*Manifest.permission.CAMERA*/}, 1);
//            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(RegisterActivity.this,
//                                    new String[]{permission}, requestCode);
//                        }
//                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{permission}, requestCode);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            try {
                selectedImage = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(RegisterActivity.this, selectedImage, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                imgview_store.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath, options));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private String getRealPathFromURIPath(Uri contentURI) {
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


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
        Call<ResponseBody> response = service.Categories();
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    String strresponse = response.body().string();
                    categorylist.clear();
                    Categorylist.clear();
                    categorylist.add("Bussiness Category");
                    Categorylist.add(new Datalist("1080", "Bussiness Category"));
                    JSONArray array = new JSONArray(strresponse);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String categoryid = jsonObject.getString("CategoryID");
                        String categoryname = jsonObject.getString("CategoryName");
                        categorylist.add(categoryname);
                        Categorylist.add(new Datalist(categoryid, categoryname));
                    }
                    BindingSpinner(categorylist, spinner_category);

                } catch (IOException e) {
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

    public void InvokeSellerType() {

        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetSellerType();
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse = response.body().string();
                    sellertypelist.clear();
                    sellertypelist.add("Seller Type");
                    Sellertypelist.clear();
                    Sellertypelist.add(new Datalist("1080", "Seller Type"));
                    JSONArray jsonArray = new JSONArray(strresponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("SellerTypeID");
                        String title = object.getString("Title");
                        sellertypelist.add(title);
                        Sellertypelist.add(new Datalist(id, title));
                    }
                    SellerTypeSpinner();
                } catch (IOException e) {
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

    public void BindingSpinner(ArrayList<String> list, Spinner spinner) {
        ArrayAdapter adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryid = Categorylist.get(position).getId();
                InvokeSellerType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void SellerTypeSpinner() {
        ArrayAdapter adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_list_item_1, sellertypelist);
        spinner_sellertype.setAdapter(adapter);
        spinner_sellertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sellertypeid = Sellertypelist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
