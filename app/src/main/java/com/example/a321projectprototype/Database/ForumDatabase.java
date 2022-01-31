package com.example.a321projectprototype.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.ForumModel;

import java.util.ArrayList;

public class ForumDatabase extends SQLiteOpenHelper
{
    private String name;
    private int groupNumber;
    private String description;
    private boolean privateFlock = true;

    public static final String DATABASE_NAME = "ForumDatabase";
    public static final String USERS_TABLE_NAME = "forum";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "username";
    public static final String USERS_COLUMN_TOPIC = "topic";
    public static final String USERS_COLUMN_DESCRIPTION = "description";

    // constructor needed for the database
    public ForumDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,1);

    }


    @Override
    // for instance of the database creates the tables needed by using the above variables
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + "("
                + USERS_COLUMN_ID + " INTEGER  PRIMARY KEY," + USERS_COLUMN_NAME + " TEXT,"
                + USERS_COLUMN_TOPIC + " TEXT," + USERS_COLUMN_DESCRIPTION +  ", TEXT" +")";
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
    public void addFlock(ForumModel forum ) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_ID, forum.getId());
        contentValues.put(USERS_COLUMN_NAME, forum.getUsername());
        contentValues.put(USERS_COLUMN_TOPIC, forum.getTopic());
        contentValues.put(USERS_COLUMN_DESCRIPTION, forum.getDescription());
        database.insert(USERS_TABLE_NAME, null, contentValues);
        database.close();

    }

    //checks if a user is in the database by using the email and password passed
    public boolean checkUser(String name)
    {
        //gets the columns id
        String[] columns = {USERS_COLUMN_ID};
        //assigns the a readble sqlitedatabase object
        SQLiteDatabase db = getReadableDatabase();
        //string to select the rows
        String selection = USERS_COLUMN_NAME + "=?";
        //string array to store those selections
        String[] selectionArgs = {name};
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
    public ArrayList<ForumModel> getAllUsers()
    {
        //Array List needed
        ArrayList<ForumModel> forumModelDataArrayList = new ArrayList<ForumModel>();

        //string query to selete the table name
        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME;

        //used to make the query
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);

        //adds the users to the arraylist and returns it
        while(cursor.moveToNext())
        {
            ForumModel forum = new ForumModel();
            forum.setId(Integer.parseInt(cursor.getString(0)));
            forum.setUsername(cursor.getString(1));
            forum.setTopic(cursor.getString(2));
            forum.setDescription(cursor.getString(3));
            forumModelDataArrayList.add(forum);
        }


        return forumModelDataArrayList;
    }



    //used to get one particular user
    public ForumModel getFlock(String name)
    {

        //used to get the specified selection query
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME
                + " WHERE " + USERS_COLUMN_TOPIC + " = '" + name + "'";

        //query on the database table that match these variables amd gets the selection passed
        Cursor cursor = database.query(USERS_TABLE_NAME, new String[] { USERS_COLUMN_ID,
                        USERS_COLUMN_NAME, USERS_COLUMN_TOPIC,USERS_COLUMN_DESCRIPTION}, USERS_COLUMN_TOPIC + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);

        //cursor will move if not null
        if (cursor != null && cursor.moveToFirst())
        {
            //returns curso selections into a new user object
            ForumModel forum = new ForumModel(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3));

            cursor.close();

            //returns the user object
            return forum;
        }

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
    public void updateUserCritical(FlockModelData flockModelData)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_ID, flockModelData.getId());
        contentValues.put(USERS_COLUMN_NAME, flockModelData.getName());
        contentValues.put(USERS_COLUMN_TOPIC, flockModelData.getGroupNumber());
        contentValues.put(USERS_COLUMN_DESCRIPTION, flockModelData.getDescription());

        database.update(USERS_TABLE_NAME, contentValues,USERS_COLUMN_ID + " = ?",
                new String[] {String.valueOf(flockModelData.getId())});

    }

    private String putPrivate(ForumModel forumModel)
    {
        if(forumModel.getPrivatePost())
        {
            return "0";
        }
        else
        {
            return "1";
        }
    }

    private boolean isPrivateFlock(String data)
    {
        int number = Integer.parseInt(data);
        if(number == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
