package com.example.tay.examen2trimestre;

import android.support.v4.app.FragmentTransaction;

import com.example.tay.examen2trimestre.entity.User;
import com.example.tay.examen2trimestre.firebase.FirebaseAdminListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

/**
 * Created by tay on 19/2/18.
 */

public class SecondActivityEvents implements FirebaseAdminListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
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
            ArrayList<User> arrUsers = dataSnapshot.getValue(indicator);//desde el value podemos castearlo al tipo que queramos, en este caso lo casteamos al genericTypeIndicator
            System.out.println("------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>Los usuarios de la bbdd son: " + arrUsers);
            //Antes de introducir los pines borramos los anteriores apra evitar que se repitan en la bbdd dado que no tenemos un id para cada fila
            DataHolder.MyDataHolder.databaseHandler.deleteAllUsers();
            //Añadimos los users a sqlite
            for (int i = 0; i < arrUsers.size(); i++) {
                System.out.println("------------>>>>>>>>>>>>entra aqui dentro--------->>>>>>>>>>>>>>");
                DataHolder.MyDataHolder.databaseHandler.addUsers(arrUsers.get(i));
            }
            this.addMarker();
        }


    }


    public void addMarker() {
        //Recorremos la tabla de usuarios de sqlite y por cada posición cremos una variable latitud longitud y añadimos un maker al mapa
        for (int i = 0; i < DataHolder.MyDataHolder.databaseHandler.getAllUsers().size(); i++) {
            LatLng latLng = new LatLng(DataHolder.MyDataHolder.databaseHandler.getAllUsers().get(i).lat, DataHolder.MyDataHolder.databaseHandler.getAllUsers().get(i).lon);
            this.googleMap.addMarker(new MarkerOptions().position(latLng).title(DataHolder.MyDataHolder.databaseHandler.getAllUsers().get(i).nombre)).setTag(DataHolder.MyDataHolder.databaseHandler.getAllUsers().get(i));
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);//decimos al mapa cuál es su escuchador para cuando pulsemos los pines

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        this.secondActivity.getInfoFragment().setDataUser((User)marker.getTag());
        FragmentTransaction transition = this.secondActivity.getSupportFragmentManager().beginTransaction();
        transition.show(this.secondActivity.getInfoFragment());
        transition.commit();



        return false;
    }
}
