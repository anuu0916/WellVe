package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diary.jimin.wellve.model.PostInfo;
import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText postTitleEditText;
    private EditText postTextEditText;
    private Button postButton;
    private String name;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String getCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        getCategory = intent.getStringExtra("setCategory");
        Log.d("getCategory", getCategory);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        init();

        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getData().get("name").toString();
                        Log.d("getname", "DocumentSnapshot data: " + document.getData().get("name"));
                    } else {
                        Log.d("getname", "No such document");
                    }
                } else {
                    Log.d("getname", "get failed with ", task.getException());
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });


    }

    private void init() {

        postTitleEditText = findViewById(R.id.postTitleEditText);
        postTextEditText = findViewById(R.id.postTextEditText);
        postButton = findViewById(R.id.postButton);


    }

    private void post() {
        String title;
        String text;
        String id;
        String time;


        title = postTitleEditText.getText().toString();
        text = postTextEditText.getText().toString();



        id = user.getUid();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        time = sdfNow.format(date);





        if(title.length() > 0 && text.length()>0) {


            PostInfo postInfo = new PostInfo(title, text, id, time, name);

            if(user != null) {
                db.collection(getCategory)
                        .add(postInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(PostActivity.this, "성공",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(PostActivity.this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }
}