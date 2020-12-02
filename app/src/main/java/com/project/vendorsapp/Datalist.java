package com.project.vendorsapp;

public class Datalist {
    String Id;
    String Name;
    String Message;
    String Messageid;
    String Messageactive;


    public Datalist(String message, String messageid, String messageactive) {
        Message = message;
        Messageid = messageid;
        Messageactive = messageactive;
    }






    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageid() {
        return Messageid;
    }

    public void setMessageid(String messageid) {
        Messageid = messageid;
    }

    public String getMessageactive() {
        return Messageactive;
    }

    public void setMessageactive(String messageactive) {
        Messageactive = messageactive;
    }



    public Datalist(String id, String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
