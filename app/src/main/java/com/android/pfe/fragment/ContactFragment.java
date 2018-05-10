package com.android.pfe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pfe.R;
import com.android.pfe.other.ContactAdaptor;
import com.android.pfe.other.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment  {

    private ListView listAmi;
    private DatabaseReference mDatabase;
    private Button bouton;
    private TextView email;
    private FirebaseAuth auth;
    private List<User> contactList;
    private ContactAdaptor adaptor;
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



    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
      ListView list =getView().findViewById(R.id.contactframe);
        auth = FirebaseAuth.getInstance();
        final User user=new User();
        bouton = getView().findViewById(R.id.search_btn);
        email=getView().findViewById(R.id.search_field);

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().trim().equals(auth.getCurrentUser().getEmail().toString().trim()))
                {
                    user.addFriend(auth.getCurrentUser().getUid(), email.getText().toString().trim());
                email.setText("");
            }
            else{
                    Toast.makeText(getActivity(),"Action refusé",Toast.LENGTH_LONG).show();
                }
            }
        });

                contactList= new ArrayList<User>();
        mDatabase= FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid().toString())
                .child("contact");

        mDatabase.addValueEventListener(valueEventListener);

        adaptor = new ContactAdaptor(getActivity(),contactList);

        list.setAdapter(adaptor);


    }


}
