package com.android.pfe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pfe.R;
import com.android.pfe.other.Message;
import com.android.pfe.other.NotificationAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    private TextView text;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ArrayList<Message> notificationList;
    private NotificationAdaptor adaptor;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          notificationList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message mp = snapshot.getValue(Message.class);
                    notificationList.add(mp);
                }

                adaptor.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public NotificationsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list =getView().findViewById(R.id.ListNotif);
        text=getView().findViewById(R.id.ListMessage);
        auth= FirebaseAuth.getInstance();
        notificationList= new ArrayList<Message>();
        mDatabase= FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid().toString())
                .child("Notification").child("Message");
        mDatabase.addValueEventListener(valueEventListener);
        adaptor = new NotificationAdaptor(getActivity(),notificationList);

        list.setAdapter(adaptor);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message mp=(Message) parent.getAdapter().getItem(position);
                FragmentDialogNotif fd=new FragmentDialogNotif(mp.getArticleId());
                fd.setTargetFragment(NotificationsFragment.this,1);
                fd.show(getFragmentManager(),"Dialog");
                //dans mp on doit d'abord mettre l'id de l'article pour charger l'article

            }
        });

    }



}
