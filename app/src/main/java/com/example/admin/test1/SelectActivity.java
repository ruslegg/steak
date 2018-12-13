package com.example.admin.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectActivity extends AppCompatActivity {
    static TextView temperature,requiredTemperatureTextView;
    public static ArrayList<String> meatList = new ArrayList<String>();
    public static ArrayList<String> meatDoneness = new ArrayList<String>();
    private String meatSelected = "";
    private String donenessSelected = "";
    public static int requiredTemperature = 0;

    public static String[][] meats = new String[][]{
            {"Meat","Rare","Medium rare","Medium","Medium well","Well-done"},
            {"Ground Beef",null,null,"71",null,null},
            {"Ground poultry",null,null,"74",null,null},
            {"Beef","52","60","66","71","74"},
            {"Veal","52","60","66","71","74"},
            {"Chicken",null,null,"74",null,null},
            {"Pork",null,null,"71","74","77"},
            {"Poultry",null,null,"74",null,null},
            {"Lamb","60","63","71","74","77"},
            {"Fish",null,null,"63",null,null}};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        temperature = (TextView)findViewById(R.id.currentTempText);
        requiredTemperatureTextView = (TextView)findViewById(R.id.requiredTempText);
//        meatArray.addAll(Arrays.asList(meatList));
//
        for (int i = 0; i <meats.length; i++) {
            System.out.println("");
            for (int j = 0; j < meats[i].length; j++) {
                System.out.printf("%-15s",meats[i][j]);
            }
        }
        System.out.println();
        for (int i = 1; i < meats.length ; i++) {
            meatList.add(meats[i][0]);
        }
        final Spinner meatSpinner = (Spinner) findViewById(R.id.meatSpinner);
        final ArrayAdapter meatAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,meatList);
        meatSpinner.setAdapter(meatAdapter);
        meatSpinner.setSelection(-1,true);
//        meatSpinner.setEnabled(false);
        final Spinner donenessSpinner = (Spinner) findViewById(R.id.donenessSpinner);
        final ArrayAdapter donenessAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,meatDoneness);
        donenessSpinner.setAdapter(donenessAdapter);
        donenessSpinner.setSelection(-1,true);
        meatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                meatDoneness.clear();
                int meatPosition = -1;
                for (int i = 1; i < meats.length; i++) {
                    if (meats[i][0].equals(meatList.get(position))){
                        meatPosition = position+1;
                        break;
                    }
                }
                if (meatPosition != -1) {
                    for (int i = 1; i < meats[meatPosition].length; i++) {
                        if (meats[meatPosition][i] != null) {
//                            System.out.println(meats[meatPosition][i]);
                            meatDoneness.add(meats[0][i]);
                        }
                    }
                }
                donenessAdapter.notifyDataSetChanged();
                meatSelected = (String) parent.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        donenessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                donenessSelected = (String) parent.getSelectedItem();
                for (int i = 1; i < meats.length; i++) {
                    if (meats[i][0].equals(meatSelected)) {
                        for (int j = 0; j < meats[0].length; j++) {
                            if (meats[0][j].equals(donenessSelected)){
                                System.out.println("TEMP"+meats[i][j]);
                                requiredTemperature = Integer.parseInt(meats[i][j]);
                                requiredTemperatureTextView.setText(meats[i][j]);
                            }
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button continueButton = (Button)findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CookingActivity.class));
            }
        });

    }

}
