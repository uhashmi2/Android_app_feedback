package com.iu.feedback.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iu.feedback.model.Feedback;
import com.iu.feedback.model.FeedbackResponse;
import com.iu.feedback.model.FieldType;
import com.iu.feedback.model.Fields;
import com.iu.feedback.model.Form;

import java.util.ArrayList;
import java.util.List;

public class MyDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    //----------- For From Table ---------------------
    private String[] columns_table_form = {
            MySQLiteHelper.FORM_ID,
            MySQLiteHelper.FORM_TITLE,
            MySQLiteHelper.FORM_DESCRIPTION,
            MySQLiteHelper.FORM_DATE};


    //----------- For Feedback Table ---------------------
    private String[] columns_table_feedback = {
            MySQLiteHelper.FEEDBACK_ID,
            MySQLiteHelper.FEEDBACK_FORM_ID_FK,
            MySQLiteHelper.FEEDBACK_DATE,
            MySQLiteHelper.FEEDBACK_SUGGESTIONS,
            MySQLiteHelper.FEEDBACK_NAME,
            MySQLiteHelper.FEEDBACK_CONTACT,
            MySQLiteHelper.FEEDBACK_EMAIL,
            MySQLiteHelper.FEEDBACK_DOB};


    public MyDataSource(Context ctx) {
        if (dbHelper == null) {
            dbHelper = new MySQLiteHelper(ctx);
        }
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void InsertFields(List<FieldType> field) {
        try {
            if (database == null || !database.isOpen()) {
                this.open();
            }

            for (FieldType fieldType : field) {
                //Insert the field table
                ContentValues fieldValues = new ContentValues();
                fieldValues.put(MySQLiteHelper.FIELD_ID, fieldType.getId());
                fieldValues.put(MySQLiteHelper.FIELD_TYPE, fieldType.getType());
                database.insert(MySQLiteHelper.TABLE_FIELD, null, fieldValues);

            }
            if (database != null && database.isOpen()) {
                this.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Form> getForms() {
        ArrayList<Form> mList = new ArrayList<>();
        if (database == null || !database.isOpen()) {
            this.open();
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FORM, columns_table_form, null, null, null, null, MySQLiteHelper.FORM_DATE + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Form mObj = cursorToPunch(cursor);
            mList.add(mObj);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        if (database != null && database.isOpen()) {
            this.close();
        }
        return mList;
    }

    public int getLastId(String table, String id) {
        int _id = 0;
        try {
            if (database == null || !database.isOpen()) {
                this.open();
            }
            Cursor cursor = database.query(table, new String[]{id}, null, null, null, null, null);
            if (cursor.moveToLast()) {
                _id = cursor.getInt(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _id;
    }

    public void InsertFromData(Form form) {
        try {
            if (database == null || !database.isOpen()) {
                this.open();
            }
            //Insert the form
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.FORM_TITLE, form.getTitle());
            values.put(MySQLiteHelper.FORM_DESCRIPTION, form.getDescription());
            values.put(MySQLiteHelper.FORM_DATE, form.getDate());
            database.insert(MySQLiteHelper.TABLE_FORM, null, values);
            int formId = getLastId(MySQLiteHelper.TABLE_FORM, MySQLiteHelper.FORM_ID);

            for (Fields fields : form.getFieldsList()) {
                //Insert the form field table
                ContentValues formFieldValues = new ContentValues();
                formFieldValues.put(MySQLiteHelper.FORM_ID_FK, formId);
                formFieldValues.put(MySQLiteHelper.FIELD_ID_FK, fields.getFieldId());
                formFieldValues.put(MySQLiteHelper.FORM_FIELD_STATUS, fields.getStatus());
                formFieldValues.put(MySQLiteHelper.FORM_FIELD_TITLE, fields.getFieldHeading());
                database.insert(MySQLiteHelper.TABLE_FORM_FIELD, null, formFieldValues);
                int formFieldId = getLastId(MySQLiteHelper.TABLE_FORM_FIELD, MySQLiteHelper.FORM_FIELD_ID);
                List<String> options = fields.getFieldOptions();
                for (int i = 0; i < options.size(); i++) {
                    //Insert the form options
                    ContentValues optionsValues = new ContentValues();
                    optionsValues.put(MySQLiteHelper.FIELD_OPTIONS_FIELD_FK, fields.getFieldId());
                    optionsValues.put(MySQLiteHelper.FIELD_OPTIONS_FORM_FIELD_ID_FK, formFieldId);
                    optionsValues.put(MySQLiteHelper.FIELD_OPTIONS_NAME, options.get(i));
                    database.insert(MySQLiteHelper.TABLE_FIELD_OPTIONS, null, optionsValues);

                }
            }
            Log.i("Last id form", "--" + formId);

            if (database != null && database.isOpen()) {
                this.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    select type,title,status,field_id,FORM_FIELD.id
//    from FORM_FIELD
//    INNER JOIN FIELD ON FORM_FIELD.field_id = FIELD.id AND form_id = 1

    public Form getFormDetail(Form form) {
        if (database == null || !database.isOpen()) {
            this.open();
        }
        ArrayList<Fields> fields = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FORM_FIELD + " INNER JOIN FIELD ON FORM_FIELD.field_id = FIELD.id AND form_id = " + form.getFormId(), new String[]{MySQLiteHelper.FIELD_ID_FK, MySQLiteHelper.FORM_FIELD_STATUS, MySQLiteHelper.FORM_FIELD_TITLE, "FORM_FIELD.id"},
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Fields field = new Fields();
            field.setFieldHeading(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FORM_FIELD_TITLE)));
            field.setStatus(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.FORM_FIELD_STATUS)));
            field.setFieldId(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.FIELD_ID_FK)));
            field.setFormFieldId(cursor.getInt(cursor.getColumnIndex("id")));


            if (field.getFieldId() == FieldType.Type.SELECT.toInteger() || field.getFieldId() == FieldType.Type.RADIO.toInteger() ||
                    field.getFieldId() == FieldType.Type.CHECKBOX.toInteger()) {

                ArrayList<String> options = new ArrayList<>();
                Cursor innerCursor = database.query(MySQLiteHelper.TABLE_FIELD_OPTIONS, new String[]{MySQLiteHelper.FIELD_OPTIONS_NAME},
                        MySQLiteHelper.FIELD_OPTIONS_FORM_FIELD_ID_FK + " = " + cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.FORM_FIELD_ID)), null, null, null, null);
                innerCursor.moveToFirst();
                while (!innerCursor.isAfterLast()) {
                    options.add(innerCursor.getString(innerCursor.getColumnIndex(MySQLiteHelper.FIELD_OPTIONS_NAME)));
                    innerCursor.moveToNext();
                }
                innerCursor.close();
                field.setFieldOptions(options);
            }

            fields.add(field);
            cursor.moveToNext();
        }
        form.setFieldsList(fields);
        // Make sure to close the cursor
        cursor.close();

        if (database != null && database.isOpen()) {
            this.close();
        }
        return form;
    }

    private Form cursorToPunch(Cursor cursor) {
        Form pObj = new Form();
        pObj.setFormId(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.FORM_ID)));
        pObj.setTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FORM_TITLE)));
        pObj.setDescription(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FORM_DESCRIPTION)));
        pObj.setDate(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.FORM_DATE)));
        return pObj;
    }

    public void AddFeedback(int formId, boolean update, String suggestion, String name, String contact, String email, String dob) {
        if (database == null || !database.isOpen()) {
            this.open();
        }

        //Insert the feedback
        ContentValues values = new ContentValues();
        if (update) {
            values.put(MySQLiteHelper.FEEDBACK_SUGGESTIONS, suggestion);
            values.put(MySQLiteHelper.FEEDBACK_NAME, name);
            values.put(MySQLiteHelper.FEEDBACK_CONTACT, contact);
            values.put(MySQLiteHelper.FEEDBACK_EMAIL, email);
            values.put(MySQLiteHelper.FEEDBACK_DOB, dob);
            int feedbackId = getLastId(MySQLiteHelper.TABLE_FEEDBACK, MySQLiteHelper.FEEDBACK_ID);
            database.update(MySQLiteHelper.TABLE_FEEDBACK, values, MySQLiteHelper.FEEDBACK_ID + "=?", new String[]{String.valueOf(feedbackId)});
        } else {
            values.put(MySQLiteHelper.FEEDBACK_FORM_ID_FK, formId);
            values.put(MySQLiteHelper.FEEDBACK_DATE, System.currentTimeMillis());
            database.insert(MySQLiteHelper.TABLE_FEEDBACK, null, values);
        }
        if (database != null && database.isOpen()) {
            this.close();
        }
    }

    public void AddFeedbackResponse(int fieldId, int formFieldId) {
        if (database == null || !database.isOpen()) {
            this.open();
        }
        int feedbackId = getLastId(MySQLiteHelper.TABLE_FEEDBACK, MySQLiteHelper.FEEDBACK_ID);
        //Insert the feedback
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.FIELD_RESPONSE_FIELD_ID_FK, fieldId);
        values.put(MySQLiteHelper.FIELD_RESPONSE_FEEDBACK_ID_FK, feedbackId);
        values.put(MySQLiteHelper.FIELD_RESPONSE_FORM_FIELD_ID_FK, formFieldId);
        values.put(MySQLiteHelper.FIELD_RESPONSE_DATE, System.currentTimeMillis());
        database.insert(MySQLiteHelper.TABLE_FIELD_RESPONSE, null, values);
        if (database != null && database.isOpen()) {
            this.close();
        }
    }

    public void AddFeedbackResponseValue(String value) {
        if (database == null || !database.isOpen()) {
            this.open();
        }
        int fieldResponseId = getLastId(MySQLiteHelper.TABLE_FIELD_RESPONSE, MySQLiteHelper.FIELD_RESPONSE_ID);

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.FIELD_RESPONSE_ID_FK, fieldResponseId);
        values.put(MySQLiteHelper.FIELD_RESPONSE_VALUES, value);
        database.insert(MySQLiteHelper.TABLE_FIELD_RESPONSE_OPTIONS, null, values);
        if (database != null && database.isOpen()) {
            this.close();
        }
    }


