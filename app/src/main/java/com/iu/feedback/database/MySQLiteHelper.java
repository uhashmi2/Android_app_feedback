package com.iu.feedback.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class MySQLiteHelper extends SQLiteOpenHelper {

    static final String TABLE_FORM = "FORM";
    static final String TABLE_FIELD = "FIELD";
    static final String TABLE_FORM_FIELD = "FORM_FIELD";
    static final String TABLE_FIELD_OPTIONS = "FIELD_OPTIONS";

    static final String TABLE_FEEDBACK = "FEEDBACK";
    static final String TABLE_FIELD_RESPONSE = "FIELD_RESPONSE";
    static final String TABLE_FIELD_RESPONSE_OPTIONS = "FIELD_RESPONSE_OPTION";
    //TABLE_FORM
    static String FORM_ID = "id";
    static String FORM_TITLE = "title";
    static String FORM_DESCRIPTION = "description";
    static String FORM_DATE = "date";
    private static final String CREATE_TABLE_FORM = "create table if not exists "
            + TABLE_FORM + "(" + FORM_ID
            + " integer primary key autoincrement, " + FORM_TITLE
            + " VARCHAR ,  " + FORM_DESCRIPTION
            + " TEXT ,  " + FORM_DATE
            + " BIGINT '');";

    //TABLE_FIELD
    static String FIELD_ID = "id";
    //    static String FIELD_NAME = "name";
    static String FIELD_TYPE = "type";
    private static final String CREATE_TABLE_FIELD = "create table if not exists "
            + TABLE_FIELD + "(" + FIELD_ID
            + " integer primary key, " + FIELD_TYPE
//            + " VARCHAR ,  " + FIELD_NAME
            + " VARCHAR '');";
    //TABLE_FORM_FIELD
    static String FORM_FIELD_ID = "id";
    static String FORM_ID_FK = "form_id";
    static String FIELD_ID_FK = "field_id";
    static String FORM_FIELD_STATUS = "status";
    static String FORM_FIELD_TITLE = "title";
    private static final String CREATE_TABLE_FORM_FIELD = "create table if not exists "
            + TABLE_FORM_FIELD + "(" + FORM_FIELD_ID
            + " integer primary key autoincrement, " + FORM_ID_FK
            + " integer ,  " + FIELD_ID_FK
            + " integer ,  " + FORM_FIELD_STATUS
            + " integer ,  " + FORM_FIELD_TITLE
            + " VARCHAR '');";
    //TABLE_FIELD_OPTIONS
    static String FIELD_OPTIONS_ID = "id";
    static String FIELD_OPTIONS_NAME = "name";
    static String FIELD_OPTIONS_FIELD_FK = "field_id";
    static String FIELD_OPTIONS_FORM_FIELD_ID_FK = "form_field_id";
    private static final String CREATE_TABLE_FIELD_OPTIONS = "create table if not exists "
            + TABLE_FIELD_OPTIONS + "(" + FIELD_OPTIONS_ID
            + " integer primary key autoincrement, " + FIELD_OPTIONS_NAME
            + " VARCHAR ,  " + FIELD_OPTIONS_FIELD_FK
            + " integer ,  " + FIELD_OPTIONS_FORM_FIELD_ID_FK
            + " integer '');";


    //TABLE_FEEDBACK
    static String FEEDBACK_ID = "id";
    static String FEEDBACK_FORM_ID_FK = "form_id";
    static String FEEDBACK_DATE = "date";
    static String FEEDBACK_SUGGESTIONS = "suggestion";
    static String FEEDBACK_NAME = "name";
    static String FEEDBACK_CONTACT = "contact";
    static String FEEDBACK_EMAIL = "email";
    static String FEEDBACK_DOB = "dob";
    private static final String CREATE_TABLE_FEEDBACK = "create table if not exists "
            + TABLE_FEEDBACK + "(" + FEEDBACK_ID
            + " integer primary key autoincrement, " + FEEDBACK_FORM_ID_FK
            + " integer ,  " + FEEDBACK_SUGGESTIONS
            + " VARCHAR ,  " + FEEDBACK_NAME
            + " VARCHAR ,  " + FEEDBACK_CONTACT
            + " VARCHAR ,  " + FEEDBACK_EMAIL
            + " VARCHAR ,  " + FEEDBACK_DOB
            + " VARCHAR ,  " + FEEDBACK_DATE
            + " BIGINT '');";

    //TABLE_FIELD_RESPONSE
    static String FIELD_RESPONSE_ID = "id";
    static String FIELD_RESPONSE_FIELD_ID_FK = "field_id";
    static String FIELD_RESPONSE_FEEDBACK_ID_FK = "feedback_id";
    static String FIELD_RESPONSE_FORM_FIELD_ID_FK = "form_field_id";
    static String FIELD_RESPONSE_DATE = "date";
    private static final String CREATE_TABLE_FIELD_RESPONSE = "create table if not exists "
            + TABLE_FIELD_RESPONSE + "(" + FIELD_RESPONSE_ID
            + " integer primary key autoincrement, " + FIELD_RESPONSE_FIELD_ID_FK
            + " integer ,  " + FIELD_RESPONSE_FEEDBACK_ID_FK
            + " integer ,  " + FIELD_RESPONSE_FORM_FIELD_ID_FK
            + " integer ,  " + FIELD_RESPONSE_DATE
            + " BIGINT '');";

    //TABLE_FIELD_RESPONSE_OPTIONS
    static String FIELD_RESPONSE_OPTIONS_ID = "id";
    static String FIELD_RESPONSE_ID_FK = "field_response_id";
    static String FIELD_RESPONSE_VALUES = "value";
    private static final String CREATE_TABLE_FIELD_RESPONSE_OPTIONS = "create table if not exists "
            + TABLE_FIELD_RESPONSE_OPTIONS + "(" + FIELD_RESPONSE_OPTIONS_ID
            + " integer primary key autoincrement, " + FIELD_RESPONSE_ID_FK
            + " integer ,  " + FIELD_RESPONSE_VALUES
            + " VARCHAR '');";

    MySQLiteHelper(Context context) {
        super(context, "Feedback", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FORM);
        db.execSQL(CREATE_TABLE_FIELD);
        db.execSQL(CREATE_TABLE_FORM_FIELD);
        db.execSQL(CREATE_TABLE_FIELD_OPTIONS);

        db.execSQL(CREATE_TABLE_FEEDBACK);
        db.execSQL(CREATE_TABLE_FIELD_RESPONSE);
        db.execSQL(CREATE_TABLE_FIELD_RESPONSE_OPTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM_FIELD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_OPTIONS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_RESPONSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_RESPONSE_OPTIONS);
        // Create Tables again;
        onCreate(db);
    }


}
