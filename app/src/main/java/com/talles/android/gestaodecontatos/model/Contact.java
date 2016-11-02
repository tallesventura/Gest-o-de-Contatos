package com.talles.android.gestaodecontatos.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

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

    @NotNull
    private String sexo;

    @Property
    private String email;

    @Property
    private int email_type;

    @Property
    private int affinity;

    public Contact() {
    }

    @Generated(hash = 930342977)
    public Contact(Long id, @NotNull String name, @NotNull String phone,
            int phone_type, @NotNull String sexo, String email, int email_type,
            int affinity) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phone_type = phone_type;
        this.sexo = sexo;
        this.email = email;
        this.email_type = email_type;
        this.affinity = affinity;
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

    public int getAffinity() {
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

    public void setAffinity(int affinity) {
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
}
