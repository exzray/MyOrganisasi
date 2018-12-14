package com.developer.athirah.myorganisasi.models;

import com.google.firebase.firestore.Exclude;

public class ModelProfile {

    private String uid;
    private String name;
    private Integer age;

    public ModelProfile() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
