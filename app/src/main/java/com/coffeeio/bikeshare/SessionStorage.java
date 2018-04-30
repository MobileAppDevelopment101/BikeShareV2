package com.coffeeio.bikeshare;

import android.content.Context;

public class SessionStorage {
    private static SessionStorage sSessionStorage;
    private String userid = "";


    private SessionStorage(Context context) {
    }
    private SessionStorage() {
    }
    public static SessionStorage get() {
        if (sSessionStorage == null) {
            sSessionStorage = new SessionStorage();
        }
        return sSessionStorage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
