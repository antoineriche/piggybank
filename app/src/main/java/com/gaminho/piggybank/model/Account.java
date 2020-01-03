package com.gaminho.piggybank.model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Account extends RealmObject implements Serializable {

    public static final String PROPERTY_UID = "mUid";
    public static final String PROPERTY_LABEL = "mLabel";
    public static final String PROPERTY_AMOUNT = "mAmount";
    public static final String PROPERTY_LAST_UPDATE = "mLastUpdate";

    @PrimaryKey
    private String mUid;

    @Required
    private String mLabel;

    private double mAmount;

    private Date mLastUpdate;

    public Account() {
    }

    public Account(String uid, String label, double amount) {
        mUid = uid;
        mLabel = label;
        mAmount = amount;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public Date getLastUpdate() {
        return mLastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.mLastUpdate = lastUpdate;
    }
}
