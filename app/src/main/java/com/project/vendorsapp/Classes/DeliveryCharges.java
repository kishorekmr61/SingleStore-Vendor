package com.project.vendorsapp.Classes;

public class DeliveryCharges {
    String Title;
    String Charges;

    public DeliveryCharges(String title, String charges) {
        Title = title;
        Charges = charges;
    }



    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCharges() {
        return Charges;
    }

    public void setCharges(String charges) {
        Charges = charges;
    }


}
