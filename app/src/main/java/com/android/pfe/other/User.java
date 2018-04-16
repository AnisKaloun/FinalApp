package com.android.pfe.other;

import android.support.annotation.Keep;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SADA INFO on 13/04/2018.
 */
@IgnoreExtraProperties
@Keep
public class User implements Serializable {
    public String username;
    public String email;
    public List contact;
    public List article;
    public DatabaseReference mDatabase;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.android.pfe.other.User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addUser(String UserId, String name, String email) {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        User user = new User(name, email);
        mDatabase.child(UserId).setValue(user);
    }


}