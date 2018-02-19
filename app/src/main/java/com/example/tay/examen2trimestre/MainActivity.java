package com.example.tay.examen2trimestre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //Definimos un CallBackManager que se encargará de realizar y devolver llamadas a facebook
    CallbackManager callbackManager;
    //Definimos el botón de logueo de facebook para asociarlo a su componente visual
    LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Crea un administrador de devoluciones de llamada que gestione las respuestas de inicio de sesión.
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //Añadimos los permisos que queramos solicitar al usuario en el momento en el que se loguea
        loginButton.setReadPermissions(Arrays.asList
                ("public_profile", "user_friends", "email", "user_photos", "  user_birthday", "read_custom_friendlists"));

        /*
           A través del registercallbaxk registraremos las llamadas a facebook y si si son exitosas o dan error se llamará a uno de estos
           métodos
         */

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }

    //Método onActivity al que llegan las llamadas de facebook y twitter.
    /*
    Se ejecuta cuando recibe respuestas desde fuera de la aplicación, es propio de todos los activities.
    Por lo tanto cuando se realiza una llamada al pinchar el btn de facebook se devuelve una respuesta por aquí y de esta manera
    dependiendo de la respuesta entrará a los métodos del registerCallBack del loginbutton
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Si es facebook
        if (requestCode == 64206) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}

}
