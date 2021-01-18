package com.gaminho.piggybank.view.dialog.viewing;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gaminho.piggybank.R;

import io.realm.RealmObject;

/**
 * Created by Bonnie on 2021-01-18
 */
public final class AccountAsJSONDialog<T extends RealmObject> {

    private final Context mContext;
    private final T mItem;
    private final SaveListener mListener;

    private TextView mTVAccountContent;

    public AccountAsJSONDialog(Context context, T realmItem, AccountAsJSONDialog.SaveListener listener) {
        this.mContext = context;
        this.mItem = realmItem;
        this.mListener = listener;
    }

    private AlertDialog build() {
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View view = factory.inflate(R.layout.dialog_view_account, null);
        initView(view);

        mTVAccountContent.setText(mItem.toString());

        return new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle(R.string.account_to_json)
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public void show() {
        final AlertDialog alertDialog = build();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> mListener.onSaveItem());
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.save);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText(R.string.cancel);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            mListener.onCancel();
            alertDialog.dismiss();
        });
    }

    private void initView(View view) {
        mTVAccountContent = view.findViewById(R.id.tv_account_content);
    }

    public T getItem() {
        return mItem;
    }

    public interface SaveListener {
        void onSaveItem();

        void onCancel();
    }
}