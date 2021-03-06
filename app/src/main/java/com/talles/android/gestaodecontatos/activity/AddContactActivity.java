package com.talles.android.gestaodecontatos.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.talles.android.gestaodecontatos.R;
import com.talles.android.gestaodecontatos.dao.ContactDao;
import com.talles.android.gestaodecontatos.dao.DaoMaster;
import com.talles.android.gestaodecontatos.dao.DaoSession;
import com.talles.android.gestaodecontatos.model.Contact;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private Bitmap picture = null;
    private String galery_path = "";
    private long id = -1;
    private boolean photoFromCamera = false;
    private boolean photoFromGalery = false;
    private Uri imgUri = null;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALERY_REQUEST = 2;

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

        ((RadioButton) findViewById(R.id.add_contact_masculino)).setSelected(true);

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
                if (flag >= 0) {
                    id = flag;
                    if(photoFromCamera)
                        updatePhoto(flag);
                    else if(photoFromGalery)
                        updatePhoto(flag,galery_path);

                    new AlertDialog.Builder(AddContactActivity.this).setMessage("Contato salvo com sucesso!").show();
                }else
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

        Button btnPhoto = (Button) findViewById(R.id.btn_send_photo);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder db = new AlertDialog.Builder(AddContactActivity.this);
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
        galery_path = cursor.getString(collumnIndex);
        cursor.close();

        //picture = BitmapFactory.decodeFile(galery_path);
        ImageView iv = (ImageView) findViewById(R.id.contact_photo);

        try {
            picture = getBitmapFromUri(imgUri);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        iv.setImageBitmap(picture);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getImageFromGalery(imgUri);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
                    imgUri = data.getData();
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    }else{
                        getImageFromGalery(imgUri);
                    }

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

    public void updatePhoto(long id){
        Contact c = contactDao.load(id);
        if(c != null && picture != null){

            String picturePath = "";
            File internalStorage = getApplicationContext().getDir("ContactPictures", Context.MODE_PRIVATE);
            File filePath = new File(internalStorage, id + ".png");
            picturePath = filePath.toString();

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath);
                picture.compress(Bitmap.CompressFormat.PNG, 100 /*quality*/, fos);
                fos.close();
            }
            catch (Exception ex) {
                Log.i("DATABASE", "Problem updating picture", ex);
                picturePath = "";
            }

            c.setPath_photo(picturePath);
            contactDao.update(c);
        }
    }

    public void updatePhoto(long id, String path){
        Contact c = contactDao.load(id);
        if (galery_path.length() == 0){
            c.setPath_photo("");
        }

        if(c != null){
            c.setPath_photo(path);
            contactDao.update(c);
        }
    }

    public void chooseImgFromGalery(View view){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //i.setType("image/*");
        startActivityForResult(i,GALERY_REQUEST);
    }
}
