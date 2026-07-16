package com.example.plantcareguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlantsApp.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_USERNAME = "username";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";


    private static final String TABLE_PLANTS = "plants";
    private static final String COL_PLANT_ID = "id";
    private static final String COL_PLANT_NAME = "name";
    private static final String COL_PLANT_CATEGORY = "category";
    private static final String COL_PLANT_WATER = "water_schedule";
    private static final String COL_PLANT_SUN = "sunlight";
    private static final String COL_PLANT_NOTES = "notes";
    private static final String COL_PLANT_USER_ID = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_USERNAME + " TEXT UNIQUE, " +
                COL_USER_EMAIL + " TEXT, " +
                COL_USER_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);


        String createPlantsTable = "CREATE TABLE " + TABLE_PLANTS + " (" +
                COL_PLANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLANT_NAME + " TEXT, " +
                COL_PLANT_CATEGORY + " TEXT, " +
                COL_PLANT_WATER + " TEXT, " +
                COL_PLANT_SUN + " TEXT, " +
                COL_PLANT_NOTES + " TEXT, " +
                COL_PLANT_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_PLANT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
        db.execSQL(createPlantsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }



    public boolean insertUser(String name, String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_USERNAME, username);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }


    public int checkUser(String loginInput, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_USER_ID + " FROM " + TABLE_USERS + " WHERE (" +
                COL_USER_USERNAME + "=? OR " + COL_USER_EMAIL + "=?) AND " + COL_USER_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{loginInput, loginInput, password});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_USERNAME + "=?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean insertPlant(String name, String category, String water, String sun, String notes, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PLANT_NAME, name);
        values.put(COL_PLANT_CATEGORY, category);
        values.put(COL_PLANT_WATER, water);
        values.put(COL_PLANT_SUN, sun);
        values.put(COL_PLANT_NOTES, notes);
        values.put(COL_PLANT_USER_ID, userId);

        long result = db.insert(TABLE_PLANTS, null, values);
        return result != -1;
    }


    public Cursor getPlantsByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PLANTS + " WHERE " + COL_PLANT_USER_ID + "=? ORDER BY " + COL_PLANT_ID + " DESC",
                new String[]{String.valueOf(userId)});
    }


    public Cursor getPlantsByUserIdAndCategory(int userId, String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PLANTS + " WHERE " + COL_PLANT_USER_ID + "=? AND " + COL_PLANT_CATEGORY + "=? ORDER BY " + COL_PLANT_ID + " DESC",
                new String[]{String.valueOf(userId), category});
    }


    public boolean updatePlant(int plantId, String name, String category, String water, String sun, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PLANT_NAME, name);
        values.put(COL_PLANT_CATEGORY, category);
        values.put(COL_PLANT_WATER, water);
        values.put(COL_PLANT_SUN, sun);
        values.put(COL_PLANT_NOTES, notes);

        int result = db.update(TABLE_PLANTS, values, COL_PLANT_ID + "=?", new String[]{String.valueOf(plantId)});
        return result > 0;
    }


    public boolean deletePlant(int plantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PLANTS, COL_PLANT_ID + "=?", new String[]{String.valueOf(plantId)});
        return result > 0;
    }

    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE id=?", new String[]{String.valueOf(userId)});
    }
}