package com.android.pfe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.HomeAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private HomeAdaptor adaptor;
    private ArrayList<Article> mydata;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list =getView().findViewById(R.id.ListAcceil);
         mydata = new ArrayList<Article>();
         auth= FirebaseAuth.getInstance();

        adaptor = new HomeAdaptor(getActivity(),mydata);
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
         DatabaseReference user = mDatabase.child(auth.getCurrentUser().getUid());
         DatabaseReference keylist = user.child("ArticleRecommande");
         keylist.addListenerForSingleValueEvent(valueEventListener);
        adaptor.setListener(new HomeAdaptor.onChecked() {
            @Override
            public void checkedListener(Article article) {
                FragmentDialogShare fd=new FragmentDialogShare(article);
                fd.setTargetFragment(HomeFragment.this,1);
                fd.show(getFragmentManager(),"MyDialog");
            }
        });
        list.setAdapter(adaptor);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mydata.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    //article c l'article recommandé qui se trouve dans l'utilisateur
                    Query listener=FirebaseDatabase.getInstance().getReference("Article").orderByChild("articleId")
                            .equalTo(article.getArticleId());

                    listener.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //içi on rentre dans l'article
                            if (dataSnapshot.exists())
                            {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Article doc=snapshot.getValue(Article.class);
                                    mydata.add(doc);
                                }
                                adaptor.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



}
