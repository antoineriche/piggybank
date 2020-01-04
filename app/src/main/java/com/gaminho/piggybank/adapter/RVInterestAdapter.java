package com.gaminho.piggybank.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.piggybank.R;
import com.gaminho.piggybank.model.Interest;
import com.gaminho.piggybank.util.Format;
import com.gaminho.piggybank.util.Formatter;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RVInterestAdapter extends RecyclerView.Adapter<RVInterestAdapter.ViewHolder> {

    private List<Interest> mInterestList;
    private static OnInterestClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView interestAmount, interestDate, accountAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onInterestClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onInterestLongClick(getAdapterPosition(), view);
            return false;
        }
    }

    public RVInterestAdapter(List<Interest> interestList) {
        this.mInterestList = interestList;
    }

    @NonNull
    @Override
    public RVInterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_interest_adapter, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);
        vh.interestAmount = view.findViewById(R.id.cell_interest_amount);
        vh.interestDate = view.findViewById(R.id.cell_interest_last_update);
        vh.accountAmount = view.findViewById(R.id.cell_interest_account_amount);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVInterestAdapter.ViewHolder viewHolder, int position) {
        viewHolder.interestAmount.setText(String.format(
                Locale.FRANCE, "%.02f€", mInterestList.get(position).getAmount()));
        final Date date = mInterestList.get(position).getDate();
        final String strDate = null != date ?
                Formatter.dateToString(date, Format.DATE_DD_MM_YYYY)
                : "Last update: unknown";
        viewHolder.interestDate.setText(strDate);
        viewHolder.accountAmount.setText(String.format(
                Locale.FRANCE, "%.02f€", mInterestList.get(position).getAccountAmount()));
    }

    @Override
    public int getItemCount() {
        return mInterestList.size();
    }

    public void setOnInterestClickListener(final OnInterestClickListener clickListener) {
        RVInterestAdapter.clickListener = clickListener;
    }

    public Interest getInterestAtPosition(final int position){
        return mInterestList.get(position);
    }

    public interface OnInterestClickListener {
        void onInterestClick(int position, View v);
        void onInterestLongClick(int position, View v);
    }
}
