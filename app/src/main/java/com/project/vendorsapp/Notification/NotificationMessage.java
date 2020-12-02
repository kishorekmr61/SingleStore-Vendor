package com.project.vendorsapp.Notification;



public class NotificationMessage {
    String notificationmessage;
    String Date;
    String Time;
    String Title;


    public NotificationMessage(String notificationmessage, String date, String time, String title) {
        this.notificationmessage = notificationmessage;
        Date = date;
        Time = time;
        Title=title;
    }

    public String getNotificationmessage() {
        return notificationmessage;
    }

    public void setNotificationmessage(String notificationmessage) {
        this.notificationmessage = notificationmessage;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
