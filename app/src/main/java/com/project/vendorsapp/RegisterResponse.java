package com.project.vendorsapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class RegisterResponse {

    @Expose
    @SerializedName("Amount")
    private int Amount;
    @Expose
    @SerializedName("CompltedCategories")
    private boolean CompltedCategories;
    @Expose
    @SerializedName("CompltedSettings")
    private boolean CompltedSettings;
    @Expose
    @SerializedName("avatar")
    private String avatar;
    @Expose
    @SerializedName("AccountTypeID")
    private int AccountTypeID;
    @Expose
    @SerializedName("CategoryID")
    private int CategoryID;
    @Expose
    @SerializedName("BranchID")
    private int BranchID;
    @Expose
    @SerializedName("VendorID")
    private int VendorID;
}
