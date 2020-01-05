package com.gaminho.piggybank.view.dialog.deleting;

import android.app.AlertDialog;
import android.content.Context;

import com.gaminho.piggybank.R;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public abstract class DeletingDialog<T extends RealmObject> {

    private Context mContext;
    DeletingDialogListener mListener;
    T mItem;

    DeletingDialog(T item, Context context, DeletingDialogListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mItem = item;
    }

    protected abstract int getTitleStringResId();
    protected abstract int getMessageStringResId();

    private AlertDialog build() {
        return new AlertDialog.Builder(mContext)
                .setMessage(getMessageStringResId())
                .setTitle(getTitleStringResId())
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


    protected void deleteItem(final AlertDialog alertDialog){
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

    Context getContext() {
        return mContext;
    }

    abstract String getKey();
    abstract String getUid();

    public interface DeletingDialogListener {
        void onItemDeleted();

        void onAddingItemFailed(final String errorMsg);
    }
}
