package com.example.admin.test1;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GuidelineAdapter extends PagerAdapter {

    private List<CardView> mViews;
    private List<Guideline> mData;
    private float mBaseElevation;
    int MAX_ELEVATION_FACTOR = 8;

    public GuidelineAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItemS(Guideline item) {
        mViews.add(null);
        mData.add(item);
    }


    public float getBaseElevation() {
        return mBaseElevation;
    }

    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    public int getCount() {
        return mData.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.guideline, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.guidelineCardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Guideline item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.guidelineHeader);
        TextView contentTextView = (TextView) view.findViewById(R.id.guidelineText);
        titleTextView.setText(item.getHeader());
        contentTextView.setText(item.getText());
    }

}

