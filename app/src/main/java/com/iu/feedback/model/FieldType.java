package com.iu.feedback.model;

/**
 * Created by sikanderkhan on 4/25/18.
 */

public class FieldType {
    String type;
    int id;

    public FieldType(String _type, int _id) {
        this.type = _type;
        this.id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return type;
    }

    public enum Type {
        CHECKBOX("CheckBox", 0),
        DATE("Date", 1),
        RADIO("Radio", 2),
        RATING("Rating", 3),
        SELECT("Select", 4),
        TEXTFIELD("TextField", 5),
        TEXTAREA("TextArea", 6);

        String stringValue;
        int intValue;

        Type(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int toInteger() {
            return intValue;
        }

    }
}
