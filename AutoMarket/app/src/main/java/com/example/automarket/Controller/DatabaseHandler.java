package com.example.automarket.Controller;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.automarket.Model.Annonce;
import com.example.automarket.Model.User;
import com.example.automarket.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE "+ Utils.TABLE_USERS+" ("+
                Utils.KEY_ID+" INTEGER PRIMARY KEY,"+
                Utils.KEY_USERNAME+" TEXT, "+
                Utils.KEY_EMAIL+" TEXT, "+
                Utils.KEY_PHONE+" TEXT, "+
                Utils.KEY_PASSWORD+" TEXT"+")";

        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_ANNONCE_TABLE = "CREATE TABLE " + Utils.TABLE_ANNONCE + "(" +
                Utils.KEY_ID + " INTEGER PRIMARY KEY," +
                Utils.KEY_USER_ID + " INTEGER," +
                Utils.KEY_MARQUE + " TEXT," +
                Utils.KEY_MODELE + " TEXT," +
                Utils.KEY_BOITE + " TEXT," +
                Utils.KEY_ENERGIE + " TEXT," +
                Utils.KEY_MOTEUR + " TEXT," +
                Utils.KEY_KILOMETRAGE + " INTEGER," +
                Utils.KEY_COULEUR + " TEXT," +
                Utils.KEY_ANNEE + " INTEGER," +
                Utils.KEY_PRIX + " REAL," +
                Utils.KEY_PHOTO_URL + " TEXT," +
                Utils.KEY_COMMENTAIRE + " TEXT," +
                Utils.KEY_ADRESSE + " TEXT," +
                Utils.KEY_DATE + " TEXT" +")";

        db.execSQL(CREATE_ANNONCE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE_ANNONCE);
        onCreate(db);
    }


    public Boolean addUser(User user){
        SQLiteDatabase database =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Utils.KEY_USERNAME,user.getUsername());
        contentValues.put(Utils.KEY_EMAIL,user.getEmail());
        contentValues.put(Utils.KEY_PASSWORD,user.getPassword());
        contentValues.put(Utils.KEY_PHONE,user.getPhone());

        database.insert(Utils.TABLE_USERS,null,contentValues);
        database.close();

        return null;
    }

    public Boolean checkUsername (String username){
        SQLiteDatabase MyDtabase = this.getWritableDatabase();
        Cursor cursor = MyDtabase.rawQuery("select * from users where username = ?", new String[]{username} );

        if(cursor.getCount() >0 ){
    return  true;
        }else{
            return false;
        }
    }



    public User checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = null;

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

        if (cursor.moveToFirst()) {
            // Récupérer les détails de user
            int id = cursor.getInt(cursor.getInt(0));
            String email = cursor.getString(Integer.parseInt(cursor.getString(1)));
            int telephone = cursor.getInt(cursor.getInt(3));

            // Créer un objet User avec les donnees recuperer récupérés
            user = new User(id, username, email, telephone);
        }

        cursor.close();
        db.close();

        return user;
    }


    public User getUser(int id){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Utils.TABLE_ANNONCE,
                new String[]{Utils.KEY_ID,Utils.KEY_USERNAME,
                        Utils.KEY_EMAIL,Utils.KEY_PHONE,
                        Utils.KEY_PASSWORD},
                Utils.KEY_ID+"=?",new String[]{String.valueOf(id)},
                null,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
            User user = new User(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),cursor.getString(4));
            return user;
        }
        else{
            return null;
        }
    }

    public List<User> getAllUsers(){
        SQLiteDatabase database = this.getReadableDatabase();
        List<User> userList = new ArrayList<>();
        String getAll = "SELECT * FROM "+ Utils.TABLE_USERS;

        Cursor cursor = database.rawQuery(getAll,null);
        cursor.moveToFirst();

        if(cursor.moveToFirst())
            do {
                User user =new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setEmail(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                user.setPhone(cursor.getString(4));

                userList.add(user);
            }while(cursor.moveToNext());

        return userList;

    }

    public void addAnnonce(Annonce annonce, int userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utils.KEY_USER_ID, userId);
        values.put(Utils.KEY_MARQUE, annonce.getMarque());
        values.put(Utils.KEY_MODELE, annonce.getModele());
        values.put(Utils.KEY_BOITE, annonce.getBoite());
        values.put(Utils.KEY_ENERGIE, annonce.getEnergie());
        values.put(Utils.KEY_MOTEUR, annonce.getMoteur());
        values.put(Utils.KEY_KILOMETRAGE, annonce.getKilometrage());
        values.put(Utils.KEY_COULEUR, annonce.getCouleur());
        values.put(Utils.KEY_ANNEE, annonce.getAnnee());
        values.put(Utils.KEY_PRIX, annonce.getPrix());
        values.put(Utils.KEY_PHOTO_URL, annonce.getPhotoUrl());
        values.put(Utils.KEY_COMMENTAIRE, annonce.getCommentaire());
        values.put(Utils.KEY_ADRESSE, annonce.getAdresse());
        values.put(Utils.KEY_DATE, annonce.getDateCreation());

        database.insert(Utils.TABLE_ANNONCE, null, values);
        database.close();
    }

    public void updateAnnonce(Annonce annonce, int userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utils.KEY_USER_ID, userId);
        values.put(Utils.KEY_MARQUE, annonce.getMarque());
        values.put(Utils.KEY_MODELE, annonce.getModele());
        values.put(Utils.KEY_BOITE, annonce.getBoite());
        values.put(Utils.KEY_ENERGIE, annonce.getEnergie());
        values.put(Utils.KEY_MOTEUR, annonce.getMoteur());
        values.put(Utils.KEY_KILOMETRAGE, annonce.getKilometrage());
        values.put(Utils.KEY_COULEUR, annonce.getCouleur());
        values.put(Utils.KEY_ANNEE, annonce.getAnnee());
        values.put(Utils.KEY_PRIX, annonce.getPrix());
        values.put(Utils.KEY_PHOTO_URL, annonce.getPhotoUrl());
        values.put(Utils.KEY_COMMENTAIRE, annonce.getCommentaire());
        values.put(Utils.KEY_ADRESSE, annonce.getAdresse());

        database.update(Utils.TABLE_ANNONCE, values, Utils.KEY_ID + "=?",
                new String[]{String.valueOf(annonce.getId())});
        database.close();
    }

    public List<Annonce> getAllAnnoncesForUser(int userId) {
        List<Annonce> annonceList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Utils.TABLE_ANNONCE + " WHERE " + Utils.KEY_USER_ID + " = " + userId;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Annonce annonce = new Annonce();
                annonce.setId(cursor.getInt(0)); // 0 est l'index de la colonne ID
                annonce.setUserId(cursor.getInt(1)); // 1 est l'index de la colonne USER_ID
                annonce.setMarque(cursor.getString(2)); // 2 est l'index de la colonne MARQUE
                annonce.setModele(cursor.getString(3)); // 3 est l'index de la colonne MODELE
                annonce.setBoite(cursor.getString(4)); // 4 est l'index de la colonne BOITE
                annonce.setEnergie(cursor.getString(5)); // 5 est l'index de la colonne ENERGIE
                annonce.setMoteur(cursor.getString(6)); // 6 est l'index de la colonne MOTEUR
                annonce.setKilometrage(cursor.getInt(7)); // 7 est l'index de la colonne KILOMETRAGE
                annonce.setCouleur(cursor.getString(8)); // 8 est l'index de la colonne COULEUR
                annonce.setAnnee(cursor.getInt(9)); // 9 est l'index de la colonne ANNEE
                annonce.setPrix(cursor.getDouble(10)); // 10 est l'index de la colonne PRIX
                annonce.setPhotoUrl(cursor.getString(11)); // 11 est l'index de la colonne PHOTO_URL
                annonce.setCommentaire(cursor.getString(12)); // 12 est l'index de la colonne COMMENTAIRE
                annonce.setAdresse(cursor.getString(13)); // 13 est l'index de la colonne ADRESSE

                annonceList.add(annonce);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return annonceList;
    }

    public List<Annonce> getAllAnnonces() {
        List<Annonce> annonceList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Utils.TABLE_ANNONCE + " ORDER BY " + Utils.KEY_DATE + " DESC";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Annonce annonce = new Annonce();
                annonce.setId(cursor.getInt(0)); // 0 est l'index de la colonne ID
                annonce.setUserId(cursor.getInt(1)); // 1 est l'index de la colonne USER_ID
                annonce.setMarque(cursor.getString(2)); // 2 est l'index de la colonne MARQUE
                annonce.setModele(cursor.getString(3)); // 3 est l'index de la colonne MODELE
                annonce.setBoite(cursor.getString(4)); // 4 est l'index de la colonne BOITE
                annonce.setEnergie(cursor.getString(5)); // 5 est l'index de la colonne ENERGIE
                annonce.setMoteur(cursor.getString(6)); // 6 est l'index de la colonne MOTEUR
                annonce.setKilometrage(cursor.getInt(7)); // 7 est l'index de la colonne KILOMETRAGE
                annonce.setCouleur(cursor.getString(8)); // 8 est l'index de la colonne COULEUR
                annonce.setAnnee(cursor.getInt(9)); // 9 est l'index de la colonne ANNEE
                annonce.setPrix(cursor.getDouble(10)); // 10 est l'index de la colonne PRIX
                annonce.setPhotoUrl(cursor.getString(11)); // 11 est l'index de la colonne PHOTO_URL
                annonce.setCommentaire(cursor.getString(12)); // 12 est l'index de la colonne COMMENTAIRE
                annonce.setAdresse(cursor.getString(13)); // 13 est l'index de la colonne ADRESSE
                annonce.setDateCreation(cursor.getString(14));

                annonceList.add(annonce);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return annonceList;
    }

}
