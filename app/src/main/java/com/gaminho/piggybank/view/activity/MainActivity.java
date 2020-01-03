package com.gaminho.piggybank.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.adapter.RVAccountAdapter;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.view.dialog.AddAccountDialog;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AddAccountDialog.AddAccountDialogListener, RVAccountAdapter.OnAccountClickListener {

    private RVAccountAdapter mAdapter;

    private Realm mRealm;

    private List<Account> mAccountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_new_account).setOnClickListener(this);

        findViewById(R.id.btn_raz).setOnClickListener(this);

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
    public void onAccountAdded() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddingAccountFailure(final String errorMsg) {
        Toast.makeText(getApplicationContext(), "Error:\n" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccountClick(int position, View v) {
        final Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(AccountActivity.INTENT_ACCOUNT_UID_PARAM, mAdapter.getAccountAtPosition(position).getUid());
        startActivity(intent);
    }

    @Override
    public void onAccountLongClick(int position, View v) {

    }
}
