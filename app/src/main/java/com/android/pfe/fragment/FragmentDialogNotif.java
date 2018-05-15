package com.android.pfe.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.LecturePDF;
import com.android.pfe.other.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by SADA INFO on 24/04/2018.
 */

public class FragmentDialogNotif extends DialogFragment{

    private static final String TAG = "Dialog";
    private FirebaseAuth auth;
    private ArrayList<User> contactList;
    private DatabaseReference mDatabase,mData;
    private Button mLire;
    private Button mPartager;
    private Query query;
    private String articleId;

    private TextView mTitre;
    private TextView mMotcle;
    private TextView mAuteur;

    public FragmentDialogNotif(){

    }

    @SuppressLint("ValidFragment")
    public FragmentDialogNotif(String articleId) {
        this.articleId=articleId;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.dialog_notification, container, false);
        getDialog().setTitle("Article");

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLire = getView().findViewById(R.id.lirePDF);
        mPartager= getView().findViewById(R.id.partagerPDF);
        query= FirebaseDatabase.getInstance().getReference("Article")
                .orderByChild("articleId")
                .equalTo(articleId);
        Log.w("FoundArticle","l'id de l'article a trouvé"+articleId);
        mTitre = getView().findViewById(R.id.titre);
        mAuteur = getView().findViewById(R.id.auteur);
        mMotcle = getView().findViewById(R.id.motcle);
        Log.w("notification","je suis dans le Dialog frag");
        ValueEventListener mListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Log.w("Notification","Article Trouvé");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        final Article article = snapshot.getValue(Article.class);

                        Log.w("FoundArticle", "le titre est" + article.getTitre());
                        Log.w("FoundArticle", "l'auteur est" + article.getAuteur());

                        mTitre.setText(article.getTitre());
                        mAuteur.setText(article.getAuteur());
                        mMotcle.setText(article.getMot_cle());

                        mLire.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent(v.getContext(), LecturePDF.class);
                                myIntent.putExtra("titre", article.getTitre() + ".pdf");
                                myIntent.putExtra("auteur", article.getAuteur());

                                v.getContext().startActivity(myIntent);

                            }
                        });


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


query.addValueEventListener(mListener);

    }



}

