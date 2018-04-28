package com.android.pfe.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.DialogAdaptor;
import com.android.pfe.other.Message;
import com.android.pfe.other.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by SADA INFO on 24/04/2018.
 */

public class FragmentDialog extends DialogFragment{
    ListView mListView;
    private FirebaseAuth auth;
    private ArrayList<User> contactList;
    private DatabaseReference mDatabase,mData;
    private DialogAdaptor adaptor;
    private Article mArticleSent;
    private static final String TAG = "MyDialog";

    public FragmentDialog(){

    }

    @SuppressLint("ValidFragment")
    public FragmentDialog(Article article) {
        this.mArticleSent=article;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.dialog_share, container, false);
        getDialog().setTitle("Mes Contacts");

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        contactList= new ArrayList<User>();
        mListView=(ListView) getView().findViewById(R.id.Shareframe);
        mDatabase= FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid().toString())
                .child("contact");

        mDatabase.addValueEventListener(valueEventListener);
        adaptor = new DialogAdaptor(getActivity(),contactList);
        mListView.setAdapter(adaptor);
      mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              User uti=(User)parent.getAdapter().getItem(position);
              Log.w("Dialog","uti"+uti.username);
              //on dois check les notification si il existe ou pas si sa existe alors on rajoute un message et l'etat change sinon
              //on crée une nouvelle notif
             uti.addNotification(mArticleSent,uti);
          }
      });




    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            contactList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User ami = snapshot.getValue(User.class);
                    contactList.add(ami);
                }

                adaptor.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


}
