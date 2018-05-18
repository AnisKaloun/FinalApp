package com.android.pfe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.pfe.R;
import com.android.pfe.activity.AjouterArticleActivity;
import com.android.pfe.other.Article;
import com.android.pfe.other.ArticleAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class ArticleFragment extends Fragment {


    private ArrayList<Article> mydata;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private ArticleAdaptor adaptor;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mydata.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    mydata.add(article);
                }

                adaptor.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private Button button;


    public ArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list =getView().findViewById(R.id.MesArticles);
         button=getView().findViewById(R.id.AjouterDocument);
        auth = FirebaseAuth.getInstance();


        mydata = new ArrayList<Article>();
        Query mDatabase = FirebaseDatabase.getInstance().getReference("Article")
                .orderByChild("id")
                .equalTo(auth.getCurrentUser().getUid().toString());
        mDatabase.addValueEventListener(valueEventListener);

         adaptor = new ArticleAdaptor(getActivity(),mydata);
        adaptor.setListener(new ArticleAdaptor.onChecked() {
            @Override
            public void checkedListener(Article article) {
                FragmentDialogShare fd=new FragmentDialogShare(article);
                fd.setTargetFragment(ArticleFragment.this,1);
                fd.show(getFragmentManager(),"MyDialog");
            }
        });
        list.setAdapter(adaptor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AjouterArticleActivity.class);
                startActivity(intent);
            }
        });

    }

}
