package com.android.pfe.other;

import android.support.annotation.Keep;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by lety2018 on 15/04/2018.
 */
@IgnoreExtraProperties
@Keep
public class Article implements Serializable {

    private static final String TAG ="Article";
    private String mot_cle;
    private String titre;
    private String auteur;
    private String articleId;
   // private float note ;
    private String id;

    public Article() {

        // Default constructor required for calls to DataSnapshot.getValue(com.android.pfe.other.User.class)

    }

    public Article(String user, String articleId, String titre, String auteur, String mot_cle) {

        this.auteur = auteur;
        this.titre = titre;
        this.id=user;
        this.articleId=articleId;
        this.mot_cle=mot_cle;


    }

    public Article(String map, String titre) {
        //constructeur pour les article desireé
        this.mot_cle=map;
        this.titre=titre;
    }

    public Article(String user, String articleId, String titre, String auteur) {
        this.auteur = auteur;
        this.titre = titre;
        this.id=user;
        this.articleId=articleId;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getTitre() {
        return titre;
    }
 /*   public float getNote() {
        return note;
    }*/

    public void addArticle(String user, String auteur, String titre, String motcle)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String id= database.child("Article").push().getKey();

        if(motcle!=null) {


            Article dc = new Article(user,id,titre,auteur,motcle);
            database.child("Article").child(id).setValue(dc);

        }
        else
               {

                Article dc = new Article(user,id,titre,auteur);
                database.child("Article").child(id).setValue(dc);

               }




    }


    public String getid() {
        return id;
    }

    public String getMot_cle() {
        return mot_cle;
    }


}






