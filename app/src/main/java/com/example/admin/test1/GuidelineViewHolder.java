package com.example.admin.test1;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class GuidelineViewHolder extends RecyclerView.ViewHolder{
    CardView cv;
    TextView header,text;

    public GuidelineViewHolder(@NonNull View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById(R.id.guidelineCardView);
        header = (TextView)itemView.findViewById(R.id.guidelineHeader);
        text = (TextView)itemView.findViewById(R.id.guidelineText);
    }

}
