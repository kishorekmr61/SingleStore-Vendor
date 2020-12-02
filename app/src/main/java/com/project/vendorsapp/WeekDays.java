package com.project.vendorsapp;

public class WeekDays {
    String name;
    String id;
    int code;

    public WeekDays(String name, String id,int code) {
        this.name = name;
        this.id = id;
        this.code=code;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getcode() {
        return code;
    }

    public void setcode(int code) {
        this.code = code;
    }


}
