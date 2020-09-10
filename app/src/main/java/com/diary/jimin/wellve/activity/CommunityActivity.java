package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.PagerAdapter;
import com.diary.jimin.wellve.adapter.VPAdapter;
import com.diary.jimin.wellve.fragment.Page1Fragment;
import com.diary.jimin.wellve.fragment.Page2Fragment;
import com.diary.jimin.wellve.fragment.Page3Fragment;
import com.diary.jimin.wellve.fragment.Page4Fragment;
import com.diary.jimin.wellve.fragment.Page5Fragment;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class CommunityActivity extends AppCompatActivity {


    private Button communitySearchButton;
    private Button communityWriteButton;
    private Button backButton;

    private PagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        init();


        communitySearchButton = findViewById(R.id.communitySearchButton);
        communityWriteButton = findViewById(R.id.communityWriteButton);

        tabLayout = findViewById(R.id.tab);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = findViewById(R.id.categoryViewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        communityWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        communitySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    
    void init() {
        communitySearchButton = (Button)findViewById(R.id.communitySearchButton);
        communityWriteButton = (Button)findViewById(R.id.communityWriteButton);
        backButton = (Button)findViewById(R.id.categoryBackButton);
    }

}

