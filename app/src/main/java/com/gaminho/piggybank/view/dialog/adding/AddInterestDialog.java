package com.gaminho.piggybank.view.dialog.adding;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.model.Interest;
import com.gaminho.piggybank.util.DateUtils;
import com.gaminho.piggybank.util.Format;
import com.gaminho.piggybank.util.Formatter;
import com.gaminho.piggybank.util.ViewUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public final class AddInterestDialog extends AddingDialog<Interest> {

    private EditText mETInterestAmount, mETInterestAccountAmount, mETInterestDate;
    private LocalDate mLocalDate;
    private final Account mAccount;

    public AddInterestDialog(Context context, Account account, AddingDialogListener<Interest> listener) {
        super(context, listener);
        this.mAccount = account;
        this.mLocalDate = null;
    }

    @Override
    protected boolean canAdd() {
        return ViewUtils.isEditTextFilled(mETInterestAmount)
                && ViewUtils.isEditTextFilled(mETInterestAccountAmount)
                && null != mLocalDate;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_add_interest;
    }

    @Override
    protected void initView(View view) {
        mETInterestAmount = view.findViewById(R.id.et_interest_amount);
        mETInterestDate = view.findViewById(R.id.et_interest_date);

        view.findViewById(R.id.btn_interest_date).setOnClickListener(v ->
                pickDate(Calendar.getInstance(Locale.FRANCE)));
        mETInterestAccountAmount = view.findViewById(R.id.et_interest_new_account_amount);
    }

    @Override
    protected int getTitleStringResId() {
        return R.string.new_interest;
    }

    @Override
    protected Interest getItemFromViews() {
        final Interest interest = new Interest();
        interest.setAmount(ViewUtils.readDoubleFromEditText(mETInterestAmount));
        interest.setAccount(mAccount);
        interest.setUid(UUID.randomUUID().toString());
        interest.setAccountAmount(ViewUtils.readDoubleFromEditText(mETInterestAccountAmount));
        interest.setDate(mLocalDate.atStartOfDay());
        return interest;
    }

    @Override
    protected void addItem(AlertDialog alertDialog) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> {
                try {
                    mAccount.setAmount(ViewUtils.readDoubleFromEditText(mETInterestAccountAmount));
                    mAccount.setLastUpdate(DateUtils.toDate(LocalDateTime.now()));
                    realm1.copyToRealmOrUpdate(mAccount);
                } catch (RealmPrimaryKeyConstraintException e) {
                    mListener.onAddingItemFailed(e.getLocalizedMessage());
                }
            });
        }

        super.addItem(alertDialog);
    }

    private void pickDate(final Calendar calendar) {
        new DatePickerDialog(super.getContext(),
                (datePicker, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar1 = Calendar.getInstance(Locale.FRANCE);
                    calendar1.set(Calendar.YEAR, year);
                    calendar1.set(Calendar.MONTH, monthOfYear);
                    calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mLocalDate =  DateUtils.toLocalDate(calendar1.getTime());
                    mETInterestDate.setText(Formatter.dateToString(mLocalDate, Format.DATE_DD_MM_YYYY));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

}
