package com.android.pfe.other;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pfe.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class ContactAdaptor extends ArrayAdapter<User> {
    List<User> myList;
    private LayoutInflater mInflater;
    private DatabaseReference Database;
    private Query mDatabase;
    private String userid;

    public ContactAdaptor(FragmentActivity activity, List<User> myList,String userid) {
        super(activity,0,myList);
        this.userid=userid;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final User item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_contact, parent, false);
        }
        TextView username = convertView.findViewById(R.id.text_view_contact_username);
        TextView email= convertView.findViewById(R.id.contact_email);
        final ImageView imgProfile= convertView.findViewById(R.id.image_view_contact_display);

        Database= FirebaseDatabase.getInstance().getReference("User").child(item.Uid).child("picUrl");

        Database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String URL= (String) dataSnapshot.getValue();
                    if(URL!=null) {
                        Log.w("URLPIC", ""+URL);

                        Glide.with(getContext()).load(URL)
                                .into(imgProfile);

                    }

                }
                else
                {
                    Log.w("URLPIC", "doesn't exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Button Supp= convertView.findViewById(R.id.delete_contact);
        Supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase= FirebaseDatabase.getInstance().getReference("User").child(userid).child("contact").orderByChild("Uid")
                        .equalTo(item.Uid);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                     if(dataSnapshot.exists())
                     {
                         for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                             snapshot.getRef().removeValue();
                         }
                     }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });




        username.setText(item.username);
        email.setText(item.email);


        return convertView;
    }


}
