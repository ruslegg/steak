package com.example.admin.test1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends AppCompatActivity {
    public static List<Option> options= new ArrayList<Option>();
    public static Option selectedOption = new Option();
    OptionsAdapter optionsAdapter;
    SharedPreferences appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("Options", "");
        Type type = new TypeToken<List<Option>>(){}.getType();
        options = gson.fromJson(json, type);

        ListView optionsLV = (ListView)findViewById(R.id.optionsLV);
        optionsAdapter = new OptionsAdapter(getApplicationContext(),options);
        optionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Option option = options.get(position);
                startActivity(new Intent(getApplicationContext(),SelectActivity.class));
                SelectActivity.fromOptions = true;
                selectedOption = option;
            }
        });
        optionsLV.setAdapter(optionsAdapter);


    }
}
