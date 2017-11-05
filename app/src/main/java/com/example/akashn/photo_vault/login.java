package com.example.akashn.photo_vault;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pnikosis.materialishprogress.ProgressWheel;

public class login extends AppCompatActivity implements  View.OnClickListener {
    private static final String TAG ="login" ;
    CardView cv;
    private EditText email;
    private EditText pass;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    //firebase

    private FloatingActionButton fab ;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private LinearLayout layout_MainMenu;
    private ProgressWheel progresswheel;
    private Button register;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        mAuth.addAuthStateListener(mAuthListener);
        //updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     email= (EditText) findViewById(R.id.login_email);
     pass= (EditText) findViewById(R.id.login_pass);
    fab= (FloatingActionButton) findViewById(R.id.login);

      ///  layout_MainMenu = (LinearLayout) findViewById( R.id.linearl);
      //  layout_MainMenu.getForeground().setAlpha( 0);


        progresswheel=(ProgressWheel)findViewById(R.id.progress_wheel);
        register=(Button)findViewById(R.id.gotoregister);
        register.setOnClickListener(this);

        fab.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in


                    checkUserExist();
             ///

                } else {progresswheel.stopSpinning();
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };



    }








    private void signIn(String email, String password) {
     //   layout_MainMenu.getForeground().setAlpha( 220);

        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
          //  layout_MainMenu.getForeground().setAlpha( 0);
            progresswheel.stopSpinning();
            return;
        }

        //showProgressDialog();
        progresswheel.spin();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            checkUserExist();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //layout_MainMenu.getForeground().setAlpha( 0);
                            progresswheel.stopSpinning();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            loginfailed();
                        }
                       // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void checkUserExist() {

    final String user_id=mAuth.getCurrentUser().getUid();



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    startActivity(new Intent(login.this,Gallery.class));
                }
                else
                {
                   // Toast.makeText(login.this, "You need to set up your account ", Toast.LENGTH_SHORT).show();
                   progresswheel.stopSpinning();
                    FirebaseAuth.getInstance().signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void loginfailed() {

email.setError("Username or password mismatch");

    }


    private boolean validateForm() {
        boolean valid = true;

        String emailstr = email.getText().toString();
        if (TextUtils.isEmpty(emailstr)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String passwordstr = pass.getText().toString();
        if (TextUtils.isEmpty(passwordstr)) {
            pass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
        }

        return valid;
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
int id=view.getId();

        if(id==R.id.login)
        {
           // layout_MainMenu.getForeground().setAlpha( 220); // dim
            signIn(email.getText().toString(),pass.getText().toString());
        }
        if(id==R.id.gotoregister)
        {
            startActivity(new Intent(login.this,signup_login.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
