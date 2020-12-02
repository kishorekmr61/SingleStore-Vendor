package com.project.vendorsapp;

public class ItemList {
    String quantity="";
    String itemname="";
    String unitprice="";
    String weight="";
    float price;


    public ItemList(String quantity, String itemname, String unitprice, String weight, float price) {
        this.quantity = quantity;
        this.itemname = itemname;
        this.unitprice = unitprice;
        this.weight = weight;
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


}
