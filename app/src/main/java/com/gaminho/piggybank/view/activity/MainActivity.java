package com.gaminho.piggybank.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.adapter.RVAccountAdapter;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.view.dialog.adding.AddAccountDialog;
import com.gaminho.piggybank.view.dialog.adding.AddingDialog;
import com.gaminho.piggybank.view.dialog.deleting.DeleteAccountDialog;
import com.gaminho.piggybank.view.dialog.deleting.DeletingDialog;
import com.gaminho.piggybank.view.dialog.viewing.AccountAsJSONDialog;

import java.util.List;
import java.util.stream.IntStream;

import io.realm.Realm;

/*
https://www.journaldev.com/23357/android-realm-database
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AddingDialog.AddingDialogListener<Account>,
        RVAccountAdapter.OnAccountClickListener,
        DeletingDialog.DeletingDialogListener,
        AccountAsJSONDialog.SaveListener {

    private RVAccountAdapter mAdapter;

    private Realm mRealm;

    private List<Account> mAccountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntStream.of(R.id.btn_new_account, R.id.btn_raz, R.id.btn_account_as_json)
                .mapToObj(this::findViewById)
                .forEach(v -> ((View) v).setOnClickListener(this));

        mRealm = Realm.getDefaultInstance();

        mAccountList = mRealm.where(Account.class).findAll();

        RecyclerView recyclerView = findViewById(R.id.rv_accounts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new RVAccountAdapter(mAccountList);
        mAdapter.setOnAccountClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRealm = Realm.getDefaultInstance();
        mAccountList = mRealm.where(Account.class).findAll();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_raz:
                deleteAll();
                break;
            case R.id.btn_new_account:
                new AddAccountDialog(this, this).show();
                break;
            case R.id.btn_account_as_json:
                if (!mAccountList.isEmpty()) {
                    new AccountAsJSONDialog<Account>(this, mAccountList.get(0), this).show();
                }
                break;
        }
    }

    private void deleteAll() {
        mRealm.executeTransaction(realm -> {
            realm.where(Account.class).findAll().deleteAllFromRealm();
            Toast.makeText(getApplicationContext(), "All account(s) have been deleted.",
                    Toast.LENGTH_SHORT).show();
        });
    }



//    private void updateEmployeeRecords() {
//
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
//
//                if (!inName.getText().toString().trim().isEmpty()) {
//
//
//                    Employee employee = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, inName.getText().toString()).findFirst();
//                    if (employee == null) {
//                        employee = realm.createObject(Employee.class, inName.getText().toString().trim());
//                    }
//                    if (!inAge.getText().toString().trim().isEmpty())
//                        employee.age = Integer.parseInt(inAge.getText().toString().trim());
//
//                    String languageKnown = inSkill.getText().toString().trim();
//                    Skill skill = realm.where(Skill.class).equalTo(Skill.PROPERTY_SKILL, languageKnown).findFirst();
//
//                    if (skill == null) {
//                        skill = realm.createObject(Skill.class, languageKnown);
//                        realm.copyToRealm(skill);
//                    }
//
//
//                    if (!employee.skills.contains(skill))
//                        employee.skills.add(skill);
//
//                }
//            }
//        });
//    }
//
//    private void deleteEmployeeRecord() {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                Employee employee = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, inName.getText().toString()).findFirst();
//                if (employee != null) {
//                    employee.deleteFromRealm();
//                }
//            }
//        });
//    }
//
//    private void deleteEmployeeWithSkill() {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
//                RealmResults<Employee> employees = realm.where(Employee.class).equalTo("skills.skillName", inSkill.getText().toString().trim()).findAll();
//                employees.deleteAllFromRealm();
//            }
//        });
//    }
//
//
//    private void filterByAge() {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
//                RealmResults<Employee> results = realm.where(Employee.class).greaterThanOrEqualTo(Employee.PROPERTY_AGE, 25).findAllSortedAsync(Employee.PROPERTY_NAME);
//
//                txtFilterByAge.setText("");
//                for (Employee employee : results) {
//                    txtFilterByAge.append(employee.name + " age: " + employee.age + " skill: " + employee.skills.size());
//                }
//            }
//        });
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    @Override
    public void onAccountClick(int position, View v) {
        final Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(AccountActivity.INTENT_ACCOUNT_UID_PARAM, mAdapter.getAccountAtPosition(position).getUid());
        startActivity(intent);
    }

    @Override
    public void onAccountLongClick(int position, View v) {
        new DeleteAccountDialog(mAccountList.get(position), this, this).show();
    }

    @Override
    public void onItemAdded(Account item) {
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDeleted() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddingItemFailed(String errorMsg) {
        Toast.makeText(getApplicationContext(), "Error:\n" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveItem() {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Log.e("Cancelling account", "big down");
    }
}
