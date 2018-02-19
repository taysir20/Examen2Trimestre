package com.example.tay.examen2trimestre;

import com.example.tay.examen2trimestre.firebase.FirebaseAdminListener;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by tay on 19/2/18.
 */

public class SecondActivityEvents implements FirebaseAdminListener{
    private SecondActivity secondActivity;

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

    }
}
