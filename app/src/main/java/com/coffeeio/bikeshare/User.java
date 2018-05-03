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

    private double balance;

    public User() {
        id =  UUID.randomUUID().toString();
        balance = 0.0;
    }
    public User(String _username, String _password) {
        id =  UUID.randomUUID().toString();
        balance = 0.0;
        username = _username;
        password = _password;
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