//    select FIELD_RESPONSE_OPTION.value ,FIELD_RESPONSE.field_id
//    from FIELD_RESPONSE
//    INNER JOIN FIELD_RESPONSE_OPTION ON FIELD_RESPONSE.id = FIELD_RESPONSE_OPTION.field_response_id AND feedback_id = 2


//    select FIELD_RESPONSE_OPTION.value ,FIELD_RESPONSE.field_id, FORM_FIELD.title
//    from FIELD_RESPONSE
//    JOIN FIELD_RESPONSE_OPTION ON FIELD_RESPONSE.id = FIELD_RESPONSE_OPTION.field_response_id AND feedback_id = 2
//    JOIN FORM_FIELD ON FORM_FIELD.id = FIELD_RESPONSE.form_field_id

    public ArrayList<Feedback> GetFeedback(int formId) {
        ArrayList<Feedback> mList = new ArrayList<>();
        if (database == null || !database.isOpen()) {
            this.open();
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FEEDBACK, columns_table_feedback, MySQLiteHelper.FEEDBACK_FORM_ID_FK + " = " + formId, null, null, null, MySQLiteHelper.FORM_DATE + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Feedback pObj = new Feedback();
            pObj.setFeedbackId(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.FEEDBACK_ID)));
            pObj.setDate(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.FEEDBACK_DATE)));

            pObj.setSuggestion(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FEEDBACK_SUGGESTIONS)));
            pObj.setName(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FEEDBACK_NAME)));
            pObj.setContact(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FEEDBACK_CONTACT)));
            pObj.setEmail(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FEEDBACK_EMAIL)));
            pObj.setDob(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FEEDBACK_DOB)));
            mList.add(pObj);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        if (database != null && database.isOpen()) {
            this.close();
        }
        return mList;
    }

    public Feedback GetFeedbackDetail(Feedback feedback) {
        ArrayList<FeedbackResponse> responseList = new ArrayList<>();
        if (database == null || !database.isOpen()) {
            this.open();
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FIELD_RESPONSE + " JOIN FIELD_RESPONSE_OPTION ON FIELD_RESPONSE.id = FIELD_RESPONSE_OPTION.field_response_id AND feedback_id = " + feedback.getFeedbackId() +
                        " JOIN FORM_FIELD ON FORM_FIELD.id = FIELD_RESPONSE.form_field_id",
                new String[]{MySQLiteHelper.FIELD_RESPONSE_VALUES, "FIELD_RESPONSE.field_id", MySQLiteHelper.FORM_FIELD_TITLE}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FeedbackResponse response = new FeedbackResponse();
            response.setFieldId(cursor.getColumnIndex(MySQLiteHelper.FIELD_RESPONSE_FIELD_ID_FK));
            response.setValue(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FIELD_RESPONSE_VALUES)));
            response.setFieldHeading(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FORM_FIELD_TITLE)));
            cursor.moveToNext();
            responseList.add(response);
        }
        // Make sure to close the cursor
        cursor.close();
        if (database != null && database.isOpen()) {
            this.close();
        }
        feedback.setResponseList(responseList);
        return feedback;
    }
}
