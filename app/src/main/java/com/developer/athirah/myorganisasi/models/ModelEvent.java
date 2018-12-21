package com.developer.athirah.myorganisasi.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelEvent {

    public enum Status {Ongoing, Cancel, Complete}

    private String uid = "";
    private String title = "";
    private String description = "";
    private String image = "";
    private String location = "";

    private Date date = new Date();

    private GeoPoint point;

    private Status status = Status.Ongoing;

    private List<String> people = new ArrayList<>();
    private Map<String, List<String>> task = new HashMap<>();

    public ModelEvent() {
        // require empty constructor
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public Status getStatus() {

        Calendar now = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.setTime(date);

        if (future.before(now)) status = Status.Complete;

        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public Map<String, List<String>> getTask() {
        return task;
    }

    public void setTask(Map<String, List<String>> task) {
        this.task = task;
    }
}
