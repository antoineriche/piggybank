package com.gaminho.piggybank.view.dialog.deleting;

import android.content.Context;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Interest;

public final class DeleteInterestDialog extends DeletingDialog<Interest> {

    public DeleteInterestDialog(Interest item, Context context, DeletingDialogListener listener) {
        super(item, context, listener);
    }

    @Override
    protected int getMessageStringResId() {
        return R.string.delete_interest;
    }

    @Override
    String getKey() {
        return Interest.PROPERTY_UID;
    }

    @Override
    String getUid() {
        return mItem.getUid();
    }
}
