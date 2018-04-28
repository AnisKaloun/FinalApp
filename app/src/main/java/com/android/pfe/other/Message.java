package com.android.pfe.other;

import java.io.Serializable;

public class Message implements Serializable{
    private String mArticleTitle;
    private String mUserUsername;
public Message(){

}
   public Message(String anticlerical,String userId)
   {
       this.mArticleTitle=anticlerical;
       this. mUserUsername=userId;
   }

    public String getArticleId() {
        return mArticleTitle;
    }

    public void setArticleId(String articleId) {
        mArticleTitle = articleId;
    }

    public String getUserUsername() {
        return  mUserUsername;
    }

    public void setUserUsername(String userId) {
        mUserUsername = userId;
    }
}
