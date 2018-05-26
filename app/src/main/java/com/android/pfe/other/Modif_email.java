package com.android.pfe.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pfe.R;
import com.android.pfe.activity.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by lety2018 on 05/04/2018.
 */

public class Modif_email extends Activity {

    private EditText mAncienUser;
    private EditText mNouveauUser;
    private Button mModifier;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modif_email);
        setTitle("Modifier Email");
        mAncienUser=findViewById(R.id.ancienEmail);
        mNouveauUser=findViewById(R.id.newEmail);
        mModifier=findViewById(R.id.enregistrerEmail);
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).child("email");
        mModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String user= (String) dataSnapshot.getValue();
                            if(user.equals(mAncienUser.getText().toString()))
                            {
                                FirebaseUser uti = FirebaseAuth.getInstance().getCurrentUser();

                                uti.updateEmail(mNouveauUser.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User email address updated.");
                                                }
                                            }
                                        });

                                mDatabase.setValue(mNouveauUser.getText().toString());
                                Intent insc = new Intent(Modif_email.this,MenuActivity.class);
                                startActivity(insc);

                            }
                            else
                            {
                                Toast.makeText(Modif_email.this, "Ancien Nom incorrect", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}

