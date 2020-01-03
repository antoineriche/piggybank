package com.gaminho.piggybank.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.model.Interest;
import com.gaminho.piggybank.util.DateUtils;
import com.gaminho.piggybank.util.ViewUtils;

import java.time.LocalDateTime;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AddInterestDialog {

    private Context mContext;
    private Account mAccount;
    private AddInterestDialogListener mListener;

    private EditText mETInterestAmount, mETInterestAccountAmount;

    public AddInterestDialog(Context context, Account account, AddInterestDialogListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mAccount = account;
    }

    private AlertDialog build() {
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View view = factory.inflate(R.layout.dialog_add_interest, null);
        mETInterestAmount = view.findViewById(R.id.et_interest_amount);
        mETInterestAccountAmount = view.findViewById(R.id.et_interest_new_account_amount);

        return new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle("New interest")
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public void show() {
        AlertDialog alertDialog = build();
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (ViewUtils.isEditTextFilled(mETInterestAmount)
                    && ViewUtils.isEditTextFilled(mETInterestAccountAmount)) {
                addInterest(alertDialog);
            } else {
                mListener.onAddingInterestFailure("Some info are missing");
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Save");
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText("Cancel");
    }

    private void addInterest(final AlertDialog alertDialog) {

        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                try {
                    mAccount.setAmount(Double.parseDouble(mETInterestAccountAmount.getText().toString()));
                    mAccount.setLastUpdate(DateUtils.toDate(LocalDateTime.now()));
                    realm1.copyToRealmOrUpdate(mAccount);

                    final Interest interest = new Interest();
                    interest.setAmount(Double.parseDouble(mETInterestAmount.getText().toString()));
                    interest.setAccount(mAccount);
                    interest.setAccountAmount(Double.parseDouble(mETInterestAccountAmount.getText().toString()));
                    interest.setDate(LocalDateTime.now());
                    realm1.copyToRealm(interest);
                    mListener.onInterestAdded();
                } catch (RealmPrimaryKeyConstraintException e) {
                    mListener.onAddingInterestFailure(e.getLocalizedMessage());
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
            alertDialog.dismiss();
        }
    }

    public interface AddInterestDialogListener {
        void onInterestAdded();

        void onAddingInterestFailure(final String errorMsg);
    }

}
