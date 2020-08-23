package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.diary.jimin.wellve.R;

public class InfoModifyActivity extends AppCompatActivity {

    private Button veganModifyButton;
    private Button nicknameModifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_modify);

        init();

        veganModifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoModifyActivity.this, ReTypeCheckActivity.class);
                startActivity(intent);
            }
        });

        nicknameModifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InfoModifyActivity.this, NicknameModifyActivity.class);
                startActivity(intent);
            }
        });
    }

    void init(){
        veganModifyButton = (Button)findViewById(R.id.infoVeganModifyButton);
        nicknameModifyButton=(Button)findViewById(R.id.nicknameModifyButton);
    }
}