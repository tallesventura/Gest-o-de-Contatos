package com.talles.android.gestaodecontatos.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.talles.android.gestaodecontatos.dao.DaoSession;
import com.talles.android.gestaodecontatos.dao.ContactDao;

/**
 * Created by talles on 11/2/16.
 */

@Entity(generateConstructors = true, generateGettersSetters = true)
public class Contact {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String phone;

    @NotNull
    private int phone_type;

    @Property
    private String sexo;

    @NotNull
    private String email;

    @NotNull
    private int email_type;

    @Property
    private Float affinity;

    @Property
    private String path_photo;

    public Contact() {
    }

    @Generated(hash = 1219787585)
    public Contact(Long id, @NotNull String name, @NotNull String phone,
            int phone_type, String sexo, @NotNull String email, int email_type,
            Float affinity, String path_photo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phone_type = phone_type;
        this.sexo = sexo;
        this.email = email;
        this.email_type = email_type;
        this.affinity = affinity;
        this.path_photo = path_photo;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSexo() {
        return sexo;
    }

    public String getEmail() {
        return email;
    }

    public Float getAffinity() {
        return affinity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAffinity(Float affinity) {
        this.affinity = affinity;
    }

    public int getPhone_type() {
        return this.phone_type;
    }

    public void setPhone_type(int phone_type) {
        this.phone_type = phone_type;
    }

    public int getEmail_type() {
        return this.email_type;
    }

    public void setEmail_type(int email_type) {
        this.email_type = email_type;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public String getPath_photo() {
        return this.path_photo;
    }

    public void setPath_photo(String path_photo) {
        this.path_photo = path_photo;
    }
}
