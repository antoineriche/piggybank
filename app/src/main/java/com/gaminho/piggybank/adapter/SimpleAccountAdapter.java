package com.gaminho.piggybank.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gaminho.piggybank.model.Account;

import java.util.List;

public class SimpleAccountAdapter extends ArrayAdapter<Account> {

    public SimpleAccountAdapter(Context context, List<Account> accounts) {
        super(context, android.R.layout.simple_spinner_item, accounts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Account account = getItem(position);

        TextView tv = convertView.findViewById(android.R.id.text1);
        tv.setText(account.getLabel());

        return convertView;
    }

}
