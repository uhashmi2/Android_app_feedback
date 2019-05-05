package com.iu.feedback.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FeedbackResponse implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FeedbackResponse> CREATOR = new Parcelable.Creator<FeedbackResponse>() {
        @Override
        public FeedbackResponse createFromParcel(Parcel in) {
            return new FeedbackResponse(in);
        }

        @Override
        public FeedbackResponse[] newArray(int size) {
            return new FeedbackResponse[size];
        }
    };
    int fieldId;
    String value;
    String fieldHeading;

    public FeedbackResponse() {
        super();
    }

    protected FeedbackResponse(Parcel in) {
        super();
        fieldId = in.readInt();
        value = in.readString();
        fieldHeading = in.readString();
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldHeading() {
        return fieldHeading;
    }

    public void setFieldHeading(String fieldHeading) {
        this.fieldHeading = fieldHeading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fieldId);
        dest.writeString(value);
        dest.writeString(fieldHeading);
    }


}
