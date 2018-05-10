package com.android.pfe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.HomeAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RechercheFragment extends Fragment {


    private RadioGroup mRdGrp;
    private View mSearch;
    private ArrayList<Article> Recherche;
    private HomeAdaptor adaptor;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Recherche.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    Recherche.add(article);
                }

                adaptor.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private TextView mTextSearch;

    public RechercheFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recherche, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list =getView().findViewById(R.id.rechercheframe);
        mRdGrp= getView().findViewById(R.id.radioGroup);
        mSearch=getView().findViewById(R.id.search_btn);
        mTextSearch= getView().findViewById(R.id.search_field);
        Recherche = new ArrayList<Article>();
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recherche.clear();
                int rd_id=mRdGrp.getCheckedRadioButtonId();
                Query query;
                if(rd_id==R.id.radioButton_auteur)
                {
                     query= FirebaseDatabase.getInstance().getReference("Article")
                            .orderByChild("auteur")
                            .equalTo(mTextSearch.getText().toString().trim());
                    //recherche par auteur
                    query.addValueEventListener(valueEventListener);
                }
                else
                {
                     query= FirebaseDatabase.getInstance().getReference("Article")
                            .orderByChild("titre")
                            .equalTo(mTextSearch.getText().toString().trim());
                    //recherche par titre
                    query.addValueEventListener(valueEventListener);

                }
            }
        });


        adaptor = new HomeAdaptor(getActivity(),Recherche);

        list.setAdapter(adaptor);

    }




}
