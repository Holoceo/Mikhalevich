package com.redmadintern.mikhalevich.model.local;

/**
 * Created by Alexander on 04.07.2015.
 */
public class Stoa {
    private long id;
    private String title;
    private String adress;
    private double longitude;
    private double latitude;
    private String serviceHours;
    private String dealer;
    private String phone;

    public Stoa(long id, String title, String adress, double longitude, double latitude, String serviceHours, String dealer, String phone) {
        this.id = id;
        this.title = title;
        this.adress = adress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.serviceHours = serviceHours;
        this.dealer = dealer;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAdress() {
        return adress;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getServiceHours() {
        return serviceHours;
    }

    public String getDealer() {
        return dealer;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isEmpty() {
        return id == 0 || title.isEmpty();
    }
}
