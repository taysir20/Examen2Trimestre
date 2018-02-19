package com.example.tay.examen2trimestre.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tay.examen2trimestre.R;
import com.example.tay.examen2trimestre.entity.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoUsers extends Fragment {

    private TextView txtUserName, txtUserLat, txtUserLong;





    public InfoUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_users, container, false);
        this.txtUserName=view.findViewById(R.id.txtUserNombre);
        this.txtUserLat=view.findViewById(R.id.txtUserLat);
        this.txtUserLong=view.findViewById(R.id.txtUserLong);

        // Inflate the layout for this fragment
        return  view;
    }

    public void setDataUser(User user){

        this.txtUserName.setText(user.nombre);
        this.txtUserLat.setText(String.valueOf(user.lat));
        this.txtUserLong.setText(String.valueOf(user.lon));


    }

    public TextView getTxtUserName() {
        return txtUserName;
    }

    public void setTxtUserName(TextView txtUserName) {
        this.txtUserName = txtUserName;
    }

    public TextView getTxtUserLat() {
        return txtUserLat;
    }

    public void setTxtUserLat(TextView txtUserLat) {
        this.txtUserLat = txtUserLat;
    }

    public TextView getTxtUserLong() {
        return txtUserLong;
    }

    public void setTxtUserLong(TextView txtUserLong) {
        this.txtUserLong = txtUserLong;
    }

}
