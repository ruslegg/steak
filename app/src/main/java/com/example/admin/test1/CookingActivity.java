package com.example.admin.test1;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CookingActivity extends AppCompatActivity {
    public static List<Guideline> guidelines = new ArrayList<>();
    private ViewPager mViewPager;

    private GuidelineAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);
        data();
        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.ring);
        mViewPager = (ViewPager) findViewById(R.id.guidelinePager);
        mCardAdapter = new GuidelineAdapter();

        for (int i = 0; i < guidelines.size(); i++) {
            mCardAdapter.addCardItemS(guidelines.get(i));
        }
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(10);
//        final RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
//        rv.setHasFixedSize(true);
//        CardSliderLayoutManager llm = new CardSliderLayoutManager(this,AttributeSet);
//        llm.canScrollHorizontally();
////        llm.getActiveCardRight();
////        rv.setLayoutManager(llm);


//        final GuidelineAdapterTest adapter = new GuidelineAdapterTest(guidelines);
//        rv.setAdapter(adapter);
        Button testButton = (Button)findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardAdapter.addCardItemS(new Guideline("CHECKING","CHECKIN 2"));
                mCardAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mCardAdapter.getCount()-1);
                mediaPlayer.start();
            }
        });
    }

    public void data() {
        for (int i = 0; i < 3; i++) {
            guidelines.add(new Guideline("Header "+i,"But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, d"));
        }
    }


}
