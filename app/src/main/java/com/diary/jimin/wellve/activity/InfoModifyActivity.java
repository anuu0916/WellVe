package com.diary.jimin.wellve.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.diary.jimin.wellve.R;

public class InfoModifyActivity extends AppCompatActivity {

    private Button veganModifyButton;
    private Button modifyPhotoButton;
    private Button nicknameModifyButton;
    private Button backButton;

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        modifyPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void init(){
        veganModifyButton = (Button)findViewById(R.id.infoVeganModifyButton);
        modifyPhotoButton = (Button)findViewById(R.id.modifyPhotoButton);
        nicknameModifyButton=(Button)findViewById(R.id.nicknameModifyButton);
        backButton = (Button)findViewById(R.id.infoModifyBackButton);
    }
}