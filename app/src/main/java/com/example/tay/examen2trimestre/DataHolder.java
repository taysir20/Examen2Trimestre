package com.example.tay.examen2trimestre;


import com.example.tay.examen2trimestre.firebase.FirebaseAdmin;

/**
 * Created by tay on 25/11/17.
 */

public class DataHolder {

    public static class MyDataHolder {
        public static FirebaseAdmin firebaseAdmin;
        //public static DatabaseHandler databaseHandler;


        public static FirebaseAdmin getFirebaseAdmin() {
            return firebaseAdmin;
        }

        public static void setFirebaseAdmin(FirebaseAdmin firebaseAdmin) {
            MyDataHolder.firebaseAdmin = firebaseAdmin;
        }
    /*
        public static DatabaseHandler getDatabaseHandler() {
            return databaseHandler;
        }

        public static void setDatabaseHandler(DatabaseHandler databaseHandler) {
            MyDataHolder.databaseHandler = databaseHandler;
        }
    */
    }


}