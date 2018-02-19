package com.example.tay.examen2trimestre.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.tay.examen2trimestre.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tay on 29/1/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    /*
    Esta clase es un gestor que encarga de administrar la bbdd SQ Lite
     */

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 35;

    // Database Name
    private static final String DATABASE_NAME = "Examen2Trimestre";

    // Contacts table name
    private static final String TABLE_USERS = "Usuarios";

    // Contacts Table Columns names
    private static final String nombre = "nombre";
    private static final String lat = "lat";
    private static final String lon = "lon";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("------------------------------------------->>>>>>>>>dentro del handler del SQlite Databse");
        getReadableDatabase();

          /*
        Cuando se hace el new de DatabaseHandel se llama al super del que hereda que es SQLiteOpenHelper y se comprueba
        si esa versión de la bbdd existe. Si existe entonces no se llama a nada más y si no existe se llama al oncreate.
        Si ya exister pero hay una versión distinta de la bbdd entonces se llama al onUpgrade.
         */
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("------------------------------------------->>>>>>>>>dentro del onCreate del SQlite Databse");
        String CREATE_USERS_TABLE = " CREATE TABLE " + TABLE_USERS + "("
                + nombre + " TEXT," + lat + " REAL," + lon + " REAL" + ")";
        db.execSQL(CREATE_USERS_TABLE);
        System.out.println("---------------->>>>>>>>>>>>>>>>>>>>>>>EJECUTA ELA CREACIÓN DE LA BBDD");

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("------------------------>>>>>>>>>>>>>>>>>>se borra????");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }


    ////MÉTODOS CRUD

    //añadir nuevo User
    public void addUsers(User user) {
        SQLiteDatabase db = this.getWritableDatabase(); // para poder escribir en la bbdd SQLite hay que pedir permisos de escritura

        ContentValues values = new ContentValues(); //ContentValues es un hashMap de tipo ContentValues que se usa para recoger los datos que se quieren instar del objeto

        values.put(nombre, user.nombre); // Contact Phone Number
        values.put(lat, user.lat);
        values.put(lon, user.lon);
        // Insertar fila
        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection

        //Para completar el insert tenemos que hacer un .close() dado que funciona a modo de transacción y es como si hicieramos un commit
    }


    public User getUser(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase(); //tenemos que pedir permisos para leer de la bbdd

        //Un cursor es igual que un ResultSet de SQL que contiene los resultados obtenidos de la consulta y el cuál hay que interar para sacarlos
        Cursor cursor = db.query(TABLE_USERS, new String[]{
                        nombre, lat, lon}, nombre + "=?",
                new String[]{String.valueOf(nombre)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst(); //situa el cursor en el primer resultado para sacar los elementos a partir del primero


        //Estos resutlados se seteran en un nuevo obejto de tipo Remotemessage para usarlo como queramos
        User user = new User(cursor.getString(0), cursor.getDouble(1),
                cursor.getDouble(2));
        // return remoteMessage
        return user;
    }



    /*
    Método para obtener de golpe todos los mensajes de la bbdd SQ lite y guardarlos en un arraylist de tipo RemoteMessage
     */

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = " SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                user.nombre = cursor.getString(0);
                user.lat = cursor.getDouble(1);
                user.lon = cursor.getDouble(2);

                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userList;
    }

    public void deleteAllUsers() {

        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_USERS, null, null);
    }


}
