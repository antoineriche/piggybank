package com.gaminho.piggybank.view.dialog.adding;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gaminho.piggybank.R;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public abstract class AddingDialog<T extends RealmObject> {

    private final Context mContext;
    final AddingDialogListener<T> mListener;

    protected abstract boolean canAdd();

    AddingDialog(Context context, AddingDialogListener<T> listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    protected abstract int getLayoutResId();

    protected abstract void initView(final View view);

    protected abstract int getTitleStringResId();

    private AlertDialog build() {
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View view = factory.inflate(getLayoutResId(), null);
        initView(view);

        return new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle(getTitleStringResId())
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public void show() {
        final AlertDialog alertDialog = build();
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (canAdd()) {
                addItem(alertDialog);
            } else {
                mListener.onAddingItemFailed(mContext.getResources().getString(R.string.missing_info));
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.save);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText(R.string.cancel);
    }


    void addItem(final AlertDialog alertDialog){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> {
                try {
                    final T item = getItemFromViews();
                    realm1.copyToRealm(item);
                    mListener.onItemAdded(item);
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

    protected abstract T getItemFromViews();

    public interface AddingDialogListener<T extends RealmObject> {
        void onItemAdded(T item);

        void onAddingItemFailed(final String errorMsg);
    }
}
