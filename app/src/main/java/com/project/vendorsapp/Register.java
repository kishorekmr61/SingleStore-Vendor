package com.project.vendorsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Register implements Parcelable {
    private int categoryid;
    private int sellertypeid;
    private String bussinessname;
    private String Tinno;
    private String midno;
    private String tidno;
    private String gstno;
    private String ownername;
    private int countrycode;
    private int statecode;
    private int cityid;
    private String addressline1;
    private String addressline2;
    private String zipcode;
    private String mobileno;
    private String altrmobileno;
    private String Imagepath;


    protected Register(Parcel in) {
        categoryid = in.readInt();
        sellertypeid = in.readInt();
        bussinessname = in.readString();
        Tinno = in.readString();
        midno = in.readString();
        tidno = in.readString();
        gstno = in.readString();
        ownername = in.readString();
        countrycode = in.readInt();
        statecode = in.readInt();
        cityid = in.readInt();
        addressline1 = in.readString();
        addressline2 = in.readString();
        zipcode = in.readString();
        mobileno = in.readString();
        altrmobileno = in.readString();
        Imagepath = in.readString();
    }

    public static final Creator<Register> CREATOR = new Creator<Register>() {
        @Override
        public Register createFromParcel(Parcel in) {
            return new Register(in);
        }

        @Override
        public Register[] newArray(int size) {
            return new Register[size];
        }
    };

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getSellertypeid() {
        return sellertypeid;
    }

    public void setSellertypeid(int sellertypeid) {
        this.sellertypeid = sellertypeid;
    }

    public String getBussinessname() {
        return bussinessname;
    }

    public void setBussinessname(String bussinessname) {
        this.bussinessname = bussinessname;
    }

    public String getTinno() {
        return Tinno;
    }

    public void setTinno(String tinno) {
        Tinno = tinno;
    }

    public String getMidno() {
        return midno;
    }

    public void setMidno(String midno) {
        this.midno = midno;
    }

    public String getTidno() {
        return tidno;
    }

    public void setTidno(String tidno) {
        this.tidno = tidno;
    }

    public String getGstno() {
        return gstno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public int getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(int countrycode) {
        this.countrycode = countrycode;
    }

    public int getStatecode() {
        return statecode;
    }

    public void setStatecode(int statecode) {
        this.statecode = statecode;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAltrmobileno() {
        return altrmobileno;
    }

    public void setAltrmobileno(String altrmobileno) {
        this.altrmobileno = altrmobileno;
    }

    public String getImagepath() {
        return Imagepath;
    }

    public void setImagepath(String imagepath) {
        Imagepath = imagepath;
    }


    public Register(int categoryid, int sellertypeid, String bussinessname, String tinno, String midno, String tidno, String gstno, String ownername, int countrycode, int statecode, int cityid, String addressline1, String addressline2, String zipcode, String mobileno, String altrmobileno, String imagepath) {
        this.categoryid = categoryid;
        this.sellertypeid = sellertypeid;
        this.bussinessname = bussinessname;
        Tinno = tinno;
        this.midno = midno;
        this.tidno = tidno;
        this.gstno = gstno;
        this.ownername = ownername;
        this.countrycode = countrycode;
        this.statecode = statecode;
        this.cityid = cityid;
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
        this.zipcode = zipcode;
        this.mobileno = mobileno;
        this.altrmobileno = altrmobileno;
        Imagepath = imagepath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryid);
        dest.writeInt(sellertypeid);
        dest.writeString(bussinessname);
        dest.writeString(Tinno);
        dest.writeString(midno);
        dest.writeString(tidno);
        dest.writeString(gstno);
        dest.writeString(ownername);
        dest.writeInt(countrycode);
        dest.writeInt(statecode);
        dest.writeInt(cityid);
        dest.writeString(addressline1);
        dest.writeString(addressline2);
        dest.writeString(zipcode);
        dest.writeString(mobileno);
        dest.writeString(altrmobileno);
        dest.writeString(Imagepath);


    }
}
