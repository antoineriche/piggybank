package com.gaminho.piggybank.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.util.ViewUtils;

import java.util.UUID;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AddAccountDialog {

    private Context mContext;
    private AddAccountDialogListener mListener;

    private EditText mETAccountLabel, mETAccountAmount;

    public AddAccountDialog(Context context, AddAccountDialogListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    private AlertDialog build() {
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View view = factory.inflate(R.layout.dialog_add_account, null);
        mETAccountLabel = view.findViewById(R.id.et_account_label);
        mETAccountAmount = view.findViewById(R.id.et_account_amount);

        return new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle("New account")
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .create();
    }

    public void show() {
        AlertDialog alertDialog = build();
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (ViewUtils.isEditTextFilled(mETAccountAmount)
                    && ViewUtils.isEditTextFilled(mETAccountLabel)) {
                addAccount(alertDialog);
            } else {
                mListener.onAddingAccountFailure("Some info are missing");
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Save");
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText("Cancel");
    }

    private void addAccount(final AlertDialog alertDialog) {

        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                try {
                    final Account account = new Account();
                    account.setAmount(Double.parseDouble(mETAccountAmount.getText().toString()));
                    account.setLabel(mETAccountLabel.getText().toString());
                    account.setUid(UUID.randomUUID().toString());
                    realm1.copyToRealm(account);
                    mListener.onAccountAdded();
                } catch (RealmPrimaryKeyConstraintException e) {
                    mListener.onAddingAccountFailure(e.getLocalizedMessage());
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
            alertDialog.dismiss();
        }
    }

    public interface AddAccountDialogListener {
        void onAccountAdded();

        void onAddingAccountFailure(final String errorMsg);
    }

}
