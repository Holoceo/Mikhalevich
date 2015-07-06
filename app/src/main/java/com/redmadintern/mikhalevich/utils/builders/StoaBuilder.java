package com.redmadintern.mikhalevich.utils.builders;

import com.redmadintern.mikhalevich.model.local.Stoa;

public class StoaBuilder {
    private long id;
    private String title;
    private String adress;
    private double longitude;
    private double latitude;
    private String serviceHours;
    private String dealer;
    private String phone;

    public StoaBuilder() {
        title = new String();
        adress = new String();
        serviceHours = new String();
        dealer = new String();
        phone = new String();
    }

    public StoaBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public StoaBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public StoaBuilder setAdress(String adress) {
        this.adress = adress;
        return this;
    }

    public StoaBuilder setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public StoaBuilder setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public StoaBuilder setServiceHours(String serviceHours) {
        this.serviceHours = serviceHours;
        return this;
    }

    public StoaBuilder setDealer(String dealer) {
        this.dealer = dealer;
        return this;
    }

    public StoaBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Stoa createStoa() {
        return new Stoa(id, title, adress, longitude, latitude, serviceHours, dealer, phone);
    }
}