package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.ListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BookmarkActivity extends AppCompatActivity {


    private ListView bookMarkListView;
    private ListViewAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private List<String> titleList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();

    private ImageView myPageProfileImage;
    private TextView myPageNickName;

    private Button infoModifyButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        init();

        db = FirebaseFirestore.getInstance();
        adapter = new ListViewAdapter();
        user = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference document = db.collection("users").document(user.getUid())
                .collection("bookmarks");

        DocumentReference document2 = db.collection("users").document(user.getUid());

        document2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        myPageNickName.setText(documentSnapshot.getData().get("nickname")+"님!");
                        if(documentSnapshot.getData().get("profileImageUrl")==null) {
                            myPageProfileImage.setImageResource(R.drawable.app_icon);
                        }
                    }
                } else {
                    Log.d("BookmarkActivity", ""+task.getException());
                }
            }
        });

        infoModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookmarkActivity.this, InfoModifyActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        document.orderBy("time", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()) {
//                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                adapter.addItem(documentSnapshot.get("title").toString(),
//                                        documentSnapshot.get("text").toString(),
//                                        documentSnapshot.get("id").toString(),
//                                        documentSnapshot.get("time").toString(),
//                                        documentSnapshot.get("name").toString());
//                                titleList.add(documentSnapshot.getId());
//                                categoryList.add(documentSnapshot.get("category").toString());
//
//                            }
//
//                        }
//                    }
//                });



    }

    private void init() {
        myPageProfileImage = (ImageView) findViewById(R.id.mypage_profile_image);
        myPageNickName = (TextView) findViewById(R.id.mypage_nickname);
        infoModifyButton=(Button)findViewById(R.id.infoModifyButton);
        backButton = (Button)findViewById(R.id.bookmarkBackButton);
    }
}
