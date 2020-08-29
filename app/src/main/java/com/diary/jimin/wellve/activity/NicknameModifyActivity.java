package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class NicknameModifyActivity extends AppCompatActivity {

    private EditText nicknameEditText;
    private Button completeButton;
    private Button backButton;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_modify);

        db=FirebaseFirestore.getInstance();

        init();

        completeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                isNameCheck();
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
        nicknameEditText = (EditText) findViewById(R.id.nicknameModifyEditText);
        completeButton=(Button)findViewById(R.id.nicknameModifyCompleteButton);
        backButton = (Button)findViewById(R.id.nicknameModifyBackButton);

        mAuth = FirebaseAuth.getInstance();

        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                completeButton.setSelected(true);
                completeButton.setTextColor(getResources().getColor(R.color.colorSelect));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                completeButton.setSelected(true);
                completeButton.setTextColor(getResources().getColor(R.color.colorSelect));
            }
        });
    }

    @Override

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void isNameCheck() {
        final String nickName = nicknameEditText.getText().toString();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("nameCheck", document.getData().get("name").toString());
                                if(nickName.equals(document.getData().get("name"))) {
                                    Toast.makeText(NicknameModifyActivity.this, "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                }

                            }
                            Intent intent = new Intent(NicknameModifyActivity.this, BookmarkActivity.class);
                            intent.putExtra("nickName",nickName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    private void newName(){
        final String nickName = nicknameEditText.getText().toString();

        if(nickName.length()>0){
            mAuth.
        }
    }

}