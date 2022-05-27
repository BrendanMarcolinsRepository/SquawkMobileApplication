package com.example.a321projectprototype.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.a321projectprototype.User.Files;
import com.example.a321projectprototype.User.UserModel;

import java.io.File;
import java.util.ArrayList;
//Coding Done By Brendan Marcolin
public class RecordingPathFileDatabase extends SQLiteOpenHelper
{

    //variables needed for the database
    public static final String DATABASE_NAME = "FileDatabase";
    public static final String USERS_TABLE_NAME = "file";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_DESCRIPTION = "description";
    public static final String USERS_COLUMN_PATH = "path";
    public static final String USERS_COLUMN_CREATED_AT = "created_at";
    public static final String USERS_COLUMN_TIME = "time";
    public static final String USERS_COLUMN_UPDATED_AT = "updated_at";






    // constructor needed for the database
    public RecordingPathFileDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,1);

    }


    @Override
    // for instance of the database creates the tables needed by using the above variables
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + "("
                + USERS_COLUMN_ID + " INTEGER  PRIMARY KEY," + USERS_COLUMN_NAME + " TEXT," + USERS_COLUMN_DESCRIPTION + " TEXT,"
                + USERS_COLUMN_PATH + " TEXT," + USERS_COLUMN_CREATED_AT + " TEXT," + USERS_COLUMN_TIME + " TEXT," + USERS_COLUMN_UPDATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    //used to drop the tables if need to create a new database version
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    //used to add the users to the tables of the database by passing a user object
    public void addFile(Files files ) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_NAME,files.getFilename());
        contentValues.put(USERS_COLUMN_DESCRIPTION, files.getDescription());
        contentValues.put(USERS_COLUMN_PATH,files.getPath());
        contentValues.put(USERS_COLUMN_CREATED_AT, files.getCreated_at());
        contentValues.put(USERS_COLUMN_TIME, files.getTime());
        contentValues.put(USERS_COLUMN_UPDATED_AT, files.getUpdated_at());
        database.insert(USERS_TABLE_NAME, null, contentValues);
        database.close();

    }

    //retrieves the pariticular file
    public Files getFile(String path ) {
        SQLiteDatabase database = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USERS_COLUMN_PATH + " = '" + path + "'";


        Cursor cursor = database.rawQuery(selectQuery,null);

        System.out.println("workingggggg");
        //adds the users to the arraylist and returns it
        Files file = new Files();
        file.setFilename(cursor.getString(1));
        file.setDescription(cursor.getString(2));
        file.setPath(cursor.getString(3));
        file.setCreated_at(cursor.getString(4));
        file.setTime(cursor.getString(5));
        file.setUpdated_at(cursor.getString(6));
        cursor.close();
        database.close();
        return file;

    }




    //returns all the users in the database
    public ArrayList<Files> getAllFiles(String date)
    {
        //Array List needed
        ArrayList<Files> fileList = new ArrayList<>();

        //string query to selete the table name
        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USERS_COLUMN_CREATED_AT + " = '" + date + "'";

        //used to make the query
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);

        System.out.println("workingggggg");
        //adds the users to the arraylist and returns it
        while(cursor.moveToNext())
        {
            Files files = new Files();
            files.setFilename(cursor.getString(1));
            files.setDescription(cursor.getString(2));
            files.setPath(cursor.getString(3));
            files.setCreated_at(cursor.getString(4));
            files.setTime(cursor.getString(5));
            files.setUpdated_at(cursor.getString(6));
            fileList.add(files);

            System.out.println(files.getCreated_at() + " here database");
        }

        cursor.close();

        return fileList;
    }

    //deletes the file
    public void deleteFile(Files file){
        File myDir = new File(file.getPath());
        if (myDir.isDirectory()) {
            String[] children = myDir.list();
            for (int i = 0; i < children.length; i++) {
                new File(myDir, children[i]).delete();
            }
        }

        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = USERS_COLUMN_PATH + " =?";
        database.delete(USERS_TABLE_NAME,selectQuery,new String[]{file.getPath()});
        database.close();



    }


}
