package com.iu.feedback.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sikanderkhan on 4/25/18.
 */

public class Form implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Form> CREATOR = new Parcelable.Creator<Form>() {
        @Override
        public Form createFromParcel(Parcel in) {
            return new Form(in);
        }

        @Override
        public Form[] newArray(int size) {
            return new Form[size];
        }
    };
    ArrayList<Fields> fieldsList;
    //form
    private int formId;
    private String title;
    private String description;
    private long date;

    public Form() {
        super();
    }

    protected Form(Parcel in) {
        super();
        formId = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readLong();

    }

    public ArrayList<Fields> getFieldsList() {
        return fieldsList;
    }

    public void setFieldsList(ArrayList<Fields> fieldsList) {
        this.fieldsList = fieldsList;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(formId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(date);

    }
}