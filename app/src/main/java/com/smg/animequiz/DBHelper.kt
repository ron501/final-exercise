package com.smg.animequiz

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object{
        private val DATABASE_NAME = "MY_TITLES"
        private val DATABASE_VERSION = 1
        private val TABLE_NAME = "main_table"
        private val ID_COL = "id"
        val NAME_COL = "name"
        val LINK_COL = "link"
        val POSTER_LINK_COL = "poster_link"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMACY KEY, " +
                NAME_COL + " TEXT, " +
                LINK_COL + " TEXT," +
                POSTER_LINK_COL + " TEXT," +
                "UNIQUE($NAME_COL)" +
                ")" )
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME)
        onCreate(db)
    }

    fun getTitle(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun addTitle(name: String, link: String, posterLink: String){
        val values = ContentValues()
        val db = this.readableDatabase
        values.put(NAME_COL, name)
        values.put(LINK_COL, link)
        values.put(POSTER_LINK_COL, posterLink)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
}
