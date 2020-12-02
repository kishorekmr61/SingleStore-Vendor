package com.project.vendorsapp;

public class VendorList  {

    String CustomerID="";
    String CustomerName="";
    String Latitude="";
    String Longitude="";
    String Distance="";
    String Mobileno="";
    String PurchaseTypeID="";
    String OrderStatusID="";
    String OrderStatus="";
    String PaymentType="";
    String paymentstatus="";
    String ServiceName="";
    String OrderID="";
    String OrderDate="";
    String DeliveryCharages="";
    String DeliveryAssigned="";
    String deliveryboyemail;
    String deliverboyfirebaseid;
    String deliveryboymobileno;
    String OrderAmount;

    public VendorList(String customerID, String customerName, String latitude, String longitude, String distance, String mobileno, String purchaseTypeID, String orderStatusID, String orderStatus, String paymentType, String paymentstatus, String serviceName, String orderID, String orderDate, String deliveryCharages,String delievery_assign,String Dboyemail,String Dboyfirebaseid,String Dboymobileno,String orderamount) {
        CustomerID = customerID;
        CustomerName = customerName;
        Latitude = latitude;
        Longitude = longitude;
        Distance = distance;
        Mobileno = mobileno;
        PurchaseTypeID = purchaseTypeID;
        OrderStatusID = orderStatusID;
        OrderStatus = orderStatus;
        PaymentType = paymentType;
        this.paymentstatus = paymentstatus;
        ServiceName = serviceName;
        OrderID = orderID;
        OrderDate = orderDate;
        DeliveryCharages = deliveryCharages;
        DeliveryAssigned=delievery_assign;
        deliveryboyemail=Dboyemail;
        deliverboyfirebaseid=Dboyfirebaseid;
        deliveryboymobileno=Dboymobileno;
        OrderAmount=orderamount;
    }


    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getMobileno() {
        return Mobileno;
    }

    public void setMobileno(String mobileno) {
        Mobileno = mobileno;
    }

    public String getPurchaseTypeID() {
        return PurchaseTypeID;
    }

    public void setPurchaseTypeID(String purchaseTypeID) {
        PurchaseTypeID = purchaseTypeID;
    }

    public String getOrderStatusID() {
        return OrderStatusID;
    }

    public void setOrderStatusID(String orderStatusID) {
        OrderStatusID = orderStatusID;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }
    public String getDeliveryAssigned() {
        return DeliveryAssigned;
    }

    public void setDeliveryAssigned(String deliveryAssigned) {
        DeliveryAssigned = deliveryAssigned;
    }

    public String getDeliveryCharages() {
        return DeliveryCharages;
    }

    public void setDeliveryCharages(String deliveryCharages) {
        DeliveryCharages = deliveryCharages;
    }

    public String getDeliveryboyemail() {
        return deliveryboyemail;
    }

    public void setDeliveryboyemail(String deliveryboyemail) {
        this.deliveryboyemail = deliveryboyemail;
    }

    public String getDeliverboyfirebaseid() {
        return deliverboyfirebaseid;
    }

    public void setDeliverboyfirebaseid(String deliverboyfirebaseid) {
        this.deliverboyfirebaseid = deliverboyfirebaseid;
    }
    public String getDeliveryboymobileno() {
        return deliveryboymobileno;
    }

    public void setDeliveryboymobileno(String deliveryboymobileno) {
        this.deliveryboymobileno = deliveryboymobileno;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        OrderAmount = orderAmount;
    }




}
