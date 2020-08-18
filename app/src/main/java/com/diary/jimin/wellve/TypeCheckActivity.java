package com.diary.jimin.wellve;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TypeCheckActivity extends AppCompatActivity {

    private Button typePescoButton;
    private Button typeLactoovoButton;
    private Button typeLactoButton;
    private Button typeOvoButton;
    private Button typeVeganButton;
    private Button typeSignUpButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_check);

        init();
        clickListener();


    }

    private void init() {
        typePescoButton = (Button)findViewById(R.id.typePescoButton);
        typeLactoovoButton = (Button)findViewById(R.id.typeLactoovoButton);
        typeLactoButton = (Button)findViewById(R.id.typeLactoButton);
        typeOvoButton = (Button)findViewById(R.id.typeOvoButton);
        typeVeganButton = (Button)findViewById(R.id.typeVeganButton);
        typeSignUpButton = (Button)findViewById(R.id.typeSignUpButton);

    }

    private void clickListener() {
        typePescoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typePescoButton.isSelected()) {
                    typePescoButton.setSelected(true);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);

                    

                } else {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);
                }
            }
        });

        typeLactoovoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeLactoovoButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(true);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);
                } else {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);
                }
            }
        });

        typeLactoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeLactoButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(true);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);
                } else {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);
                }
            }
        });

        typeOvoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeOvoButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(true);
                    typeVeganButton.setSelected(false);
                } else {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);
                }
            }
        });

        typeVeganButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeVeganButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(true);
                } else {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);
                }
            }
        });


    }
}
