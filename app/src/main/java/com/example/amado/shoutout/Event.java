package com.example.amado.shoutout;

import java.io.Serializable;

/**
 * Created by amado on 6/7/2017.
 */

public class Event implements Serializable {

    private String title;
    private double lat;
    private double lng;
    private String addressString;
    private String description;
    private String fromDate;
    private String fromTime;
    private String toDate;
    private String toTime;

    public Event(String title, double lat, double lng, String addressString, String description, String fromDate, String fromTime, String toDate, String toTime) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.addressString = addressString;
        this.description = description;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
    }


    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "Title='" + title + '\'' +
                ", Description='" + description + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
