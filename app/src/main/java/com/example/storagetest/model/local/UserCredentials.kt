package com.example.storagetest.model.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 2.- subclass the SqliteOpenHelper
 *
 */
class UserCredentials(
    context: Context,
    databaseName: String,
    version: Int
) : SQLiteOpenHelper(
    context, // will be ApplicationContext
    databaseName, // file name in your device
    null, // factory to create multiple databases
    version // your current DB version. +1
) {
    /**
     * Run the SQL query to create all your Tables
     */
    override fun onCreate(p0: SQLiteDatabase?) {
        // CREATE TABLE A (A.COLUMN VARCHAR,);
        p0?.execSQL("CREATE TABLE ${UserTable.TABLE_NAME} ("+
        "${UserTable.TABLE_COLUMN_FIRST} VARCHAR(255), "+
        "${UserTable.TABLE_COLUMN_ADDR} VARCHAR(255), "+
        "${UserTable.TABLE_COLUMN_LAST} VARCHAR(255), "+
        "${UserTable.TABLE_COLUMN_PHONE} VARCHAR(255));"
        )
    }

    /**
     *  Invoked when a change on the schema changes.
     */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // p0?.exeq("DROP TABLE IF EXIST TABLE_NAME")
    }

}






