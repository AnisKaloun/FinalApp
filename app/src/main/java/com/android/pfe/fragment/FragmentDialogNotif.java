package com.android.pfe.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.LecturePDF;
import com.android.pfe.other.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    private TextView mTitre,mNoteG;
    private TextView mMotcle;
    private TextView mAuteur;
    private Button mTelecharger;

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
        mTelecharger=getView().findViewById(R.id.telechargerPDF);
        query= FirebaseDatabase.getInstance().getReference("Article")
                .orderByChild("articleId")
                .equalTo(articleId);
        Log.w("FoundArticle","l'id de l'article a trouvé"+articleId);
        mTitre = getView().findViewById(R.id.titre);
        mAuteur = getView().findViewById(R.id.auteur);
        mMotcle = getView().findViewById(R.id.motcle);
        mNoteG=getView().findViewById(R.id.nbrEtoiles);
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
                        NumberFormat nf = new DecimalFormat("0.#");
                        String s = nf.format(article.getMoyenne());
                        mNoteG.setText(s);
                        mLire.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent(v.getContext(), LecturePDF.class);
                                myIntent.putExtra("titre", article.getTitre() + ".pdf");
                                myIntent.putExtra("auteur", article.getAuteur());
                                myIntent.putExtra("link",article.getPdfUrl());
                                v.getContext().startActivity(myIntent);

                            }
                        });
                        mTelecharger.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile(article);
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

    private void downloadFile(Article item) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.w("HomeAdaptor","le lien est"+item.getPdfUrl());
        StorageReference storageRef = storage.getReferenceFromUrl(item.getPdfUrl());


        File rootPath = new File(Environment.getExternalStorageDirectory(),"Article");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,item.getTitre()+".pdf");

        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(),"Fichier téléchargé", Toast.LENGTH_LONG).show();
                Log.e("firebase ","local file created" +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ","local file not created" +exception.toString());
            }
        });
    }

}

