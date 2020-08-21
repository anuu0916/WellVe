package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.diary.jimin.wellve.adapter.ListViewAdapter;
import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter;
    private Button makeButton;
    private TextView boardCategoryTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<String> titleList = new ArrayList<>();

    private String getCategory;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        getCategory = intent.getStringExtra("category");
        Log.d("getCategory", getCategory);

        listView = (ListView) findViewById(R.id.listView);
        boardCategoryTextView = (TextView) findViewById(R.id.boardCategoryTextView);
        mAuth = FirebaseAuth.getInstance();

        if(getCategory.equals("freePosts")) {
            boardCategoryTextView.setText("자유게시판");
        } else if (getCategory.equals("QnAPosts")) {
            boardCategoryTextView.setText("QnA");
        } else if (getCategory.equals("restaurantPosts")) {
            boardCategoryTextView.setText("비건 식당 추천");
        }else if (getCategory.equals("literPosts")) {
            boardCategoryTextView.setText("비건 문학 추천");
        }



        makeButton = (Button) findViewById(R.id.makeButton);
        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, PostActivity.class);
                intent.putExtra("setCategory",getCategory);
                startActivity(intent);


            }
        });


        db = FirebaseFirestore.getInstance();
        adapter = new ListViewAdapter();
        CollectionReference collectionReference = db.collection(getCategory);

        collectionReference
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                adapter.addItem(document.getData().get("title").toString(),
                                        document.getData().get("text").toString(),
                                        document.getData().get("id").toString(),
                                        document.getData().get("time").toString(),
                                        document.getData().get("name").toString());
                                titleList.add(document.getId());
                            }



                            listView.setAdapter(adapter);

                        } else {

                        }
                    }
                });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BoardActivity.this, PostInActivity.class);
                intent.putExtra("ID",titleList.get(position));
                intent.putExtra("setCategory",getCategory);
                startActivity(intent);
            }
        });

    }


}
