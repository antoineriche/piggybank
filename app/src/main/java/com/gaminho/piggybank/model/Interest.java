package com.gaminho.piggybank.model;

import com.gaminho.piggybank.util.DateUtils;

import java.time.LocalDateTime;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Interest extends RealmObject {

    public static final String PROPERTY_UID = "mUid";
    public static final String PROPERTY_AMOUNT = "mAmount";
    public static final String PROPERTY_ACCOUNT_AMOUNT = "mAccountAmount";
    public static final String PROPERTY_ACCOUNT = "mAccount";
    public static final String PROPERTY_DATE = "mDate";

    @PrimaryKey
    @Index
    private String mUid;

    private double mAmount;

    private double mAccountAmount;

    private Account mAccount;

    @Required
    @Index
    private Date mDate;

    public Interest() {
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(Account account) {
        mAccount = account;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setDate(LocalDateTime date) {
        mDate = DateUtils.toDate(date);
    }

    public double getAccountAmount() {
        return mAccountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        mAccountAmount = accountAmount;
    }
}
