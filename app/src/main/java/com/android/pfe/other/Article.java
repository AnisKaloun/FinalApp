package com.android.pfe.other;

import android.support.annotation.Keep;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by lety2018 on 15/04/2018.
 */
@Keep
public class Article implements Serializable {

    private String titre;
    private String auteur;
    private String mot_cle;
    private String articleId;
    private float note ;

    public Article() {

        // Default constructor required for calls to DataSnapshot.getValue(com.android.pfe.other.User.class)

    }

    public Article(String titre, String auteur, String mot_cle) {
        this.auteur = auteur;
        this.titre = titre;
        this.mot_cle = mot_cle;
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

    public String getMot_cle() {
        return mot_cle;
    }

    public void addArticle(String auteur, String titre, String mot_cle) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Article dc = new Article(auteur,titre,mot_cle);
        database.child("Article").push().setValue(dc);
    }
}






