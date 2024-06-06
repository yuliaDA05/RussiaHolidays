package com.mirea.kt.ribo.russiaholidays;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HolidaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HolidayData> currentMonthHolidays = new ArrayList<>();
    private List<HolidayData> nextMonthHolidays = new ArrayList<>();

    private static final int dividerSize = 1;
    private static final int itemTypeDivider = 0;
    private static final int itemTypeHoliday = 1;

    @Override
    public int getItemViewType(int position) {
        if (position < dividerSize) {
            return itemTypeDivider;
        } else if(position < dividerSize + currentMonthHolidays.size()) {
            return itemTypeHoliday;
        } else if (position < dividerSize + currentMonthHolidays.size() + dividerSize) {
            return itemTypeDivider;
        } else {
            return itemTypeHoliday;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == itemTypeHoliday) {
            View view = inflater.inflate(R.layout.item_holiday, parent, false);//false чтоб не добавлялась к родительскому VIewGroup
            return new HolidayViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_divider, parent, false);
            return new DividerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == itemTypeDivider) {
            ((DividerViewHolder) holder).bind(position >= dividerSize);
            return;
        }

        HolidayData item;
        if (position < dividerSize + currentMonthHolidays.size()) {
            int index = position - dividerSize;
            item = currentMonthHolidays.get(index);
        } else  {
            int index = position - dividerSize - currentMonthHolidays.size() - dividerSize;
            item = nextMonthHolidays.get(index);
        }
        ((HolidayViewHolder) holder).bind(item);
    }

    @Override
    public int getItemCount() {
        return dividerSize + currentMonthHolidays.size() + dividerSize + nextMonthHolidays.size();
    }

    public void setCurrentMonthHolidays(List<HolidayData> holidays) {
        int start = dividerSize;
        notifyItemRangeRemoved(start, currentMonthHolidays.size());
        currentMonthHolidays = holidays;
        notifyItemRangeInserted(start, currentMonthHolidays.size());
    }

    public void setNextMonthHolidays(List<HolidayData> holidays) {
        int start = dividerSize + currentMonthHolidays.size() + dividerSize;
        notifyItemRangeRemoved(start, nextMonthHolidays.size());
        nextMonthHolidays = holidays;
        notifyItemRangeInserted(start, nextMonthHolidays.size());
    }

    public static class DividerViewHolder extends RecyclerView.ViewHolder {

        TextView labelTextView;

        public DividerViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.labelTextView);
        }

        void bind(Boolean isForNextMonth) {
            if (isForNextMonth) {
                labelTextView.setText(R.string.divider_next_month);
            } else {
                labelTextView.setText(R.string.divider_current_month);
            }
        }
    }

    public static class HolidayViewHolder extends RecyclerView.ViewHolder {

        View root;

        TextView nameTextView;
        TextView dataTextView;
        TextView descriptionTextView;
        TextView typeTextView;

        public HolidayViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
        }

        void bind(HolidayData data) {
            nameTextView.setText(data.name);
            dataTextView.setText(String.valueOf(data.day));
            descriptionTextView.setText(data.description);
            typeTextView.setText(String.join(", ", data.type));
            root.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = v.getResources().getString(
                        R.string.holiday_share_format,
                        data.day, data.month, data.name, data.description
                );
                intent.putExtra(Intent.EXTRA_TEXT, text);

                v.getContext().startActivity(Intent.createChooser(intent, null));
            });
        }
    }
}
