package com.project.vendorsapp;

public class DeliverBoyList {
    String id="";
    String name="";
    String MobileNo="";
    String Address="";
    String CategoryID="";



    public DeliverBoyList(String id, String name, String mobileNo, String address, String categoryID) {
        this.id = id;
        this.name = name;
        MobileNo = mobileNo;
        Address = address;
        CategoryID = categoryID;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }


}
