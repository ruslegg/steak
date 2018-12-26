package com.example.admin.test1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.test1.SelectActivity.donenessSelected;
import static com.example.admin.test1.SelectActivity.meatSelected;

public class FinishActivity extends AppCompatActivity {

    Button setToFavorite,addToOptions,exitButton;

    Gson gson = new Gson();

    SharedPreferences appSharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        setToFavorite = (Button) findViewById(R.id.finishSetToFavorite);
        addToOptions = (Button) findViewById(R.id.addToOptions);
        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        setToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = appSharedPrefs.getString("Favorite", "");
                Option favorite = gson.fromJson(json, Option.class);
                try {
                    if (favorite == null) {
                        favorite = new Option();
                    }
                    favorite = new Option(meatSelected,donenessSelected);
                    String jsonOptions = gson.toJson(favorite);
                    prefsEditor.putString("Favorite",jsonOptions);
                    prefsEditor.commit();
                    Toast.makeText(FinishActivity.this, "This options is now set to favorite", Toast.LENGTH_SHORT).show();
                }
                catch (Error error) {
                    Toast.makeText(FinishActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        addToOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FinishActivity.this, R.style.MyAlertDialogStyle);
                builder.setTitle("Please insert comment for the option");

                final EditText input = new EditText(FinishActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                builder.setView(input);
                builder.setPositiveButton("Add option", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String comment = input.getText().toString();
                                String json = appSharedPrefs.getString("Options", "");
                                Type type = new TypeToken<ArrayList<Option>>() {
                                }.getType();
                                List<Option> options = gson.fromJson(json, type);

                                if (options == null) {
                                    options = new ArrayList<Option>();
                                }
                                options.add(new Option(meatSelected, donenessSelected, comment));
                                String jsonOptions = gson.toJson(options);
                                prefsEditor.putString("Options", jsonOptions);
                                prefsEditor.commit();
                                Toast.makeText(FinishActivity.this, "This setup has been added to options", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });

    }
}
