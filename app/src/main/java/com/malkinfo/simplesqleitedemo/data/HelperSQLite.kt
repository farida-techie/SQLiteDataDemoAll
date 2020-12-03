package com.malkinfo.simplesqleitedemo.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HelperSQLite(c:Context):
SQLiteOpenHelper(c,"ABCDB",null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ACTABLE(_id INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT , MEANING TEXT)")
        db?.execSQL("INSERT INTO ACTABLE(NAME , MEANING) VALUES('www','word wide web')")
        db?.execSQL("INSERT INTO ACTABLE(NAME , MEANING) VALUES('MCA','master of computer application')")
        db?.execSQL("INSERT INTO ACTABLE(NAME , MEANING) VALUES('GDG','google Developer Group')")
        db?.execSQL("INSERT INTO ACTABLE(NAME , MEANING) VALUES('AVD','Android virtual devise')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}