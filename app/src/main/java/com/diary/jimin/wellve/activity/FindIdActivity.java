package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.diary.jimin.wellve.R;

public class FindIdActivity extends AppCompatActivity {
    String[] items = {"010", "011", "019"};

    private Button backButton;
    private Button authNumButton;
    private Button authButton;
    private EditText findIdPhoneNumEditText;
    private EditText findIdNumEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        init();
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindIdActivity.this, FindIdCompleteActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init(){
        backButton = (Button) findViewById(R.id.findIdBackButton);
        authNumButton = (Button) findViewById(R.id.authNumButton);
        authButton = (Button) findViewById(R.id.authButton);
        findIdPhoneNumEditText = (EditText) findViewById(R.id.findIdPhoneNumEditText);
        findIdNumEditText = (EditText) findViewById(R.id.findIdNumEditText);

        findIdNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               authButton.setSelected(true);
                authButton.setTextColor(getResources().getColor(R.color.colorWhite));
            }

            @Override
            public void afterTextChanged(Editable s) {
                authButton.setSelected(true);
                authButton.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
    }
}