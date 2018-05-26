package com.android.pfe.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.HomeAdaptor;
import com.android.pfe.other.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private HomeAdaptor adaptor;
    private ArrayList<Article> mydata;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private ListView list;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        auth= FirebaseAuth.getInstance();

         list =getView().findViewById(R.id.ListAcceil);

        new MyAsyncTask().execute();


    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            auth= FirebaseAuth.getInstance();
            User uti=new User();
            uti.recommand(auth.getCurrentUser().getUid());
            //writing in database
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            auth= FirebaseAuth.getInstance();
            mydata = new ArrayList<Article>();
            adaptor = new HomeAdaptor(getActivity(),mydata);
            mDatabase = FirebaseDatabase.getInstance().getReference("User");
            DatabaseReference user = mDatabase.child(auth.getCurrentUser().getUid());
            DatabaseReference keylist = user.child("ArticleRecommande");
            //reading from the database
            keylist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.w("HomeFragment","In the listener");
                    mydata.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //je lis d'abord les articles recommandé
                            Log.w("HomeFragment","not empty");
                            Article article = snapshot.getValue(Article.class);
                            //article c l'article recommandé qui se trouve dans l'utilisateur
                            Query listener=FirebaseDatabase.getInstance().getReference("Article").orderByChild("articleId")
                                    .equalTo(article.getArticleId());

                            listener.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    //içi on rentre dans l'article
                                    if (dataSnapshot.exists())
                                    {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Article doc=snapshot.getValue(Article.class);
                                            int size=mydata.size();
                                            boolean isEqual=false;
                                            for(int i=0;i<size;i++) {
                                               Article art=mydata.get(i);
                                               if(art.getArticleId().equals(doc.getArticleId()))
                                               {
                                               isEqual=true;
                                               }
                                                }

                                             if(!isEqual) {

                                                 mydata.add(doc);
                                             }
                                        }
                                        adaptor.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                    else
                    {
                        //when i first charge the fragment i am here
                        Log.w("HomeFragment","empty");

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            adaptor.setListener(new HomeAdaptor.onChecked() {
                @Override
                public void checkedListener(Article article) {
                    FragmentDialogShare fd=new FragmentDialogShare(article);
                    fd.setTargetFragment(HomeFragment.this,1);
                    fd.show(getFragmentManager(),"MyDialog");
                }
            });
            list.setAdapter(adaptor);
            
        }
    }
}





