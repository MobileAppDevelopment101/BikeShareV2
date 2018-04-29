package com.coffeeio.bikeshare;

import android.support.annotation.RequiresPermission;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Bike extends RealmObject {

    @PrimaryKey
    @Required
    private String id;
    @Required
    private String name;
    private double price;

    public Bike() {
        id = UUID.randomUUID().toString();
        name = "Something";
    }
    public String getId() {
        return id;
    }
}
