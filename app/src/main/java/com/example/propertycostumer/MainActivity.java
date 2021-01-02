package com.example.propertycostumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.propertycostumer.adapter.PropertyAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    PropertyAdapter adapter;
    ProgressDialog progressDialog;

    Toolbar toolbar;

    FirebaseAuth firebaseAuth;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        textView = findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Italic.ttf");
        textView.setTypeface(typeface);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.setCanceledOnTouchOutside(true);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
        });

        getData();

    }

    private void getData() {
        progressDialog.show();
        FirebaseRecyclerOptions<Plots> options =
                new FirebaseRecyclerOptions.Builder<Plots>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Plots")
                        .orderByChild("is_sold").equalTo("No")
                        , Plots.class)
                        .build();
        adapter = new PropertyAdapter(options, this, progressDialog);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_logout) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.logout_dialog, null);
            builder.setView(dialogView);
            final  AlertDialog alertDialog = builder.create();

            final Button logout = dialogView.findViewById(R.id.YesBtn);
            final Button cancel = dialogView.findViewById(R.id.cancel_action);

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE).edit();

                    preferences.clear();
                    preferences.apply();
                    firebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();


                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.cancel();
                }
            });

            alertDialog.show();

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}