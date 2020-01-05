package com.gaminho.piggybank.view.dialog.deleting;

import android.app.AlertDialog;
import android.content.Context;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.model.Interest;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public final class DeleteAccountDialog extends DeletingDialog<Account> {

    public DeleteAccountDialog(Account item, Context context, DeletingDialogListener listener) {
        super(item, context, listener);
    }

    @Override
    protected int getMessageStringResId() {
        return R.string.delete_account;
    }

    @Override
    String getKey() {
        return Account.PROPERTY_UID;
    }

    @Override
    String getUid() {
        return mItem.getUid();
    }

    @Override
    protected void deleteItem(AlertDialog alertDialog) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> {
                try {
                    realm1.where(Interest.class).equalTo("mAccount.mUid", mItem.getUid())
                            .findAll().deleteAllFromRealm();
                } catch (RealmPrimaryKeyConstraintException e) {
                    mListener.onAddingItemFailed(e.getLocalizedMessage());
                }
            });
        }
        super.deleteItem(alertDialog);
    }
}
