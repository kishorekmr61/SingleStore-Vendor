package com.project.vendorsapp.Classes;

public class Holiday {
    String HolidayDescription;
    String StoreStatus;
    String HolidayDate;
    String HolidayID;

    public Holiday(String holidayDescription, String storeStatus, String holidayDate, String holidayID) {
        HolidayDescription = holidayDescription;
        StoreStatus = storeStatus;
        HolidayDate = holidayDate;
        HolidayID = holidayID;
    }

    public String getHolidayDescription() {
        return HolidayDescription;
    }

    public void setHolidayDescription(String holidayDescription) {
        HolidayDescription = holidayDescription;
    }

    public String getStoreStatus() {
        return StoreStatus;
    }

    public void setStoreStatus(String storeStatus) {
        StoreStatus = storeStatus;
    }

    public String getHolidayDate() {
        return HolidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        HolidayDate = holidayDate;
    }

    public String getHolidayID() {
        return HolidayID;
    }

    public void setHolidayID(String holidayID) {
        HolidayID = holidayID;
    }

}
