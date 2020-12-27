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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText userEmail, userPassword;
    Button login;

    TextView signup;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loging in");
        progressDialog.setCanceledOnTouchOutside(false);

        userEmail = findViewById(R.id.emailAddress);
        userPassword = findViewById(R.id.pass);

        signup= findViewById(R.id.Signingup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        login = findViewById(R.id.loginbtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                final String email = userEmail.getText().toString();
                final String pswrd = userPassword.getText().toString();

                if (!email.isEmpty() && !pswrd.isEmpty()) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Costumers");

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.e("exception", "try check 4");

                            String logging = "";

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                try {
                                    Log.e("exception", "try check");

                                    User model = data.getValue(User.class);

                                    if (model != null && model.getEmail().equals(email) && model.getPassword().equals(pswrd)) {

                                        Log.e("exception", "try check 2");
                                        logging = "done";

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(Login.this, VerifyPhoneActivity.class);
                                        intent.putExtra("number", model.getNumber());
                                        startActivity(intent);
                                        finish();

                                        return;

                                    }


                                } catch (Exception e) {
                                    Log.e("exception", e.getLocalizedMessage());
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                                }
                            }

                            if (!logging.equals("done")) {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Enter Valid Email and Password", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Exception", databaseError.getMessage());
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    };


                    databaseReference.addValueEventListener(valueEventListener);

                } else {
                    progressDialog.dismiss();

                    Toast.makeText(Login.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}