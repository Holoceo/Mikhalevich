package com.redmadintern.mikhalevich.model.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 04.07.2015.
 */
public class EventReport {
    private long id;
    private String number;
    private List<EventStatus> statuses;

    public EventReport(long id, String number) {
        this.id = id;
        this.number = number;
        statuses = new ArrayList<>(10);
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public List<EventStatus> getStatuses() {
        return statuses;
    }

    public void addStatus(EventStatus eventStatus) {
        statuses.add(eventStatus);
    }

    public void addStatuses(List<EventStatus> statuses) {
        this.statuses.addAll(statuses);
    }
}
