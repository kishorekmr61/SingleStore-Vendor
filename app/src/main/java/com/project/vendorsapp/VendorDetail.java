package com.project.vendorsapp;

public class VendorDetail {
    String VendorID;
    String BusinessName;


    public VendorDetail(String vendorID, String businessName) {
        VendorID = vendorID;
        BusinessName = businessName;
    }

    public String getVendorID() {
        return VendorID;
    }

    public void setVendorID(String vendorID) {
        VendorID = vendorID;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }



}
