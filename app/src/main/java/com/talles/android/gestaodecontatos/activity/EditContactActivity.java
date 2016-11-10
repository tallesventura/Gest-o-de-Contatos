package com.talles.android.gestaodecontatos.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.talles.android.gestaodecontatos.R;
import com.talles.android.gestaodecontatos.dao.ContactDao;
import com.talles.android.gestaodecontatos.model.Contact;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by talles on 11/7/16.
 */

public class EditContactActivity  extends AppCompatActivity {

    private Spinner spinnerTelTypes;
    private Spinner spinnerEmailTypes;
    private ContactDao contactDao;
    private EditText editTxtName;
    private EditText editTxtPhone;
    private RadioGroup rdGroupGender;
    private EditText editTxtEmail;
    private RatingBar rtnBarAffinity;
    private ImageView imViewPhoto;
    private Long id = null;
    private String photoPath = "";
    private Bitmap picture = null;
    private boolean photoFromCamera = false;
    private boolean photoFromGalery = false;

    private static final int CAMERA_REQUEST = 1;
    private static final int GALERY_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        contactDao = MainActivity.contactDao;

        this.spinnerTelTypes = (Spinner) findViewById(R.id.add_contact_tipo_telefone);
        this.spinnerEmailTypes = (Spinner) findViewById(R.id.add_contact_tipo_email);
        this.editTxtName = (EditText) findViewById(R.id.add_contact_nome);
        this.editTxtPhone = (EditText) findViewById(R.id.add_contact_telefone);
        this.editTxtEmail = (EditText) findViewById(R.id.add_contact_email);
        this.rtnBarAffinity = (RatingBar) findViewById(R.id.add_contact_rating);
        this.rdGroupGender = (RadioGroup) findViewById(R.id.add_contact_radioSexo);
        this.imViewPhoto = (ImageView) findViewById(R.id.contact_photo);

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


        Bundle contact_bundle = getIntent().getExtras();
        if(contact_bundle != null){

            id = getIntent().getLongExtra("id",-1);
            Contact c = contactDao.load(id);
            System.out.println("Tela Edit: " + id);

            String name = c.getName();
            String phone = c.getPhone();
            int phoneType = c.getPhone_type();
            String gender = c.getSexo();
            String email = c.getEmail();
            int emailType = c.getEmail_type();
            float affinity = c.getAffinity();
            photoPath = c.getPath_photo();
            picture = getContactPhoto(photoPath);

            editTxtName.setText(name);
            editTxtPhone.setText(phone);
            spinnerTelTypes.setSelection(phoneType,true);
            RadioButton rBtMas = (RadioButton) findViewById(R.id.add_contact_masculino);
            RadioButton rBtFem = (RadioButton) findViewById(R.id.add_contact_feminino);
            if(gender.equalsIgnoreCase("Masculino"))
                rBtMas.setChecked(true);
            else if(gender.equalsIgnoreCase("Feminino"))
                rBtFem.setChecked(true);

            editTxtEmail.setText(email);
            spinnerEmailTypes.setSelection(emailType,true);
            rtnBarAffinity.setRating(affinity);
            if(picture != null){
                imViewPhoto.setImageBitmap(picture);
            }

        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        Button btnSave = (Button) findViewById(R.id.add_contact_salvar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
                new AlertDialog.Builder(EditContactActivity.this).setMessage("Contato salvo com sucesso!").show();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.add_contact_cancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder db = new AlertDialog.Builder(EditContactActivity.this);
                db.setTitle("Atenção");
                db.setMessage("Deseja realmente cancelar a inclusão?");

                db.setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent i = new Intent(EditContactActivity.this,AddContactActivity.class);
                                startActivity(i);
                            }
                        });

                db.setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(EditContactActivity.this,MainActivity.class);
                                startActivity(i);
                            }
                        });

                AlertDialog alert = db.create();
                alert.show();
            }
        });

        Button btnPhoto = (Button) findViewById(R.id.btn_send_photo);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder db = new AlertDialog.Builder(EditContactActivity.this);
                db.setTitle("Envio de foto");
                db.setMessage("Escolha como deseja enviar a foto");

                db.setNegativeButton(
                        R.string.camera,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                photoFromCamera = true;
                                photoFromGalery = false;
                                openCamera();
                            }
                        });

                db.setPositiveButton(
                        R.string.galery,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                photoFromCamera = false;
                                photoFromGalery = true;
                                chooseImgFromGalery(v);
                            }
                        });

                AlertDialog alert = db.create();
                alert.show();
            }
        });

    }

    private void openCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,CAMERA_REQUEST);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void getImageFromGalery(Uri imgUri){

        String colunas[] = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imgUri,colunas,null,null,null);
        cursor.moveToFirst();
        int collumnIndex = cursor.getColumnIndex(colunas[0]);
        photoPath = cursor.getString(collumnIndex);
        cursor.close();

        picture = BitmapFactory.decodeFile(photoPath);
        ImageView iv = (ImageView) findViewById(R.id.contact_photo);

        /*
        try {
            picture = getBitmapFromUri(imgUri);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        iv.setImageBitmap(picture);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            if(resultCode == AppCompatActivity.RESULT_OK) {

                if (requestCode == CAMERA_REQUEST) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        picture = (Bitmap) bundle.get("data");
                        ImageView iv = (ImageView) findViewById(R.id.contact_photo);
                        iv.setImageBitmap(picture);
                    }
                }

                if(requestCode == GALERY_REQUEST){
                    Uri imgUri = data.getData();
                    getImageFromGalery(imgUri);
                }
            }
        }
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

    private Bitmap getContactPhoto(String path){
        if (path == null || path.length() == 0)
            return (null);

        Bitmap picture = BitmapFactory.decodeFile(path);

        return picture;
    }

    public void updateImage(){


        FileOutputStream fos = null;
        File filePath = new File(photoPath);
        try {
            fos = new FileOutputStream(filePath);
            picture.compress(Bitmap.CompressFormat.PNG, 100 /*quality*/, fos);
            fos.close();
        }
        catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            photoPath = "";
        }
    }

    public void updatePhoto(long id){

        photoPath = "";
        File internalStorage = getApplicationContext().getDir("ContactPictures", Context.MODE_PRIVATE);
        File filePath = new File(internalStorage, id + ".png");
        photoPath = filePath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            picture.compress(Bitmap.CompressFormat.PNG, 100 /*quality*/, fos);
            fos.close();
        }
        catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            photoPath = "";
        }

    }

    public void chooseImgFromGalery(View view){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //i.setType("image/*");
        startActivityForResult(i,GALERY_REQUEST);
    }

    private void updateContact() {

        Contact contact = new Contact();
        contact.setId(id);
        contact.setName(editTxtName.getText().toString());
        contact.setPhone(editTxtPhone.getText().toString());
        contact.setPhone_type(spinnerTelTypes.getSelectedItemPosition());
        int genderId = rdGroupGender.getCheckedRadioButtonId();
        if(genderId == R.id.add_contact_masculino){
            contact.setSexo("Masculino");
        }else if(genderId == R.id.add_contact_feminino){
            contact.setSexo("Feminino");
        }
        contact.setEmail(editTxtEmail.getText().toString());
        contact.setEmail_type(spinnerEmailTypes.getSelectedItemPosition());
        contact.setAffinity(rtnBarAffinity.getRating());

        picture = ((BitmapDrawable)imViewPhoto.getDrawable()).getBitmap();

        if(photoFromCamera)
            updatePhoto(contact.getId());

        contact.setPath_photo(photoPath);
        contactDao.update(contact);
    }
}
