package com.android.pfe.other;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by lety2018 on 15/04/2018.
 */

public class Article implements Serializable{

    private static final String TAG ="Article";
    private String mot_cle;
    private String titre;
    private String auteur;
    private String articleId;
    private String id;
    private String PdfUrl;
    private Integer nbrvote;
    private Float note;
    private Float moyenne;
    private Boolean voted;

    public Article() {
        this.note=null;
        this.nbrvote=null;
        this.moyenne=null;

    }

    public Article(String user, String articleId, String titre, String auteur, String mot_cle, String s) {

        this.auteur = auteur;
        this.titre = titre;
        this.id=user;
        this.articleId=articleId;
        this.mot_cle=mot_cle;
        this.PdfUrl=s;
        this.note= Float.valueOf(0);
        this.nbrvote=0;
        this.moyenne= Float.valueOf(0);

    }

    public Article(String map, String titre) {
        //constructeur pour les article desireé
        this.mot_cle=map;
        this.titre=titre;
        this.note=null;
        this.nbrvote=null;
        this.moyenne=null;
    }

    public Article(String user, String articleId, String titre, String auteur) {
        this.auteur = auteur;
        this.titre = titre;
        this.id=user;
        this.articleId=articleId;
        this.note=null;
        this.nbrvote=null;
        this.moyenne=null;
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

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void addArticle(String user, String auteur, String titre, String motcle, String s)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String id= database.child("Article").push().getKey();

        if(motcle!=null) {


            Article dc = new Article(user,id,titre,auteur,motcle,s);
            database.child("Article").child(id).setValue(dc);

        }
        else
               {

                Article dc = new Article(user,id,titre,auteur,"",s);
                database.child("Article").child(id).setValue(dc);

               }




    }

    public String getid() {
        return id;
    }

    public String getMot_cle() {
        return mot_cle;
    }

    public void setMot_cle(String mot_cle) {
        this.mot_cle = mot_cle;
    }

    public String getPdfUrl() {
        return PdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        PdfUrl = pdfUrl;
    }


    public Float getNote() {
        if(note!=null)
        return note;
        return Float.valueOf(0);
    }

    public void setNote(float note) {
        this.note = note;
    }

    public int getNbrvote() {
        if(nbrvote!=null)return nbrvote;
        return Integer.valueOf(0);
    }

    public void setNbrvote(int nbrvote) {
        this.nbrvote = nbrvote;
    }


    public Float getMoyenne() {
        if(moyenne!=null)
    return moyenne;
    return Float.valueOf(0);
    }

    public void setMoyenne(){
        if(this.getNote()!=0 && this.getNbrvote()!=0)
        {
            this.moyenne=(this.getNote()/this.getNbrvote());
        }
        else
        {
            this.moyenne=Float.valueOf(0);
        }
    }


    public Boolean isVoted() {
        return voted;
    }


    public void setVoted(Boolean voted) {
        this.voted = voted;
    }


}






