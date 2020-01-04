package com.gaminho.piggybank.view.dialog.adding;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.util.ViewUtils;

import java.util.UUID;

public class AddAccountDialog extends AddingDialog<Account> {

    private EditText mETAccountLabel, mETAccountAmount;

    public AddAccountDialog(Context context, AddingDialogListener listener) {
        super(context, listener);
    }

    @Override
    protected boolean canAdd() {
        return ViewUtils.isEditTextFilled(mETAccountAmount)
                && ViewUtils.isEditTextFilled(mETAccountLabel);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_add_account;
    }

    @Override
    protected void initView(View view) {
        mETAccountLabel = view.findViewById(R.id.et_account_label);
        mETAccountAmount = view.findViewById(R.id.et_account_amount);
    }

    @Override
    protected int getTitleStringResId() {
        return R.string.new_account;
    }

    @Override
    protected Account getItemFromViews() {
        final Account account = new Account();
        account.setAmount(ViewUtils.readDoubleFromEditText(mETAccountAmount));
        account.setLabel(mETAccountLabel.getText().toString());
        account.setUid(UUID.randomUUID().toString());
        return account;
    }
}
