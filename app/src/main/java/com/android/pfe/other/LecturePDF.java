package com.android.pfe.other;

import android.app.Activity;
import android.os.Bundle;

import com.android.pfe.R;
import com.github.barteksc.pdfviewer.PDFView;

/**
 * Created by lety2018 on 21/04/2018.
 */

public class LecturePDF extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_pdf);



        Bundle b = getIntent().getExtras();
        String rec_titre = b.getString("titre");
        String rec_auteur =b.getString("auteur");


        PDFView pdfView = (PDFView)findViewById(R.id.pdfView);
        pdfView.fromAsset(rec_titre).load();

    }
}
