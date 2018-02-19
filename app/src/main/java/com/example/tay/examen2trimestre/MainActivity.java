package com.example.tay.examen2trimestre;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.tay.examen2trimestre.SQLite.DatabaseHandler;
import com.example.tay.examen2trimestre.firebase.FirebaseAdmin;
import com.example.tay.examen2trimestre.firebase.FirebaseAdminListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    //Definimos un CallBackManager que se encargará de realizar y devolver llamadas a facebook
    CallbackManager callbackManager;
    //Definimos el botón de logueo de facebook para asociarlo a su componente visual
    LoginButton loginButton;
    //Creamos una variable de tipo FirebaseAdmin
    private FirebaseAdmin firebaseAdmin;
    //Creamos una variable de google sign in:
    private GoogleSignInClient mGoogleSignInClient;
    //Definimos el botón de logueo de google para asociarlo a su componente visual
    private SignInButton signInButton;
    //Creamos una variable de tipo Mainctivity Events
    private MainActivityEvents mainActivityEvents;
    // Creamos variable listener firebase
    private FirebaseAdminListener firebaseAdminListener;
    //Variable para la animación
    private Animation facebookAnimarion;
    //Creamos una variable del sqlite
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicializamos el firebaseAdmin
        firebaseAdmin = new FirebaseAdmin();
        //Instanciamos el mainacitivty events que recibe por parámetro el mainActivity
        mainActivityEvents = new MainActivityEvents(this);
        //Instanciamos el listener de firebaseAdminListener que será el mainActivityEvents
        this.setFirebaseAdminListener(getMainActivityEvents());

        // instanciamos sqlite
        databaseHandler = new DatabaseHandler(this);
        DataHolder.MyDataHolder.databaseHandler = databaseHandler;

        try {
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
                    //Método de firebase que nos inicia sesión en firebase con el login de facebook
                    handleFacebookAccessToken(loginResult.getAccessToken());
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
        } catch (ExceptionInInitializerError e) {
            //1ºCrash Reporting
            FirebaseCrash.report(e);
        }


        ///////////////LOGIN GOOGLE/////////////
        /*
        Configuración el inicio de sesión de Google para solicitar los datos de usuario requeridos por su aplicación
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(String.valueOf(R.string.CredentialTokenGoogle))
                .requestEmail()
                .build();
        //Se debe de instanciar un objeto de tipo sigin client de google con el googleSignInoptions que hemos creado
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Asociamos al siginButton su componente visual y seteamos su tamaño
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        //Seteamos el escuchador del botón de google que será el MainActivityEvents
        signInButton.setOnClickListener(this.getMainActivityEvents());



        /*
        A través de la clase AnimationUtils podemos asignar a un elemento una animación que tengamos creada
        en 'anim'. Para ello haremos uso de su método loadAnimation para cargarla y la guardaremos
         en una variable de tipo animation para luego llamar a startAnimation desde el onclick del events y pasarsela como parámetro
        para asignarsela al elemento.
        */
        //recordamos que el contexto debe de ser cualquierla que actue de contenedor de este componente por lo tanto nos vale el propio MainActivity
        this.setFacebookAnimarion(AnimationUtils.loadAnimation(this, R.anim.facebook_anim));

        //Seteamos el listener de la animación que será nuestro MainActivityEvents
        this.loginButton.startAnimation(this.getFacebookAnimarion());


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
            //Si es google
        } else if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //llamamos al método handleSingResult pasándole por parámetro el objeto siginAccount obtenido del task
            // El GoogleSignInAccount objeto contiene información sobre el usuario que inició sesión, como el nombre del usuario.
            handleSignInResult(task);
        }


    }

    /*
    Método que se encarga de loguear con firebase la cuenta facebook
    */
    private void handleFacebookAccessToken(AccessToken token) {
        try{
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            DataHolder.MyDataHolder.getFirebaseAdmin().getmAuth().signInWithCredential(credential)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = DataHolder.MyDataHolder.getFirebaseAdmin().getmAuth().getCurrentUser();
                                firebaseAdminListener.loginIsOk(true);
                            } else {

                                // If sign in fails, display a message to the user.
                                Log.w("Logueo", "signInWithCredential:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }catch(FacebookException e){
            //3ºCrash Reporting
            FirebaseCrash.report(e);
        }


    }

    //Método que se encarga de devolvernos un resultado si se ha logueado correctamente o no el usuario por google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERROR LOGUEO GOOGLE", "signInResult:failed code=" + e.getStatusCode());
            //2ºCrash Reporting
            FirebaseCrash.report(e);

        }
    }


    //Método para el logueo de google account con firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        System.out.println("Dentro???? del google");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        DataHolder.MyDataHolder.getFirebaseAdmin().getmAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = DataHolder.MyDataHolder.getFirebaseAdmin().getmAuth().getCurrentUser();
                            firebaseAdminListener.loginIsOk(true);

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                        // ...
                    }
                });
    }


    public MainActivityEvents getMainActivityEvents() {
        return mainActivityEvents;
    }

    public void setMainActivityEvents(MainActivityEvents mainActivityEvents) {
        this.mainActivityEvents = mainActivityEvents;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    public FirebaseAdminListener getFirebaseAdminListener() {
        return firebaseAdminListener;
    }

    public void setFirebaseAdminListener(FirebaseAdminListener firebaseAdminListener) {
        this.firebaseAdminListener = firebaseAdminListener;
    }

    public Animation getFacebookAnimarion() {
        return facebookAnimarion;
    }

    public void setFacebookAnimarion(Animation facebookAnimarion) {
        this.facebookAnimarion = facebookAnimarion;
    }
}


