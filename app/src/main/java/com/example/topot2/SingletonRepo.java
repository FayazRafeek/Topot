package com.example.topot2;

public class SingletonRepo {

    public static SingletonRepo INSTANCE = new SingletonRepo();

    public static SingletonRepo getInstance(){

        if(INSTANCE == null) return new SingletonRepo();
        return INSTANCE;
    }

//    GLobal
    public Product productInfo;

    public Product getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(Product productInfo) {
        this.productInfo = productInfo;
    }
}
