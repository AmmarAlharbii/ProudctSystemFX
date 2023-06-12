package com.example.proudctsystemfx;


public class Product { //this class will represent the recoerds in table
    private int id;
    private String type;
    private String model;
    private float price;
    private int count;
    private String deliveryDate;

    public Product(int id, String type, String model, float price, int count, String deliveryDate) {
        this.id = id;
        this.type = type;
        this.model = model;
        this.price = price;
        this.count = count;
        this.deliveryDate = deliveryDate;
    }

    public int getId() {
        return id;
    }//return id

    public String getType() {
        return type;
    }//return type

    public String getModel() {
        return model;
    }//return model

    public float getPrice() {
        return price;
    }//return price

    public int getCount() {
        return count;
    }//return count

    public String getDeliveryDate() {
        return deliveryDate;
    }//return deliveryDate
}
