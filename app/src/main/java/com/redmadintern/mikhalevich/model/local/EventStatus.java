package com.redmadintern.mikhalevich.model.local;

/**
 * Created by Alexander on 04.07.2015.
 */
public class EventStatus {
    private long id;
    private String sortNumber;
    private int date;
    private boolean passed;
    private Stoa stoa;
    private String imageUrl;
    private String title;
    private String shortDescription;

    public EventStatus(long id, String sortNumber, int date, boolean passed, Stoa stoa, String imageUrl, String title, String shortDescription) {
        this.id = id;
        this.sortNumber = sortNumber;
        this.date = date;
        this.passed = passed;
        this.stoa = stoa;
        this.imageUrl = imageUrl;
        this.title = title;
        this.shortDescription = shortDescription;
    }

    public long getId() {
        return id;
    }

    public String getSortNumber() {
        return sortNumber;
    }

    public int getDate() {
        return date;
    }

    public boolean isPassed() {
        return passed;
    }

    public Stoa getStoa() {
        return stoa;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
