package com.example.tay.examen2trimestre.entity;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {
    public double lat;
    public double lon;
    public String nombre;

    public User(String string, double aDouble, double cursorDouble) {

        this.lat = lat;
        this.lon = lon;
        this.nombre = nombre;
    }

    public User() {

    }
}
