package com.example.topot2;

public class Product {

    String id,name,price,imgUrl,type,desc;
    int prodOder = -1;

    public Product(String id, String name, String price, String imgUrl, String type, String desc) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.type = type;
        this.desc = desc;
    }

    public Product() {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getProdOder() {
        return prodOder;
    }

    public void setProdOder(int prodOder) {
        this.prodOder = prodOder;
    }
}
