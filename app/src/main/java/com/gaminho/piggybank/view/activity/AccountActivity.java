package com.gaminho.piggybank.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.adapter.RVInterestAdapter;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.model.Interest;
import com.gaminho.piggybank.view.dialog.adding.AddInterestDialog;
import com.gaminho.piggybank.view.dialog.adding.AddingDialog;
import com.gaminho.piggybank.view.dialog.deleting.DeleteInterestDialog;
import com.gaminho.piggybank.view.dialog.deleting.DeletingDialog;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener,
        RVInterestAdapter.OnInterestClickListener, AddingDialog.AddingDialogListener<Interest>, DeletingDialog.DeletingDialogListener {

    public static final String INTENT_ACCOUNT_UID_PARAM = "account-uid";
    private Account mAccount;
    private List<Interest> mInterestList;

    private RVInterestAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Realm realm = Realm.getDefaultInstance();
        getAccount(getIntent(), realm);

        findViewById(R.id.btn_new_interest).setOnClickListener(this);

        mInterestList = realm.where(Interest.class).equalTo("mAccount.mUid", mAccount.getUid())
                .findAllSorted(Interest.PROPERTY_DATE, Sort.DESCENDING);

        final RecyclerView recyclerView = findViewById(R.id.rv_interests);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVInterestAdapter(mInterestList);
        mAdapter.setOnInterestClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    private void getAccount(final Intent intent, final Realm realm) {
        if (null != intent && null != intent.getStringExtra(INTENT_ACCOUNT_UID_PARAM)) {
            final String uid = intent.getStringExtra(INTENT_ACCOUNT_UID_PARAM);
            realm.executeTransaction(realm1 -> {
                mAccount = realm1.where(Account.class).equalTo(Account.PROPERTY_UID, uid).findFirst();
                if (mAccount == null) {
                    Toast.makeText(getApplicationContext(), R.string.unknown_account, Toast.LENGTH_SHORT).show();
                    onDestroy();
                } else {
                    setTitle(mAccount.getLabel());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.unknown_account, Toast.LENGTH_SHORT).show();
            onDestroy();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_interest) {
            new AddInterestDialog(this, mAccount, this).show();
        }
    }

    @Override
    public void onInterestClick(int position, View v) {
        new DeleteInterestDialog(mInterestList.get(position), this, this).show();
    }

    @Override
    public void onInterestLongClick(int position, View v) {

    }

    @Override
    public void onItemAdded(Interest item) {
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDeleted() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddingItemFailed(String errorMsg) {
        Toast.makeText(getApplicationContext(), "Error:\n" + errorMsg, Toast.LENGTH_SHORT).show();
    }
}
