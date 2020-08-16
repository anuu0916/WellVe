package com.diary.jimin.wellve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

//    private ListView listView;
//    private CategoryAdapter adapter;
//    private FirebaseFirestore db;
//
//    private List<String> categoryList = new ArrayList<>();


    private ViewPager vp;
    private VPAdapter vpAdapter;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        vp = findViewById(R.id.categoryViewPager);


        getTabs();
//        listView = (ListView) findViewById(R.id.categoryListView);

//        db = FirebaseFirestore.getInstance();
//        adapter = new CategoryAdapter();
//
//
//        categoryList.add("freePosts");
//        categoryList.add("QnAPosts");
//        categoryList.add("restaurantPosts");
//        categoryList.add("literPosts");
//
//
//        adapter.addItem("자유게시판");
//        adapter.addItem("QnA");
//        adapter.addItem("비건 식당 추천");
//        adapter.addItem("비건 문학 추천");
//
//        listView.setAdapter(adapter);
//
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(CategoryActivity.this, BoardActivity.class);
//                intent.putExtra("category", categoryList.get(position));
//                startActivity(intent);
//
//            }
//        });
    }

    public void getTabs() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                vpAdapter = new VPAdapter(getSupportFragmentManager());

                vpAdapter.addFragment(Page1Fragment.getInstance(),"전체");
                vpAdapter.addFragment(Page2Fragment.getInstance(),"자유");
                vpAdapter.addFragment(Page3Fragment.getInstance(),"QnA");
                vpAdapter.addFragment(Page4Fragment.getInstance(),"식당");
                vpAdapter.addFragment(Page5Fragment.getInstance(),"문학");

                vp.setAdapter(vpAdapter);

                tab = findViewById(R.id.tab);
                tab.setupWithViewPager(vp);
            }
        });


    }
}

