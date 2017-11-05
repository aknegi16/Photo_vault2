package com.example.akashn.photo_vault;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_login extends AppCompatActivity implements  ProgressGenerator.OnCompleteListener{

    FloatingActionButton fab, add_user;
    CardView  upload_photo,cv;
    EditText email, pass, conf_pass,name;
    public final String TAG =signup_login.class.getName() ;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

   //firebase


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        //updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);


      /*  final View context_name = findViewById(R.id.name);
        final View context_pass = findViewById(R.id.fpass);
        final View context_cpass = findViewById(R.id.cpass);
        final View context_upload_photo = findViewById(R.id.upload_photo);
*/




        name= (EditText) findViewById(R.id.user_id);
        email = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.fpass);
        conf_pass = (EditText) findViewById(R.id.cpass);









        //firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");


        ///firebase

        final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.register);



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressGenerator.start(btnSignIn);

                btnSignIn.setEnabled(false);
                name.setEnabled(false);
                email.setEnabled(false);
                pass.setEnabled(false);
                createAccount(email.getText().toString().trim(), pass.getText().toString().trim(),name.getText().toString().trim());

            }
        });








    }



    private void createAccount(String email, String password, final String name) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String user_id=user.getUid();
                            DatabaseReference currentUser_db=mDatabase.child(user_id);
                            currentUser_db.child("name").setValue(name);
                            currentUser_db.child("photo").setValue("photo.jpg");

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signup_login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
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




    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onComplete() {
      startActivity(new Intent(signup_login.this,login.class));
    }
}
