package com.example.admin.test1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectActivity extends AppCompatActivity {
    static TextView temperature;
    public static ArrayList<String> meatArray = new ArrayList<String>();
    public static ArrayList<String> meatDonenessArray = new ArrayList<String>();

    String[] meatList = {"Chicken","Lamb","Beef","Something else"};
    String[] meatDoneness = {"Rare","Medium Rare","Medium","Medium well","Well done"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        meatArray.addAll(Arrays.asList(meatList));
        Spinner meatSpinner = (Spinner) findViewById(R.id.meatSpinner);
        ArrayAdapter meatAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,meatList);
        meatSpinner.setAdapter(meatAdapter);
        meatSpinner.setSelection(2,true);
        meatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), meatList[position], Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        meatDonenessArray.addAll(Arrays.asList(meatDoneness));
        final Spinner donenessSpinner = (Spinner) findViewById(R.id.donenessSpinner);
        final ArrayAdapter donenessAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,meatDoneness);
        donenessSpinner.setAdapter(donenessAdapter);
        donenessSpinner.setSelection(2,true);
        donenessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), meatDoneness[position], Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        temperature = (TextView)findViewById(R.id.tempStatus);

    }

}
