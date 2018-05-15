package com.android.pfe.other;

import java.io.Serializable;

public class Message implements Serializable{
    private String mArticleTitle;
    private String mUserUsername;
    private String mArticleId;


    public Message(){


    }


   public Message(String Articletitle, String Articleid, String userId)
   {
       this.mArticleId=Articleid;
       this.mArticleTitle=Articletitle;
       this. mUserUsername=userId;
   }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public void setArticleTitle(String articleId) {
        mArticleTitle = articleId;
    }

    public String getUserUsername() {
        return  mUserUsername;
    }

    public void setUserUsername(String userId) {
        mUserUsername = userId;
    }
    public String getArticleId() {
        return mArticleId;
    }

    public void setArticleId(String articleId) {
        mArticleId = articleId;
    }
}
