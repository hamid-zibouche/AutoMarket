package com.example.automarket.domain;

import com.example.automarket.Model.Annonce;

import java.io.Serializable;

public class PopularDomain implements Serializable {
    private String title;

    private String picUrl;


    private int nbrVue;

    private double price;
    private Annonce annonce;

    public PopularDomain(Annonce annonce , String title, String picUrl, int nbrVue, double price) {
        this.annonce = annonce;
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
        this.nbrVue =nbrVue;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PopularDomain() {
    }


    public Annonce getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNbrVue() {
        return nbrVue;
    }

    public void setNbrVue(int nbrVue) {
        this.nbrVue = nbrVue;
    }
}
