package com.gaminho.piggybank.view.dialog.deleting;

import android.app.AlertDialog;
import android.content.Context;

import com.gaminho.piggybank.R;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public abstract class DeletingDialog<T extends RealmObject> {

    private final Context mContext;
    final DeletingDialogListener mListener;
    final T mItem;

    DeletingDialog(T item, Context context, DeletingDialogListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mItem = item;
    }

    protected abstract int getMessageStringResId();

    private AlertDialog build() {
        return new AlertDialog.Builder(mContext)
                .setMessage(getMessageStringResId())
                .setTitle(R.string.delete)
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public void show() {
        final AlertDialog alertDialog = build();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> deleteItem(alertDialog));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.delete);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText(R.string.cancel);
    }


    void deleteItem(final AlertDialog alertDialog){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> {
                try {
                    realm1.where(mItem.getClass()).equalTo(getKey(), getUid())
                            .findAll().deleteAllFromRealm();
                    mListener.onItemDeleted();
                } catch (RealmPrimaryKeyConstraintException e) {
                    mListener.onAddingItemFailed(e.getLocalizedMessage());
                }
            });
        } finally {
            alertDialog.dismiss();
        }
    }

    abstract String getKey();
    abstract String getUid();

    public interface DeletingDialogListener {
        void onItemDeleted();

        void onAddingItemFailed(final String errorMsg);
    }
}
