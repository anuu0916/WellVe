package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diary.jimin.wellve.R;

public class DeveloperActivity extends AppCompatActivity {

    //    EditText etMessage;
    private Button bt_send;
    private Button backButton;
    private EditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        init();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
//        etMessage = (EditText)findViewById(R.id.et_message);
    }

//    public void onClickSend(View v){
//        sendEmail();
//    }
//
//    protected void sendEmail(){
//        String message = etMessage.getText().toString();
//
//
//    }

    private void sendEmail(){
        String text = et_message.getText().toString();

        if(text.length()>0){
            Toast.makeText(DeveloperActivity.this, "전송 성공! 소중한 의견 감사합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeveloperActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            Toast.makeText(DeveloperActivity.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }
    private void init() {
        bt_send = (Button) findViewById(R.id.bt_send);
        backButton = (Button)findViewById(R.id.developerBackButton);
        et_message = (EditText) findViewById(R.id.et_message);

        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_send.setSelected(true);
                bt_send.setTextColor(getResources().getColor(R.color.colorWhite));
            }

            @Override
            public void afterTextChanged(Editable s) {
                bt_send.setSelected(true);
                bt_send.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
    }
}