package com.project.vendorsapp.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.project.vendorsapp.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    int FM_NOTIFICATION_ID = 10;
    String notifications = "", jsonnotifications = "", Strtitle = "", Strbody = "", Strvendorid = "", Strorderid = "", Strbranchid = "", Strcategory = "";
    SharedPreferences sp;
    Intent notificationIntent;
    SharedPreferences.Editor editor;
    public static final String NOTIFICATION_CHANNEL_ID = "MolsPay";
    ArrayList<NotificationMessage> messageList=new ArrayList<>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sp = getSharedPreferences("config_info", 0);
        editor = sp.edit();

        notificationIntent = new Intent();

        try {
            jsonnotifications = sp.getString("Notifications", "");
//            if (!jsonnotifications.isEmpty() && !jsonnotifications.equalsIgnoreCase("null")) {
//                JSONArray jsonArray = new JSONArray(jsonnotifications);
//                getSaveUsernotifications = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Usernotifications>>() {
//                }.getType());
//                for (int N = 0; N < getSaveUsernotifications.size(); N++) {
//                    saveUsernotifications.add(new Usernotifications(getSaveUsernotifications.get(N).getTitle(), getSaveUsernotifications.get(N).getMessage()));
//                }
//            }
            Map<String, String> data = remoteMessage.getData();
            Log.v("Messages", data.toString());
            try {
                Strtitle = data.get("title");
                if (Strtitle.equalsIgnoreCase("null")) {
                    Strtitle = "";
                }
            } catch (Exception ex) {
                Strtitle = "";
            }

            try {
                Strbody = data.get("body");
                if (Strbody.equalsIgnoreCase("null")) {
                    Strbody = "";
                }
            } catch (Exception ex) {
                Strbody = "";
            }
//            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.message);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
////                        .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true)
                   // .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentTitle(Strtitle)
                    .setLargeIcon(largeIcon)
                   .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentText(Strbody/*title*/);
            Gson gson = new Gson();
            String message=sp.getString("MyObject",null);
            if(message!=null)
            {
                Type messagetype =new TypeToken<ArrayList<NotificationMessage>>(){}.getType();
                messageList=gson.fromJson(message,messagetype);

            }
            Calendar calander = Calendar.getInstance();
            String Date= String.valueOf(calander.get(Calendar.DAY_OF_MONTH))+"."+ String.valueOf(calander.get(Calendar.MONTH)+1)+"."+ String.valueOf(calander.get(Calendar.YEAR));
            String Time= String.valueOf(calander.get(Calendar.HOUR))+":"+ String.valueOf(calander.get(Calendar.MINUTE))+":"+ String.valueOf(calander.get(Calendar.SECOND));
            messageList.add(0,new NotificationMessage(Strbody,Date,Time,Strtitle));
            String json = gson.toJson(messageList);
            editor.putString("MyObject", json);
            editor.commit();
            String notificationmessage;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setColor(getResources().getColor(R.color.lightgrey));
            } else {
                builder.setSmallIcon(R.mipmap.ic_launcher);
            }

           PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, NotificationActivity.class),0);
            builder.setContentIntent(contentIntent);

            // Add as notification

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, getResources().getString(R.string.app_name), importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
                assert manager != null;
                builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                manager.createNotificationChannel(notificationChannel);
            }
            manager.notify(FM_NOTIFICATION_ID, builder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


