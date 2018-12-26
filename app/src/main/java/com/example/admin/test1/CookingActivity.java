package com.example.admin.test1;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.test1.MainActivity.CONNECTING_STATUS;
import static com.example.admin.test1.MainActivity.address;
import static com.example.admin.test1.MainActivity.createBluetoothSocket;
import static com.example.admin.test1.MainActivity.mBTAdapter;
import static com.example.admin.test1.MainActivity.mBTSocket;
import static com.example.admin.test1.MainActivity.name;

public class CookingActivity extends AppCompatActivity {
    public static ViewPager mViewPager;
    public static GuidelineAdapter mCardAdapter;
    public static TextView tempValue,requiredTemp;
    public static Button finishButton;


    static CookingActivity mn;
    public static List<Guideline> guidelines = new ArrayList<>();
    public static boolean finished = false;
    final static MediaPlayer guidePlayer = MediaPlayer.create(mn,R.raw.ring);
    final static MediaPlayer alarmPlayer = MediaPlayer.create(mn,R.raw.alarm);
    final static MediaPlayer donePlayer  = MediaPlayer.create(mn,R.raw.completed);


    @Override
    public void onResume() {
        super.onResume();
        new Thread()
        {
            public void run() {
                boolean fail = false;
                BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                try {
                    mBTSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        mBTSocket.close();
                        MainActivity.mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        //insert code to deal with this
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
                if(fail == false) {
                    MainActivity.mConnectedThread = new MainActivity.ConnectedThread(mBTSocket);
                    MainActivity.mConnectedThread.start();

                    MainActivity.mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                            .sendToTarget();
                    startActivity(new Intent(getApplicationContext(),SelectActivity.class));
                }
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mn = CookingActivity.this;
        setContentView(R.layout.activity_cooking);
        finishButton = (Button)findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FinishActivity.class));
            }
        });
        finishButton.setVisibility(View.INVISIBLE);


        mViewPager = (ViewPager) findViewById(R.id.guidelinePager);
        mCardAdapter = new GuidelineAdapter();
        tempValue = (TextView) findViewById(R.id.tempValue);
        requiredTemp = (TextView)findViewById(R.id.requiredTemp);
        requiredTemp.setText(String.valueOf(SelectActivity.requiredTemperature));

        for (int i = 0; i < guidelines.size(); i++) {
            mCardAdapter.addCardItemS(guidelines.get(i));
        }
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(10);

        mCardAdapter.addCardItemS(new Guideline("Prepare",
                "Things required: Pan, Stove, Desired Meat \n" +
                        "Instructions: \n" +
                        "1.Put your pan on the stove with the medium-high power\n" +
                        "2. Season your meat as desired (Salt, Pepper, Desired spices)" +
                        "3. Put oil on your meat both sides" +
                        "4. After the pan have heated up, put your meat on the pan" +
                        "5. Insert the probe in the meat" +
                        "6. Wait for further guidelines"));
        mCardAdapter.notifyDataSetChanged();
        guidePlayer.start();

        mViewPager.setCurrentItem(mCardAdapter.getCount()-1);
        finishButton = (Button)findViewById(R.id.finishButton);

    }
    public static void startFlip() {
        Thread flipThread = new Thread(() -> {
            while(!finished){
                try{
                    Thread.sleep(60000);
                    mn.runOnUiThread(() -> {
                        mCardAdapter.addCardItemS(new Guideline("Flip","Please flip the meat to properly distribute the heat throughout uncooked portions of the steak."));
                        mCardAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mCardAdapter.getCount()-1);
                        guidePlayer.start();
                    });
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        flipThread.start();
    }
}
