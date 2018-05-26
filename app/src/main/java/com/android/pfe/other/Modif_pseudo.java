package com.android.pfe.other; /**
 * Created by lety2018 on 04/04/2018.
 */

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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Modif_pseudo extends Activity {

    private static final String TAG ="Modif_pseudo" ;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText mAncienUser;
    private EditText mNouveauUser;
    private Button mModifier;
    private FirebaseUser userFire;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         userFire = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.modif_pseudo);
        mAncienUser=findViewById(R.id.editText);
        mNouveauUser=findViewById(R.id.editText2);
        mModifier=findViewById(R.id.button7);
        mAuth=FirebaseAuth.getInstance();
        setTitle("Modifier Pseudo");
        mDatabase= FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).child("username");
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
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mNouveauUser.getText().toString()).build();
                            userFire.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            mDatabase.setValue(mNouveauUser.getText().toString());

                            Intent insc = new Intent(Modif_pseudo.this,MenuActivity.class);
                            startActivity(insc);
                        }
                        else
                        {
                            Toast.makeText(Modif_pseudo.this, "Ancien Nom incorrect", Toast.LENGTH_SHORT).show();
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
