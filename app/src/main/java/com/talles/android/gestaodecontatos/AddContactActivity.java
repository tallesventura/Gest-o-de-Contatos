package com.talles.android.gestaodecontatos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by talles on 8/30/16.
 */

public class AddContactActivity  extends AppCompatActivity{

    private Spinner spinnerTelTypes;
    private Spinner spinnerEmailTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_add_contact);

        this.spinnerTelTypes = (Spinner) findViewById(R.id.add_contact_tipo_telefone);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.telefone_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTelTypes.setAdapter(adapter);

        this.spinnerEmailTypes = (Spinner) findViewById(R.id.add_contact_tipo_email);
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.email_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerEmailTypes.setAdapter(adapter);

    }
}
