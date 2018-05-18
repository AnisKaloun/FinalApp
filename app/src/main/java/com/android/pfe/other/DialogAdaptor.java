package com.android.pfe.other;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pfe.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class DialogAdaptor extends ArrayAdapter<User> {
    List<User> myList;
    private LayoutInflater mInflater;
    private FirebaseAuth auth,admin;
    private DatabaseReference mDatabase;
    private DatabaseReference Database;
    //  private onClicked listener;

    public DialogAdaptor(FragmentActivity activity, List<User> myList) {
        super(activity,0,myList);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final User item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_partage, parent, false);
        }
       auth=FirebaseAuth.getInstance();


        TextView username = convertView.findViewById(R.id.text_view_contact_username);
        TextView email= convertView.findViewById(R.id.contact_email);
        username.setText(item.username);
        email.setText(item.email);
       /*convertView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (listener!=null)
               {
                   Log.w("Adaptor","i am clicking on item");
                   listener.checkedListener(item);
               }


           }
       });*/

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

        return convertView;
    }
/*
    public void setListener(onClicked listener) {
        this.listener = listener;
    }
    public interface onClicked {
        void checkedListener(User user);
    }
*/




}
