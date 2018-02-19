package com.example.tay.examen2trimestre;

import com.example.tay.examen2trimestre.entity.User;
import com.example.tay.examen2trimestre.firebase.FirebaseAdminListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

/**
 * Created by tay on 19/2/18.
 */

public class SecondActivityEvents implements FirebaseAdminListener, OnMapReadyCallback {
    private SecondActivity secondActivity;
    //Variable google Map que usaremos después para añadir los pines
    private GoogleMap googleMap;

    public SecondActivityEvents(SecondActivity secondActivity) {
        this.secondActivity = secondActivity;
    }

    @Override
    public void loginIsOk(boolean ok) {

    }

    @Override
    public void registerOk(boolean ok) {

    }

    @Override
    public void signOutOk(boolean ok) {

    }

    @Override
    public void downloadBranch(String branch, DataSnapshot dataSnapshot) {
        if (branch.equals("Usuarios")) {
            //Tenemos que usar un GenericTypeIndicator dado que firebase devuelve los datos utlizando esta clase abstracta
            GenericTypeIndicator<ArrayList<User>> indicator = new GenericTypeIndicator<ArrayList<User>>() {
            };
            ArrayList<User> arrUsers = dataSnapshot.getValue(indicator);//desde el value podemos castearlo al tipo que queramos, en este caso


        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }
}
