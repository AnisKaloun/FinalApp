package com.android.pfe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.HomeAdaptor;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


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
        ListView list =getView().findViewById(R.id.ListAcceil);
        ArrayList<Article> mydata = new ArrayList<Article>();
        HomeAdaptor adaptor = new HomeAdaptor(getActivity(),mydata);
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
