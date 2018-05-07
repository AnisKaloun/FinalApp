package com.android.pfe.other;

import android.support.annotation.Keep;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lety2018 on 15/04/2018.
 */
@Keep
public class Article implements Serializable {

    private static final String TAG ="Article";
    public String mMot_cle;
    private String titre;
    private String auteur;
    private String articleId;
    private float note ;
    private String Userid;
    private List motcle;

    public Article() {

        // Default constructor required for calls to DataSnapshot.getValue(com.android.pfe.other.User.class)

    }

    public Article(String user, String articleId, String titre, String auteur,String mot_cle) {

        this.auteur = auteur;
        this.titre = titre;
        this.Userid=user;
        this.articleId=articleId;
        this.mMot_cle=mot_cle;
    }

    public Article(ArrayList<String> arrayList, String titre) {
        this.motcle=new ArrayList(arrayList);
        this.titre=titre;
    }
    public Article(String user, String articleId, String titre, String auteur,String mot_cle,ArrayList<String> arrayList) {

        this.auteur = auteur;
        this.titre = titre;
        this.Userid=user;
        this.articleId=articleId;
        this.mMot_cle=mot_cle;
        this.motcle=new ArrayList(arrayList);

    }


    public String getArticleId() {
        return articleId;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getTitre() {
        return titre;
    }
    public float getNote() {
        return note;
    }

    public void addArticle(String user,String auteur, String titre,ArrayList motcle)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String id= database.child("Article").push().getKey();
        if(motcle.isEmpty()==false) {

            String mot="";

            for (Object s:motcle) {
                mot+=s+"";
            }


            Article dc = new Article(user,id,titre,auteur,mot,motcle);
            database.child("Article").child(id).setValue(dc);
         //  database.child("Article").child(id).child("motcle").setValue(motcle);
        }
        else
               {

                Article dc = new Article(user,id,titre,auteur,"");
                database.child("Article").child(id).setValue(dc);

               }




    }


    public String getid() {
        return Userid;
    }

    public String getMot_cle() {
        return mMot_cle;
    }
}






