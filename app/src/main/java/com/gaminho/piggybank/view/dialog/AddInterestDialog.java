package com.gaminho.piggybank.view.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.model.Interest;
import com.gaminho.piggybank.util.DateUtils;
import com.gaminho.piggybank.util.ViewUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class AddInterestDialog {

    private Context mContext;
    private Account mAccount;
    private LocalDate mLocalDate = null;
    private AddInterestDialogListener mListener;

    private EditText mETInterestAmount, mETInterestAccountAmount, mETInterestDate;

    public AddInterestDialog(Context context, Account account, AddInterestDialogListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mAccount = account;
    }

    private AlertDialog build() {
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View view = factory.inflate(R.layout.dialog_add_interest, null);
        mETInterestAmount = view.findViewById(R.id.et_interest_amount);
        mETInterestDate = view.findViewById(R.id.et_interest_date);

        Locale.setDefault(Locale.FRANCE);
        view.findViewById(R.id.btn_interest_date).setOnClickListener(v -> pickDate(Calendar.getInstance(Locale.FRANCE)));
        mETInterestAccountAmount = view.findViewById(R.id.et_interest_new_account_amount);

        return new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle("New interest")
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    private void pickDate(final Calendar calendar) {
        new DatePickerDialog(mContext, (datePicker, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar1 = Calendar.getInstance(Locale.FRANCE);
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, monthOfYear);
            calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mLocalDate = DateUtils.toLocalDate(calendar1.getTime());
            mETInterestDate.setText(String.format(Locale.FRANCE,
                    "%02d/%02d/%04d", mLocalDate.getDayOfMonth(), mLocalDate.getMonthValue(),
                    mLocalDate.getYear()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void show() {
        AlertDialog alertDialog = build();
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (ViewUtils.isEditTextFilled(mETInterestAmount)
                    && ViewUtils.isEditTextFilled(mETInterestAccountAmount)
                    && null != mLocalDate) {
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
                    interest.setDate(mLocalDate.atStartOfDay());
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
