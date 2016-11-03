package com.talles.android.gestaodecontatos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.talles.android.gestaodecontatos.R;
import com.talles.android.gestaodecontatos.dao.ContactDao;
import com.talles.android.gestaodecontatos.dao.DaoMaster;
import com.talles.android.gestaodecontatos.dao.DaoSession;
import com.talles.android.gestaodecontatos.model.Contact;

/**
 * Created by talles on 8/30/16.
 */

public class AddContactActivity  extends AppCompatActivity{

    private Spinner spinnerTelTypes;
    private Spinner spinnerEmailTypes;
    private ContactDao contactDao;
    private EditText editTxtName;
    private EditText editTxtPhone;
    private RadioGroup rdGroupGender;
    private EditText editTxtEmail;
    private RatingBar rtnBarAffinity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        this.spinnerTelTypes = (Spinner) findViewById(R.id.add_contact_tipo_telefone);
        this.spinnerEmailTypes = (Spinner) findViewById(R.id.add_contact_tipo_email);
        this.editTxtName = (EditText) findViewById(R.id.add_contact_nome);
        this.editTxtPhone = (EditText) findViewById(R.id.add_contact_telefone);
        this.editTxtEmail = (EditText) findViewById(R.id.add_contact_email);
        this.rtnBarAffinity = (RatingBar) findViewById(R.id.add_contact_rating);
        this.rdGroupGender = (RadioGroup) findViewById(R.id.add_contact_radioSexo);

        //((RadioButton) findViewById(R.id.add_contact_masculino)).setSelected(true);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        contactDao = MainActivity.contactDao;


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.telefone_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTelTypes.setAdapter(adapter);


        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.email_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerEmailTypes.setAdapter(adapter);

        Button btnSave = (Button) findViewById(R.id.add_contact_salvar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long flag = 0;
                try {
                    flag = insertContact();
                } catch (Exception e) {
                    new AlertDialog.Builder(AddContactActivity.this).setMessage(e.getMessage()).show();
                }
                if (flag >= 0)
                    new AlertDialog.Builder(AddContactActivity.this).setMessage("Contato salvo com sucesso!").show();
                else
                    new AlertDialog.Builder(AddContactActivity.this).setMessage("Falha ao inserir contato!").show();
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
                                Intent i = new Intent(AddContactActivity.this,AddContactActivity.class);
                                startActivity(i);
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

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_search) {
            Intent i  = new Intent(this,SearchContactActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private long insertContact() throws Exception {

        Contact contact = new Contact();
        String name = editTxtName.getText().toString();
        if(name == null || name.length() < 1){
            throw new Exception("O campo nome não pode ser vazio");
        }else{
            contact.setName(name);
        }

        String phone = editTxtPhone.getText().toString();
        if(phone == null || phone.trim().length() == 0){
            throw new Exception("O campo telefone não pode ser vazio");
        }else{
            contact.setPhone(phone);
        }

        contact.setPhone_type(spinnerTelTypes.getSelectedItemPosition());

        RadioButton genderRBtn = (RadioButton) findViewById(rdGroupGender.getCheckedRadioButtonId());
        String gender = genderRBtn.getText().toString();
        contact.setSexo(gender);

        String email = editTxtEmail.getText().toString();
        if(email == null || email.trim().length() == 0){
            throw new Exception("O campo email não pode ser vazio");
        }else{
            contact.setEmail(email);
        }

        contact.setEmail_type(spinnerEmailTypes.getSelectedItemPosition());
        contact.setAffinity(rtnBarAffinity.getRating());

        long result =  contactDao.insert(contact);
        Log.d("DaoExample", "Inserted new note, ID: " + contact.getId());
        return result;
    }
}
