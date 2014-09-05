/*
 * Copyright (c) 2014 NetMoms GmbH. All rights reserved.
 */

package eu.rekisoft.android.recyclerviewbug;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> implements View.OnClickListener {
    public static final int VISIBLE_ITEMS = 7;
    public static final int NON_VISIBLE_ITEMS = 150;
    private static final int TOTAL_ITEMS = VISIBLE_ITEMS + NON_VISIBLE_ITEMS * 2;
    public static final int ITEM_IN_CENTER = (int)Math.ceil(VISIBLE_ITEMS / 2f) + NON_VISIBLE_ITEMS;

    private Calendar mCalendar;

    public MyAdapter() {
        mCalendar = GregorianCalendar.getInstance();
        setHasStableIds(true);
    }

    private int getToday() {
        return (int)TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return TOTAL_ITEMS;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        final TextView tv = new TextView(parent.getContext());
        int width = parent.getWidth() / VISIBLE_ITEMS;
        tv.setLayoutParams(new TableRow.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.TRANSPARENT);
        DisplayMetrics metrics = tv.getContext().getResources().getDisplayMetrics();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        tv.setLineSpacing(padding, 1f);
        tv.setPadding(0, (int)padding, 0, 0);
        tv.setOnClickListener(this);
        return new Holder(tv);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int today = getToday();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.HOUR_OF_DAY, 12); // set to noon to avoid energy saver time problems
        mCalendar.add(Calendar.DAY_OF_YEAR, position - ITEM_IN_CENTER + 1);
        DateFormat format = new SimpleDateFormat("E\nd");
        String label = format.format(mCalendar.getTime()).replace(".\n", "\n");
        int day = (int)TimeUnit.MILLISECONDS.toDays(mCalendar.getTimeInMillis());
        holder.update(day, today, label);
    }

    @Override
    public long getItemId(int position) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.HOUR_OF_DAY, 12); // set to noon to avoid energy saver time problems
        mCalendar.add(Calendar.DAY_OF_YEAR, position - ITEM_IN_CENTER + 1);
        DateFormat format = new SimpleDateFormat("dMMyyyy");
        return Long.parseLong(format.format(mCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        String day = ((TextView)v).getText().toString().replace("\n", " ");
        Toast.makeText(v.getContext(), "You clicked on " + day, Toast.LENGTH_SHORT).show();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private final Typeface font;

        private Holder(TextView v) {
            super(v);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                font = Typeface.create("sans-serif-light", Typeface.NORMAL);
            } else {
                font = null;
            }
        }

        public void update(int day, int today, String label) {
            TextView tv = (TextView)itemView;
            tv.setText(label);

            if(day == today) {
                tv.setTextSize(18);
                tv.setTypeface(null, Typeface.BOLD);
            } else {
                tv.setTextSize(16);
                tv.setTypeface(font, Typeface.NORMAL);
            }

            tv.setBackgroundColor(0xff8dc380);
        }
    }
}