package com.iu.feedback.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sikanderkhan on 4/25/18.
 */

public class Fields implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Fields> CREATOR = new Parcelable.Creator<Fields>() {
        @Override
        public Fields createFromParcel(Parcel in) {
            return new Fields(in);
        }

        @Override
        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };
    private int fieldId;
    private int formFieldId;
    private String fieldHeading;
    private int status;
    private ArrayList<String> fieldOptions;

    public Fields() {
    }

    public Fields(String title, int fieldId, int status, ArrayList<String> fieldOptions) {
        this.setFieldHeading(title);
        this.setFieldId(fieldId);
        this.setStatus(status);
        this.setFieldOptions(fieldOptions);
    }

    protected Fields(Parcel in) {
        fieldId = in.readInt();
        formFieldId = in.readInt();
        fieldHeading = in.readString();
        status = in.readInt();
    }

    public ArrayList<String> getFieldOptions() {
        return fieldOptions;
    }

    public void setFieldOptions(ArrayList<String> fieldOptions) {
        this.fieldOptions = fieldOptions;
    }

    public int getFormFieldId() {
        return formFieldId;
    }

    public void setFormFieldId(int formFieldId) {
        this.formFieldId = formFieldId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFieldHeading() {
        return fieldHeading;
    }

    public void setFieldHeading(String title) {
        this.fieldHeading = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(formFieldId);
        dest.writeInt(fieldId);
        dest.writeString(fieldHeading);
        dest.writeInt(status);
    }
}