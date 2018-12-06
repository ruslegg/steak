package com.example.admin.test1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class GuidelineAdapterTest extends RecyclerView.Adapter<GuidelineViewHolder> {

    List<Guideline> guidelines;

    GuidelineAdapterTest(List<Guideline> guidelines){
        this.guidelines = guidelines;
    }
    @NonNull
    @Override
    public GuidelineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.guideline, viewGroup, false);
        GuidelineViewHolder gvh = new GuidelineViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GuidelineViewHolder guidelineViewHolder, int i) {
        guidelineViewHolder.header.setText(guidelines.get(i).header);
        guidelineViewHolder.text.setText(guidelines.get(i).text);

    }

    @Override
    public int getItemCount() {
        return guidelines.size();
    }
}
