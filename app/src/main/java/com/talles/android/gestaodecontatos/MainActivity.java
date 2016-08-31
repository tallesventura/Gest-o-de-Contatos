package com.talles.android.gestaodecontatos;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAddContact;


    private void goToAddContactActivity(){
        Intent i = new Intent(this, AddContactActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fabAddContact = (FloatingActionButton) findViewById(R.id.fab_add_contact);
        this.fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddContactActivity();
            }
        });
    }
}
