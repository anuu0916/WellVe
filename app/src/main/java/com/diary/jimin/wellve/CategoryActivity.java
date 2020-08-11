package com.diary.jimin.wellve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private CategoryAdapter adapter;
    private FirebaseFirestore db;

    private List<String> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = (ListView) findViewById(R.id.categoryListView);

        db = FirebaseFirestore.getInstance();
        adapter = new CategoryAdapter();


        categoryList.add("freePosts");
        categoryList.add("QnAPosts");
        categoryList.add("restaurantPosts");
        categoryList.add("literPosts");


        adapter.addItem("자유게시판");
        adapter.addItem("QnA");
        adapter.addItem("비건 식당 추천");
        adapter.addItem("비건 문학 추천");

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, BoardActivity.class);
                intent.putExtra("category", categoryList.get(position));
                startActivity(intent);

            }
        });

    }
}
