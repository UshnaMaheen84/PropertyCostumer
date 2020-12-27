package com.example.propertycostumer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText entername, enteremail, enterpassword, enternumber;
    Button signup;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering");
        progressDialog.setCanceledOnTouchOutside(false);

        entername = findViewById(R.id.name);
        enteremail = findViewById(R.id.email);
        enternumber = findViewById(R.id.number);
        enterpassword = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Costumers");

        String regexPassword = "(?=.*[a-z]).{6,}";
        awesomeValidation.addValidation(SignUp.this, R.id.name, "[a-zA-Z\\s]+", R.string.name);
        awesomeValidation.addValidation(SignUp.this, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.email);
        awesomeValidation.addValidation(SignUp.this, R.id.password, regexPassword, R.string.password);

        signup = findViewById(R.id.signupbtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (awesomeValidation.validate()) {

                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(enteremail.getText().toString(), enterpassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Log.e("successful", user.getUid());

                                        User users = new User(entername.getText().toString(),
                                                enteremail.getText().toString(),
                                                enternumber.getText().toString(),
                                                enterpassword.getText().toString());

                                        databaseReference.child(user.getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(SignUp.this, VerifyPhoneActivity.class);
                                                    intent.putExtra("number", enternumber.getText().toString());
                                                    startActivity(intent);
                                                    progressDialog.dismiss();
                                                    finish();
                                                } else {
                                                    progressDialog.dismiss();
                                                    Log.e("Exception", task.getException().getMessage());
                                                }
                                            }
                                        });
                                    } else {
                                        Log.e("unsuccessful", task.getException().getMessage());
                                        progressDialog.dismiss();
                                    }

                                }
                            });


                }
            }
        });

    }
}