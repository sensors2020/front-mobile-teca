package com.device.aquafox.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.device.aquafox.R;

public class ServicesAdapter extends BaseAdapter {
    Context context;
    String countryList[];
    int flags[];
    LayoutInflater inflter;

    public ServicesAdapter(Context applicationContext, String[] countryList, int[] flags) {
        this.context = context;
        this.countryList = countryList;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_servicios, null);
/*
        TextView title = (TextView) view.findViewById(R.id.service_title);
        TextView info = (TextView) view.findViewById(R.id.service_info);

        ImageView icon = (ImageView) view.findViewById(R.id.service_icon);
        //ImageView iconLink = (ImageView) view.findViewById(R.id.service_icon_link);
        title.setText(countryList[i]);
        info.setText(countryList[i]);
        icon.setImageResource(flags[i]);
/*

        ProgressBar progressBar  = view.findViewById(R.id.progressbar);
/*
        CircularProgressBar circularProgressBar = view.findViewById(R.id.circularProgressBar);
        // Set Progress
        circularProgressBar.setProgress(65f);
        // or with animation
        circularProgressBar.setProgressWithAnimation(65f, (long) 1000); // =1s

        // Set Progress Max
        circularProgressBar.setProgressMax(200f);

        // Set ProgressBar Color
        circularProgressBar.setProgressBarColor(Color.BLACK);
        // or with gradient
        circularProgressBar.setProgressBarColorStart(Color.GRAY);
        circularProgressBar.setProgressBarColorEnd(Color.RED);
        circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

        // Set background ProgressBar Color
        circularProgressBar.setBackgroundProgressBarColor(Color.GRAY);
        // or with gradient
        circularProgressBar.setBackgroundProgressBarColorStart(Color.WHITE);
        circularProgressBar.setBackgroundProgressBarColorEnd(Color.RED);
        circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

        // Set Width
        circularProgressBar.setProgressBarWidth(7f); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(3f); // in DP

        // Other
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(180f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
 */

        //iconLink.setImageResource(flags[i]);;
        return view;
    }

}