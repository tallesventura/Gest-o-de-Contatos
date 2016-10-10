package com.talles.android.gestaodecontatos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.talles.android.gestaodecontatos.R;

/**
 * Created by talles on 8/30/16.
 */

public class AddContactActivity  extends AppCompatActivity{

    private Spinner spinnerTelTypes;
    private Spinner spinnerEmailTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


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

        Button btnSave = (Button) findViewById(R.id.add_contact_salvar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddContactActivity.this).setMessage("Contato salvo com sucesso!").show();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.add_contact_cancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder db = new AlertDialog.Builder(AddContactActivity.this);
                db.setTitle("Atenção");
                db.setMessage("Deseja realmente cancelar a inclusão?");

                db.setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                db.setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(AddContactActivity.this,MainActivity.class);
                                startActivity(i);
                            }
                        });

                AlertDialog alert = db.create();
                alert.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addcontact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_search) {
            Intent i  = new Intent(this,SearchContactActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
