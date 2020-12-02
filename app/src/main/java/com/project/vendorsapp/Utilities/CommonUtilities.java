package com.project.vendorsapp.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class CommonUtilities {
    public static String url="http://shinwariapi.fadelsoft.com/";
   // public static String url2="https://molsindia.fadelsoft.com/";
//    public static String url="http://167.86.86.78/SingleStoreApi/";
    public static String url2="http://api.isbazaar.com/";

    public static String url1="http://shinwari.fadelsoft.com/";//test

    public static ProgressDialog dialog(Context context)
    {
        ProgressDialog dialog=new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        return dialog;

    }

    public static void ToastMessage(Context mcontext,String message)
    {
        Toast.makeText(mcontext,message,Toast.LENGTH_SHORT).show();
    }

    /*CONVERTING INTO BASE64*/
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap pickImageFromGallery(Intent data, Activity activity) {
        try {
            if (null != data) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(activity, selectedImageUri, projection, null, null, null);
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
                return BitmapFactory.decodeFile(selectedImagePath, options);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
