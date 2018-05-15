package com.android.pfe.other;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by SADA INFO on 13/04/2018.
 */
@IgnoreExtraProperties

public class User implements Serializable {
    private static final String TAG ="UserClass" ;
    public String username;
    public String email;
    public ArrayList<User> contact;
    public String Uid;
    public ArrayList<Article> ArticleDesire;
    public DatabaseReference mDatabase;
    public Notification mNotifications;
    private DatabaseReference Database;

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
    public User(String username, String email,String uid,ArrayList<Article> list) {
        this.username = username;
        this.email = email;
        this.contact= new ArrayList<>();
        this.Uid=uid;
        this.ArticleDesire= new ArrayList<>(list);
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
    public void addMotcle(final ArrayList<Article> motcle, String Userid)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference user = mDatabase.child(Userid);
        final DatabaseReference keylist = user.child("ArticleDesire");
        keylist.setValue(motcle);
       /*  keylist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


               if(dataSnapshot.exists())
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

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



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
                            if(!bool)
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
        Message mp=new Message(article.getTitre(),article.getArticleId(),user.getUsername());
        message.push().setValue(mp);
        Log.w("User","notification state"+mNotifications.isState()+"message :");



    }

    public void recommand(final String UserId)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        final DatabaseReference user = mDatabase.child(UserId);
        final DatabaseReference keylist = user.child("ArticleDesire");
        keylist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                         Article art = snapshot.getValue(Article.class);
                         if(art.getMot_cle()!=null) {
                             findArticleBySimilarity(art,UserId);
                         }


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

    private void findArticleBySimilarity(final Article map, final String userId) {

            Query query = FirebaseDatabase.getInstance().getReference("Article");
          mDatabase = FirebaseDatabase.getInstance().getReference("User").child(userId);
        final DatabaseReference articleRec = mDatabase.child("ArticleRecommande");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Article arti = snapshot.getValue(Article.class);
                            if (arti != null) {
                               // Log.w("Article ","Article de user "+arti.getid());
                                if (!arti.getid().equals(userId)) {
                                    if (arti.getMot_cle() != null) {
                                        Log.w("Article ","mot clé 1 "+arti.getMot_cle());
                                        Log.w("Article ","mot clé 2 "+map.getMot_cle());
                                        double sim = calculerSimi(map.getMot_cle(), arti.getMot_cle());
                                        Log.w("Sim", " " + sim);
                                        if (sim > 0.6) {
                                            //içi on as trouvé que c similaire
                                            Article docR=new Article();
                                            docR.setArticleId(arti.getArticleId());
                                            articleRec.push().setValue(docR);

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




    }

    private double calculerSimi(String mot_cle, String mot_cle1) {
        //içi on calcule avec jacard
        String [] mot1 = mot_cle.split("\\s+");
        String [] mot2 = mot_cle1.split("\\s+");


        Set<String> tag1=new HashSet<>(Arrays.asList(mot1));
        Set<String> tag2=new HashSet<>(Arrays.asList(mot2));
        //union
        Set<String> union = new HashSet<String>(tag1);
        union.addAll(tag2);
        Log.w("Union","taille est"+union.size());
        Log.w("Union","les element sont"+union);
        //intersection
        Set<String> intersect=new HashSet<String>(tag1);
        intersect.retainAll(tag2);
        Log.w("intersect","taille est"+intersect.size());

        return (Double.valueOf(intersect.size()) / Double.valueOf(union.size()));

    }


}

