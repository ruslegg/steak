package com.example.admin.test1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class OptionsAdapter extends ArrayAdapter<Option> {
    private List<Option> options;
    Context context;

    public OptionsAdapter(Context context, List<Option> options) {
        super(context,0,options);
    }
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        Option option = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.option,parent,false);
        }

        TextView meat = (TextView)convertView.findViewById(R.id.meatTitle);
        TextView doneness = (TextView)convertView.findViewById(R.id.meatDonenessTitle);
        TextView comment = (TextView)convertView.findViewById(R.id.commentTitle);

        meat.setText(option.getType());
        doneness.setText(option.getDoneness());
        comment.setText(option.getComment());

        return convertView;
    }
}
