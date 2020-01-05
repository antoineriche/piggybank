package com.gaminho.piggybank.adapter;

import android.content.Context;
import android.graphics.Color;
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

public class RVInterestAdapter extends RecyclerView.Adapter<RVInterestAdapter.ViewHolder> {

    private List<Interest> mInterestList;
    private static OnInterestClickListener clickListener;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView interestAmount, interestDate, accountAmount, interestDelta;

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

    public RVInterestAdapter(Context context, List<Interest> interestList) {
        this.mInterestList = interestList;
        this.mContext = context;
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
        vh.interestDelta = view.findViewById(R.id.cell_interest_delta);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVInterestAdapter.ViewHolder viewHolder, int position) {
        viewHolder.interestAmount.setText(
                Formatter.formatAmount(mInterestList.get(position).getAmount()));
        final Date date = mInterestList.get(position).getDate();
        final String strDate = null != date ?
                Formatter.dateToString(date, Format.DATE_DD_MM_YYYY)
                : "Last update: unknown";
        viewHolder.interestDate.setText(strDate);
        viewHolder.accountAmount.setText(
                Formatter.formatAmount(mInterestList.get(position).getAccountAmount()));
        final double delta = getDelta(position);
        if(delta > 0) {
            viewHolder.interestDelta.setText(String.format("+ %s", Formatter.formatDouble(delta)));
            viewHolder.interestDelta.setTextColor(mContext.getColor(R.color.green500));
        } else if (delta < 0){
            viewHolder.interestDelta.setText(String.format("- %s", Formatter.formatDouble(-delta)));
            viewHolder.interestDelta.setTextColor(mContext.getColor(R.color.red500));
        } else {
            viewHolder.interestDelta.setText(Formatter.formatDouble(0));
        }
    }

    @Override
    public int getItemCount() {
        return mInterestList.size();
    }

    public void setOnInterestClickListener(final OnInterestClickListener clickListener) {
        RVInterestAdapter.clickListener = clickListener;
    }

    public Interest getInterestAtPosition(final int position) {
        return mInterestList.get(position);
    }

    private double getDelta(final int position) {
        if (mInterestList.size() != position + 1) {
            return mInterestList.get(position).getAmount() -
                    mInterestList.get(position + 1).getAmount();
        } else {
            return 0d;
        }
    }

    public interface OnInterestClickListener {
        void onInterestClick(int position, View v);

        void onInterestLongClick(int position, View v);
    }

}
