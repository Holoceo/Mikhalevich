package com.redmadintern.mikhalevich.utils.builders;

import com.redmadintern.mikhalevich.model.local.EventStatus;
import com.redmadintern.mikhalevich.model.local.Stoa;

public class EventStatusBuilder {
    private long id;
    private String sortNumber;
    private int date;
    private boolean passed;
    private Stoa stoa;
    private String imageUrl;
    private String title;
    private String shortDescription;

    public EventStatusBuilder() {
        sortNumber = new String();
        imageUrl = new String();
        title = new String();
        shortDescription = new String();
        stoa = new StoaBuilder().createStoa();
    }

    public EventStatusBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public EventStatusBuilder setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
        return this;
    }

    public EventStatusBuilder setDate(int date) {
        this.date = date;
        return this;
    }

    public EventStatusBuilder setPassed(boolean passed) {
        this.passed = passed;
        return this;
    }

    public EventStatusBuilder setStoa(Stoa stoa) {
        this.stoa = stoa;
        return this;
    }

    public EventStatusBuilder setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public EventStatusBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EventStatusBuilder setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public EventStatus createEventStatus() {
        return new EventStatus(id, sortNumber, date, passed, stoa, imageUrl, title, shortDescription);
    }
}