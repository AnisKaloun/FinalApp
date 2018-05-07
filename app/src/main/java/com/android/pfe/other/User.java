package com.android.pfe.other;

import android.support.annotation.Keep;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
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
    public ArrayList ArticleDesire;
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
    public User(String username, String email,String uid,List list) {
        this.username = username;
        this.email = email;
        this.contact= new ArrayList<>();
        this.Uid=uid;
        this.ArticleDesire=new ArrayList(list);
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
    public void addMotcle(final ArrayList<String> motcle, String Userid)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference user = mDatabase.child(Userid);
        final DatabaseReference keylist = user.child("ArticleDesire");
        keylist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                keylist.setValue(motcle);
              /*  if(dataSnapshot.exists())
                {
                    ArrayList list = (ArrayList) dataSnapshot.getValue();
                    Map<String,Long>Hmap= new HashMap<>();
                    Hmap.putAll(map);

                    //if there is duplicate we incremente les mots clé
                    for (Object key : motcle.keySet()) {

                        if (Hmap.containsKey(key)) {
                            Log.w("clé classe"," "+Hmap.get(key).getClass());
                            Hmap.put((String)key,(Hmap.get(key))+1);

                        }
                        else
                        {
                            Hmap.put((String) key, (long) 1);
                        }
                    }
                   keylist.setValue(Hmap);

                }
                else
                {
                 keylist.setValue(motcle);

                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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

    public void addNotification (final Article article, final User user){

        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference uti = mDatabase.child(user.Uid);
        final DatabaseReference notif=uti.child("Notification");
        final DatabaseReference message=notif.child("Message");
        mNotifications=new Notification();
        mNotifications.setState(true);
        notif.setValue(mNotifications);
        Message mp=new Message(article.getTitre(),user.getUsername());
        message.push().setValue(mp);
        Log.w("User","notification state"+mNotifications.isState()+"message :");



    }

    public void recommand(String UserId)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference user = mDatabase.child(UserId);
        final DatabaseReference keylist = user.child("motcle");
        keylist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Map<String,Long> map = (Map) dataSnapshot.getValue();
                    if(numberofitem(map)<10) {
                        //on peut prendre les i premiers article par mot clé

                        findArticleBySimilarity(map);
                    }
                    else{
                        //calcule collaboratif en utilisant pearson
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private Long numberofitem(Map<String, Long> map) {

         Long sum = null;
        for(Long L:map.values())
        {
         sum+=L;
        }
        return sum;
    }

    private void findArticleBySimilarity(Map<String, Long> map) {



        //on met une grande boucle ou en itere sur map

            Query query = FirebaseDatabase.getInstance().getReference("Article");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> arti = (Map) snapshot.getValue();
                            if (arti.containsKey("motcle")) {
                                Map<String, Long> motcle = (Map<String, Long>) arti.get("motcle");
                              //  FastByIDMap<PreferenceArray> preferences=new FastByIDMap<PreferenceArray>;
                                //içi on calcule la similarité avec pearson
                                //aprés avoir calculer on regarde la note si elle est bonne on l'as met dans une liste


                            }
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




    }



}

