package com.example.a321projectprototype.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.UserModel;

import java.util.ArrayList;

public class UserDatabase extends SQLiteOpenHelper
{

    //variables needed for the database
    public static final String DATABASE_NAME = "UsersDatabase";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PASSWORD = "password";
    public static final String USERS_COLUMN_FLOCK = "flockName";

    // constructor needed for the database
    public UserDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,1);

    }


    @Override
    // for instance of the database creates the tables needed by using the above variables
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + "("
                + USERS_COLUMN_ID + " INTEGER  PRIMARY KEY," + USERS_COLUMN_NAME + " TEXT," + USERS_COLUMN_USERNAME + " TEXT,"
                + USERS_COLUMN_EMAIL + " TEXT," + USERS_COLUMN_PASSWORD + " TEXT," + USERS_COLUMN_FLOCK + " TEXT" + ")";
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
    public void addUsers(UserModel user ) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_ID, user.getId());
        contentValues.put(USERS_COLUMN_NAME, user.getName());
        contentValues.put(USERS_COLUMN_USERNAME, user.getUsername());
        contentValues.put(USERS_COLUMN_EMAIL, user.getEmail());
        contentValues.put(USERS_COLUMN_PASSWORD, user.getPassword());
        contentValues.put(USERS_COLUMN_FLOCK, user.getUserFlock());;
        database.insert(USERS_TABLE_NAME, null, contentValues);
        database.close();

    }

    //checks if a user is in the database by using the email and password passed
    public boolean checkUser(String username, String password)
    {
        //gets the columns id
        String[] columns = {USERS_COLUMN_ID};
        //assigns the a readble sqlitedatabase object
        SQLiteDatabase db = getReadableDatabase();
        //string to select the rows
        String selection = USERS_COLUMN_USERNAME + "=?" + " and " + USERS_COLUMN_PASSWORD + "=?";
        //string array to store those selections
        String[] selectionArgs = {username, password};
        //cursor object used to make the query on the table name
        Cursor cursor = db.query(USERS_TABLE_NAME, columns,selection,selectionArgs,null,null,null);
        //count used to see if there is a user by those details, 1 yes and 0 no
        int count = cursor.getCount();
        cursor.close();


        //returns a boolean depending on the count
        if(count>0)
            return true;
        else
            return false;
    }

    //returns all the users in the database
    public ArrayList<UserModel> getAllUsers()
    {
        //Array List needed
        ArrayList<UserModel> users = new ArrayList<UserModel>();

        //string query to selete the table name
        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME;

        //used to make the query
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);

        //adds the users to the arraylist and returns it
        while(cursor.moveToNext())
        {
            UserModel user = new UserModel();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setUsername(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setUserFlock(cursor.getString(5));
            users.add(user);
        }


        return users;
    }

    //used to get one particular user
    public UserModel getUser(String username)
    {

        //used to get the specified selection query
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME
                + " WHERE " + USERS_COLUMN_USERNAME + " = " + username;

        //query on the database table that match these variables amd gets the selection passed
        Cursor cursor = database.query(USERS_TABLE_NAME, new String[] { USERS_COLUMN_ID,
                        USERS_COLUMN_NAME,USERS_COLUMN_USERNAME, USERS_COLUMN_EMAIL,USERS_COLUMN_PASSWORD,
                        USERS_COLUMN_FLOCK}, USERS_COLUMN_USERNAME + "=?",
                new String[] { String.valueOf(username) }, null, null, null, null);

        UserModel user;
        //cursor will move if not null
        if (cursor != null && cursor.moveToFirst())
        {
            user = new UserModel(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5));

            cursor.close();

            return user;
        }

        //returns curso selections into a new user object


        //returns the user object
        return null;
    }

    //used to the amount of contacts in the database
    public int getContactsCount()
    {
        String countQuery = "SELECT  * FROM " + USERS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    //used to update a user in the database by passing an object user
    public void updateUserCritical(UserModel user)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_ID, user.getId());
        contentValues.put(USERS_COLUMN_NAME, user.getName());
        contentValues.put(USERS_COLUMN_USERNAME, user.getUsername());
        contentValues.put(USERS_COLUMN_EMAIL, user.getEmail());
        contentValues.put(USERS_COLUMN_PASSWORD, user.getPassword());
        contentValues.put(USERS_COLUMN_FLOCK, user.getUserFlock());


        database.update(USERS_TABLE_NAME, contentValues,USERS_COLUMN_ID + " = ?",
                new String[] {String.valueOf(user.getId())});

    }
}
