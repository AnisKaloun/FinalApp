package com.android.pfe.other;

import android.support.annotation.Keep;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SADA INFO on 13/04/2018.
 */
@IgnoreExtraProperties
@Keep
public class User implements Serializable {
    private static final String TAG ="UserClass" ;
    public String username;
    public String email;
    public ArrayList<User> contact;
    public String Uid;
    public DatabaseReference mDatabase;
    public Notification mNotifications;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.android.pfe.other.User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;



    }

    public User(String username, String email,String uid) {
        this.username = username;
        this.email = email;
        this.contact= new ArrayList<>();
        this.Uid=uid;


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
        User user = new User(name, email,UserId);
        mDatabase.child(UserId).setValue(user);

    }
    public void addFriend(final String UserId, final String email)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference user = mDatabase.child(UserId);
        final DatabaseReference friendlist = user.child("contact");
        checkUser(email, new ICheckUserListener() {
        @Override
        public void onSuccess(final Map value) {
            friendlist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean bool=false;
                    if(dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, String> map = (Map) snapshot.getValue();
                            if(map.get("email").equals(email))
                            {
                            bool=true;
                            }


                        }
                        if(bool==false)
                        {
                            HashMap<String, String> Hmap = new HashMap<>();
                            Hmap.put("email", "" + email.toString().trim());
                            Hmap.put("username", "" + value.get("username").toString().trim());
                            Hmap.put("Uid",""+value.get("Uid").toString().trim());
                            friendlist.push().setValue(Hmap);
                        }

                    }
                    else
                    {
                        Map<String, String> map;
                        map = new HashMap<>();
                        map.put("email", "" + email.toString().trim());
                        map.put("username", "" + value.get("username").toString().trim());
                        map.put("Uid",""+value.get("Uid").toString().trim());
                        Log.w("UserClass", "i'm in map==null");
                        friendlist.push().setValue(map);

                    }

                    }




                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onError(Exception e) {

        }
    });

    }
    public void checkUser(String email, final ICheckUserListener listener) {
        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, String> map = (Map) snapshot.getValue();

                        if (map == null) {
                            Log.w(TAG, "user pas trouvé");
                        }
                        else {
                            listener.onSuccess(map);


                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                listener.onError(databaseError.toException());
            }
        };
        FirebaseDatabase
                .getInstance()
                .getReference("User").orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(mValueEventListener);
    }

    public void getIdFromMail(String email)
    {
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, String> map = (Map) snapshot.getValue();

                        if (map == null) {
                            Log.w(TAG, "user pas trouvé");
                        }
                        else {
                            //listener.onSuccess(map);


                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference("User").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(valueEventListener);

    }

    public void addNotification (Article article,User user){

        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference uti = mDatabase.child(user.Uid);
        DatabaseReference notif=uti.child("Notification");
        mNotifications=new Notification();
        mNotifications.setState(true);
        Message mp=new Message(article.getTitre(),user.Uid);
        mNotifications.addMessages(mp);
        Log.w("User","notification state"+mNotifications.isState()+"message :"+mNotifications.getMessages());



    }


}

