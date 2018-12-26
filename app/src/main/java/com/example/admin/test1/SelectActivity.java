package com.example.admin.test1;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.admin.test1.MainActivity.CONNECTING_STATUS;
import static com.example.admin.test1.MainActivity.address;
import static com.example.admin.test1.MainActivity.createBluetoothSocket;
import static com.example.admin.test1.MainActivity.mBTAdapter;
import static com.example.admin.test1.MainActivity.mBTSocket;
import static com.example.admin.test1.MainActivity.name;

public class SelectActivity extends AppCompatActivity {
    static TextView temperature,requiredTemperatureTextView;
    public static Spinner meatSpinner,donenessSpinner;
    public static ArrayAdapter donenessAdapter;
    public static ArrayAdapter meatAdapter;


    public static ArrayList<String> meatList = new ArrayList<String>();
    public static ArrayList<String> meatDoneness = new ArrayList<String>();
    public static String meatSelected = "";
    public static String donenessSelected = "";
    public static int requiredTemperature = 0;
    public static boolean fromOptions = false;
    String comment = "";
    Gson gson = new Gson();
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
    public void onResume() {
        super.onResume();
        meatAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meatList);
        meatSpinner.setAdapter(meatAdapter);
        meatSpinner.setSelection(-1, true);
        donenessAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meatDoneness);
        donenessSpinner.setAdapter(donenessAdapter);
        donenessSpinner.setSelection(-1, true);
        meatAdapter.notifyDataSetChanged();
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

        setContentView(R.layout.activity_select);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        temperature = (TextView) findViewById(R.id.currentTempText);
        requiredTemperatureTextView = (TextView) findViewById(R.id.requiredTempText);


        if(!fromOptions) {
            for (int i = 0; i < meats.length; i++) {
                System.out.println("");
                for (int j = 0; j < meats[i].length; j++) {
                    System.out.printf("%-15s", meats[i][j]);
                }
            }
            System.out.println();
            for (int i = 1; i < meats.length; i++) {
                meatList.add(meats[i][0]);
            }
        }
        meatSpinner = (Spinner) findViewById(R.id.meatSpinner);
        meatAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meatList);
        meatSpinner.setAdapter(meatAdapter);
        meatSpinner.setSelection(-1, true);
        meatAdapter.notifyDataSetChanged();
        donenessSpinner = (Spinner) findViewById(R.id.donenessSpinner);
        donenessAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meatDoneness);
        donenessSpinner.setAdapter(donenessAdapter);
        donenessSpinner.setSelection(-1, true);
        meatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                meatSelected = (String) parent.getSelectedItem();
                getDonenessSpinnerUpdated(position);
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
                            if (meats[0][j].equals(donenessSelected)) {
                                if(meats[i][j] != null) {
                                    requiredTemperature = Integer.parseInt(meats[i][j]);
                                    requiredTemperatureTextView.setText(meats[i][j]);
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CookingActivity.class));
            }
        });

        Button addToOptions = (Button) findViewById(R.id.mainAddToOptions);
        addToOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this, R.style.MyAlertDialogStyle);
                builder.setTitle("Please insert comment for the option");

// Set up the input
                final EditText input = new EditText(SelectActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("Add option", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        comment = input.getText().toString();
                        String json = appSharedPrefs.getString("Options", "");
                        Type type = new TypeToken<ArrayList<Option>>() {
                        }.getType();
                        List<Option> options = gson.fromJson(json, type);

                        System.out.println("OPTIONS: " + options);
                        if (options == null) {
                            options = new ArrayList<Option>();
                        }
                        if (meatSelected.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please choose a meat to add to the option", Toast.LENGTH_SHORT).show();
                        } else if (donenessSelected.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please choose doneness to add to the option", Toast.LENGTH_SHORT).show();
                        } else {
                            options.add(new Option(meatSelected, donenessSelected, comment));
                            String jsonOptions = gson.toJson(options);
                            prefsEditor.putString("Options", jsonOptions);
                            prefsEditor.commit();
                            startActivity(new Intent(getApplicationContext(), OptionsActivity.class));
                        }
                    }
                });
                builder.show();


            }
        });
        Button setToFavorite = (Button) findViewById(R.id.setToFavorite);
        setToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = appSharedPrefs.getString("Favorite", "");
                Option favorite = gson.fromJson(json, Option.class);

                if (favorite == null) {
                    favorite = new Option();
                }
                if (meatSelected.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please choose a meat to add to the option", Toast.LENGTH_SHORT).show();
                } else if (donenessSelected.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please choose doneness to add to the option", Toast.LENGTH_SHORT).show();
                } else {
                    favorite = new Option(meatSelected, donenessSelected);
                    String jsonOptions = gson.toJson(favorite);
                    prefsEditor.putString("Favorite", jsonOptions);
                    prefsEditor.commit();
                    Toast.makeText(getApplicationContext(), "This option is now set to favorite", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button favoriteButton = (Button) findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences appSharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = appSharedPrefs.getString("Favorite", "");
                Option favorite = gson.fromJson(json, Option.class);
                if(favorite != null && favorite.getType() != null && favorite.getDoneness() != null) {
                    for (int i = 0; i < meatAdapter.getCount(); i++) {
                        if (meatAdapter.getItem(i).equals(favorite.getType())) {
                            meatSpinner.setSelection(i);
                            getDonenessSpinnerUpdated(i);
                            break;
                        }
                    }
                    for (int i = 0; i < donenessAdapter.getCount(); i++) {
                        if (donenessAdapter.getItem(i).equals(favorite.getDoneness())) {
                            donenessSpinner.setSelection(i);
                            break;
                        }
                    }
                }
                else {
                    Toast.makeText(SelectActivity.this, "You have not set up any favorite yet", Toast.LENGTH_SHORT).show();
                }


            }
        });
        Button optionsButton = (Button) findViewById(R.id.goOptionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OptionsActivity.class));
            }
        });

        if (fromOptions) {
            setSelectedFromOption(OptionsActivity.selectedOption);
        }


    }
    public static void getDonenessSpinnerUpdated(Integer position) {
        meatDoneness.clear();
        int meatPosition = -1;
        for (int i = 1; i < meats.length; i++) {
            if (meats[i][0].equals(meatList.get(position))) {
                meatPosition = position + 1;
                break;
            }
        }
        if (meatPosition != -1) {
            for (int i = 1; i < meats[meatPosition].length; i++) {
                if (meats[meatPosition][i] != null) {
                    meatDoneness.add(meats[0][i]);
                }
            }
        }
        donenessAdapter.notifyDataSetChanged();
    }

    public static void setSelectedFromOption(Option option) {
        for (int i = 0; i < meatAdapter.getCount(); i++) {
            if (meatAdapter.getItem(i).equals(option.getType())) {
                meatSpinner.setSelection(i);
                getDonenessSpinnerUpdated(i);
                break;
            }
        }
        for (int i = 0; i < donenessAdapter.getCount(); i++) {
            if (donenessAdapter.getItem(i).equals(option.getDoneness())) {
                donenessSpinner.setSelection(i);
                break;
            }
        }
    }
}

