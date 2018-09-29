package com.example.rodrigoespinoza.fragmentos.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlConecttion  extends SQLiteOpenHelper {
    final String CREATE_TABLE_USER = "CREATE TABLE user (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "email VARCHAR (50) NOT NULL, " +
            "pass  VARCHAR (50) NOT NULL, " +
            "fecha DATE NOT NULl)";

    final String CREATE_TABLE_PERSON ="CREATE TABLE person(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "rut VARCHAR(50) NOT NULL," +
            "name VARCHAR(50) NOT NULL, " +
            "last_name VARCHAR(50) NOT NULL," +
            "sexo VARCHAR(100) NOT NULL, " +
            "location VARCHAR(100) NOT NULL, " +
            "id_user INTEGER NOT NULL," +
            "FOREIGN KEY(id_user) REFERENCES user(id))";

    final String CREATE_TABLE_PEDIDO = "CREATE TABLE pedido(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "fecha DATE NOT NULL, " +
            "estado VARCHAR(50) NOT NULL, " +
            "total INTEGER NOT NULL, " +
            "id_person INTEGER NOT NULL, " +
            "id_product INTEGER NOT NULL, " +
            "FOREIGN KEY(id_person) REFERENCES person(id), " +
            "FOREIGN KEY(id_product) REFERENCES product(id))";

    final String CREATE_TABLE_PRODUCT = "CREATE TABLE product(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "name VARCHAR(100) NOT NULL, " +
            "stock INTEGER NOT NULL)";

    String[] create_sentences = {CREATE_TABLE_USER, CREATE_TABLE_PERSON, CREATE_TABLE_PEDIDO, CREATE_TABLE_PRODUCT};

    public SqlConecttion(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(Integer i=0; i<this.create_sentences.length; i++){
            db.execSQL(this.create_sentences[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS person");
        db.execSQL("DROP TABLE IF EXISTS product");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS pedido");
    }
}
