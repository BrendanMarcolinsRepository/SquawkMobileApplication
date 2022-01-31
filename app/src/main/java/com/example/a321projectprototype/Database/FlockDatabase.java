package com.example.a321projectprototype.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a321projectprototype.User.FlockModelData;

import java.util.ArrayList;

public class FlockDatabase extends SQLiteOpenHelper
{
    private String name;
    private int groupNumber;
    private String description;
    private boolean privateFlock = true;

    public static final String DATABASE_NAME = "FlockDatabase";
    public static final String USERS_TABLE_NAME = "flock";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_GROUPNUMBER = "groupNumber";
    public static final String USERS_COLUMN_DESCRIPTION = "description";
    public static final String USERS_COLUMN_PRIVATEFLOCK = "privateFlock";
    public static final String USERS_COLUMN_OWNERUSERNAME = "ownerUsername";
    public static final String USERS_COLUMN_SCORE= "score";

    // constructor needed for the database
    public FlockDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,1);

    }


    @Override
    // for instance of the database creates the tables needed by using the above variables
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + "("
                + USERS_COLUMN_ID + " INTEGER  PRIMARY KEY," + USERS_COLUMN_NAME + " TEXT,"
                + USERS_COLUMN_GROUPNUMBER + " TEXT," + USERS_COLUMN_DESCRIPTION + " TEXT," + USERS_COLUMN_PRIVATEFLOCK + " TEXT," +
                USERS_COLUMN_OWNERUSERNAME + " TEXT," + USERS_COLUMN_SCORE + " TEXT" +")";
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
    public void addFlock(FlockModelData flock ) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_ID, flock.getId());
        contentValues.put(USERS_COLUMN_NAME, flock.getName());
        contentValues.put(USERS_COLUMN_GROUPNUMBER, flock.getGroupNumber());
        contentValues.put(USERS_COLUMN_DESCRIPTION, flock.getDescription());
        contentValues.put(USERS_COLUMN_PRIVATEFLOCK, flock.isPrivateFlock());;
        contentValues.put(USERS_COLUMN_OWNERUSERNAME, flock.getOwnerUsername());
        contentValues.put(USERS_COLUMN_SCORE, flock.getScore());;
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
    public ArrayList<FlockModelData> getAllUsers()
    {
        //Array List needed
        ArrayList<FlockModelData> flockModelDataArrayList = new ArrayList<FlockModelData>();

        //string query to selete the table name
        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME;

        //used to make the query
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);

        //adds the users to the arraylist and returns it
        while(cursor.moveToNext())
        {
            FlockModelData flock = new FlockModelData();
            flock.setId(Integer.parseInt(cursor.getString(0)));
            flock.setName(cursor.getString(1));
            flock.setGroupNumber(Integer.parseInt(cursor.getString(2)));
            flock.setDescription(cursor.getString(3));
            flock.setPrivateFlock(isPrivateFlock(cursor.getString(4)));
            flock.setOwnerUsername(cursor.getString(5));
            flock.setScore(Integer.parseInt(cursor.getString(6)));
            flockModelDataArrayList.add(flock);
        }


        return flockModelDataArrayList;
    }



    //used to get one particular user
    public FlockModelData getFlock(String name)
    {

        //used to get the specified selection query
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + USERS_TABLE_NAME
                + " WHERE " + USERS_COLUMN_NAME + " = " + name;

        //query on the database table that match these variables amd gets the selection passed
        Cursor cursor = database.query(USERS_TABLE_NAME, new String[] { USERS_COLUMN_ID,
                        USERS_COLUMN_NAME, USERS_COLUMN_GROUPNUMBER,USERS_COLUMN_DESCRIPTION,
                        USERS_COLUMN_PRIVATEFLOCK,USERS_COLUMN_OWNERUSERNAME,USERS_COLUMN_SCORE}, USERS_COLUMN_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);

        //cursor will move if not null
        if (cursor != null && cursor.moveToFirst())
        {
            //returns curso selections into a new user object
            FlockModelData flock = new FlockModelData(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3),isPrivateFlock(cursor.getString(4)), cursor.getString(5), Integer.parseInt(cursor.getString(6)));

            cursor.close();

            //returns the user object
            return flock;
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
        contentValues.put(USERS_COLUMN_GROUPNUMBER, flockModelData.getGroupNumber());
        contentValues.put(USERS_COLUMN_DESCRIPTION, flockModelData.getDescription());
        contentValues.put(USERS_COLUMN_PRIVATEFLOCK, putPrivate(flockModelData));
        contentValues.put(USERS_COLUMN_OWNERUSERNAME, flockModelData.getOwnerUsername());
        contentValues.put(USERS_COLUMN_SCORE, flockModelData.getScore());


        database.update(USERS_TABLE_NAME, contentValues,USERS_COLUMN_ID + " = ?",
                new String[] {String.valueOf(flockModelData.getId())});

    }

    private String putPrivate(FlockModelData flockModelData)
    {
        if(flockModelData.isPrivateFlock())
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
