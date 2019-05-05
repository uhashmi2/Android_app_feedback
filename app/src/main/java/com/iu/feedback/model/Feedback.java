package com.iu.feedback.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sikanderkhan on 4/25/18.
 */

public class Feedback implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Feedback> CREATOR = new Parcelable.Creator<Feedback>() {
        @Override
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }

        @Override
        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };
    ArrayList<FeedbackResponse> responseList;
    private int feedbackId;
    private long date;
    private String suggestion;
    private String name;
    private String contact;
    private String email;
    private String dob;

    public Feedback() {
        super();
    }

    protected Feedback(Parcel in) {
        super();
        feedbackId = in.readInt();
        suggestion = in.readString();
        name = in.readString();
        email = in.readString();
        contact = in.readString();
        dob = in.readString();
        date = in.readLong();
        if (in.readByte() == 0x01) {
            responseList = new ArrayList<FeedbackResponse>();
            in.readList(responseList, FeedbackResponse.class.getClassLoader());
        } else {
            responseList = null;
        }
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public ArrayList<FeedbackResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(ArrayList<FeedbackResponse> responseList) {
        this.responseList = responseList;
    }

    public String getDate() {
        Date date1 = new Date(date);
        DateFormat df = new SimpleDateFormat("E, MMM d, hh:mm a");
        return df.format(date1);
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(feedbackId);
        dest.writeString(suggestion);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(contact);
        dest.writeString(dob);
        dest.writeLong(date);
        if (responseList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(responseList);
        }
    }

}
