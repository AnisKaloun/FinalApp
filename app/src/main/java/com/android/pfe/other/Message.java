package com.android.pfe.other;

import java.io.Serializable;

public class Message implements Serializable{
    private String mArticleTitle;
    private String mUserId;
public Message(){

}
   public Message(String anticlerical,String userId)
   {
       this.mArticleTitle=anticlerical;
       this.mUserId=userId;
   }

    public String getArticleId() {
        return mArticleTitle;
    }

    public void setArticleId(String articleId) {
        mArticleTitle = articleId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
