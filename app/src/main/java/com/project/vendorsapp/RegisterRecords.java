package com.project.vendorsapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class RegisterRecords {

    @Expose
    @SerializedName("EconomyPaymentType")
    public int EconomyPaymentType;
    @Expose
    @SerializedName("Amount")
    public double Amount;
    @Expose
    @SerializedName("SalesManID")
    public int SalesManID;
    @Expose
    @SerializedName("PaymentTypeID")
    public int PaymentTypeID;
    @Expose
    @SerializedName("AccountTypeID")
    public int AccountTypeID;
    @Expose
    @SerializedName("SmsPlanID")
    public int SmsPlanID;
    @Expose
    @SerializedName("MemoryPlanID")
    public int MemoryPlanID;
    @Expose
    @SerializedName("Documents")
    public List<Documents> Documents;
    @Expose
    @SerializedName("MapGeoLocationID")
    public String MapGeoLocationID;
    @Expose
    @SerializedName("MobileDeviceID")
    public String MobileDeviceID;
    @Expose
    @SerializedName("BranchID")
    public int BranchID;
    @Expose
    @SerializedName("Avatar")
    public Avatar Avatar;
    @Expose
    @SerializedName("GeoLatitude")
    public String GeoLatitude;
    @Expose
    @SerializedName("GeoLongitude")
    public String GeoLongitude;
    @Expose
    @SerializedName("CategoryID")
    public int CategoryID;
    @Expose
    @SerializedName("Zipcode")
    public String Zipcode;
    @Expose
    @SerializedName("CountryID")
    public int CountryID;
    @Expose
    @SerializedName("CityID")
    public int CityID;
    @Expose
    @SerializedName("aliascategoryid")
    public int aliascategoryid;
    @Expose
    @SerializedName("StateID")
    public int StateID;
    @Expose
    @SerializedName("AddressLine2")
    public String AddressLine2;
    @Expose
    @SerializedName("AddressLine1")
    public String AddressLine1;
    @Expose
    @SerializedName("BusinessGSTNo")
    public String BusinessGSTNo;
    @Expose
    @SerializedName("BusinessTIDNo")
    public String BusinessTIDNo;
    @Expose
    @SerializedName("BusinessMIDNo")
    public String BusinessMIDNo;
    @Expose
    @SerializedName("BusinessTANNo")
    public String BusinessTANNo;
    @Expose
    @SerializedName("SellerTypeID")
    public int SellerTypeID;
    @Expose
    @SerializedName("AltMobileNo")
    public String AltMobileNo;
    @Expose
    @SerializedName("MobileNo")
    public String MobileNo;
    @Expose
    @SerializedName("OwnerName")
    public String OwnerName;
    @Expose
    @SerializedName("BusinessName")
    public String BusinessName;

    public static class Documents {
        @Expose
        @SerializedName("Buffer")
        public String Buffer;
        @Expose
        @SerializedName("MediaType")
        public String MediaType;
        @Expose
        @SerializedName("FileName")
        public String FileName;
    }

    public static class Avatar {
        @Expose
        @SerializedName("Buffer")
        public String Buffer;
        @Expose
        @SerializedName("MediaType")
        public String MediaType;
        @Expose
        @SerializedName("FileName")
        public String FileName;
    }

    public List<RegisterRecords.Documents> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<RegisterRecords.Documents> documents) {
        Documents = documents;
    }

    public RegisterRecords.Avatar getAvatar() {
        return Avatar;
    }

    public void setAvatar(RegisterRecords.Avatar avatar) {
        Avatar = avatar;
    }
}
