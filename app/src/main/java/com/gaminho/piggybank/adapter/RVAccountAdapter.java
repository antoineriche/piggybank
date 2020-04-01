package com.gaminho.piggybank.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Account;
import com.gaminho.piggybank.util.Format;
import com.gaminho.piggybank.util.Formatter;

import java.util.Date;
import java.util.List;

public class RVAccountAdapter extends RecyclerView.Adapter<RVAccountAdapter.ViewHolder> {

    private final List<Account> mAccountList;
    private static OnAccountClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView accountLabel, accountLastUpdate, accountAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onAccountClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onAccountLongClick(getAdapterPosition(), view);
            return false;
        }
    }

    public RVAccountAdapter(List<Account> accountList) {
        this.mAccountList = accountList;
    }

    @NonNull
    @Override
    public RVAccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_account_adapter, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);
        vh.accountLabel = view.findViewById(R.id.cell_account_label);
        vh.accountLastUpdate = view.findViewById(R.id.cell_account_last_update);
        vh.accountAmount = view.findViewById(R.id.cell_account_amount);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAccountAdapter.ViewHolder viewHolder, int position) {
        viewHolder.accountLabel.setText(mAccountList.get(position).getLabel());
        final Date date = mAccountList.get(position).getLastUpdate();
        final String strDate = null != date ?
                Formatter.dateToString(date, Format.DATE_YYYY_MM_DD)
                : "Last update: unknown";
        viewHolder.accountLastUpdate.setText(strDate);
        viewHolder.accountAmount.setText(
                Formatter.formatAmount(mAccountList.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return mAccountList.size();
    }

    public void setOnAccountClickListener(OnAccountClickListener clickListener) {
        RVAccountAdapter.clickListener = clickListener;
    }

    public Account getAccountAtPosition(int position){
        return mAccountList.get(position);
    }

    public interface OnAccountClickListener {
        void onAccountClick(int position, View v);
        void onAccountLongClick(int position, View v);
    }
}
