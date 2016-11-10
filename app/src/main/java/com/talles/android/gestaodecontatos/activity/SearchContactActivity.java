package com.talles.android.gestaodecontatos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.talles.android.gestaodecontatos.R;
import com.talles.android.gestaodecontatos.dao.ContactDao;
import com.talles.android.gestaodecontatos.model.Contact;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.List;

public class SearchContactActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private ContactDao contactDao;
    private ListView listView;
    ArrayAdapter<Contact> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.search_contact_listView);
        editTextSearch = (EditText) findViewById(R.id.search_contact_text);
        contactDao = MainActivity.contactDao;

        adapter = new ArrayAdapter(this,R.layout.search_item,R.id.contact_name);
        listView.setAdapter(adapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                if(name.length() > 0)
                    search(name);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Contact c = adapter.getItem(position);
                AlertDialog.Builder db = new AlertDialog.Builder(SearchContactActivity.this);
                db.setTitle("Atenção");
                db.setMessage("Deseja realmente excluir o contato?");

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
                                deleteContact(c);
                            }
                        });

                AlertDialog alert = db.create();
                alert.show();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact c = adapter.getItem(position);
                Intent i = new Intent(SearchContactActivity.this, EditContactActivity.class);
                i.putExtra("id",c.getId());
                System.out.println("Tela Search: " + c.getId());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void deleteContact(Contact c){
        String picturePath = c.getPath_photo();
        if (picturePath != null && picturePath.length() != 0) {
            File filePath = new File(picturePath);
            filePath.delete();
        }
        contactDao.delete(c);
        editTextSearch.setText("");
        adapter.clear();
    }

    private void search(String name){
        adapter.clear();
        QueryBuilder qb = contactDao.queryBuilder();
        List<Contact> l = qb.where(ContactDao.Properties.Name.like("%" + name + "%")).
                orderAsc(ContactDao.Properties.Name).list();
        for(Contact c: l){
            adapter.add(c);
        }
    }
}
