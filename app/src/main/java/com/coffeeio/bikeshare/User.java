package com.coffeeio.bikeshare;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {
    @PrimaryKey
    @Required
    private String id;
    @Required
    private String username;
    @Required
    private String password;

    public User() {
        id =  UUID.randomUUID().toString();
    }
    public User(String _username, String _password) {
        id =  UUID.randomUUID().toString();
        username = _username;
        password = _password;
    }

    public String getId() {
        return id;
    }

}
