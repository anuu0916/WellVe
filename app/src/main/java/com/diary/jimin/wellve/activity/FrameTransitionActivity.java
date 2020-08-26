package com.diary.jimin.wellve.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.diary.jimin.wellve.R;

public class FrameTransitionActivity extends AppCompatActivity {

    private LinearLayout detailPage = null;
    private Button btn_slide;
    private Animation ani_top = null;
    private Animation ani_bottom = null;
    private boolean isPageState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera_result);
        initView();

        final SlidingAnimationListener listener = new SlidingAnimationListener(detailPage, btn_slide);
        ani_top.setAnimationListener(listener);
        ani_bottom.setAnimationListener(listener);

        btn_slide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                isPageState = listener.getIsPageState();
                if(isPageState){
                    detailPage.startAnimation(ani_bottom);
                }else{
                    detailPage.setVisibility(View.VISIBLE);
                    detailPage.startAnimation(ani_top);
                }
            }
        });
    }

    private void initView(){
        detailPage = findViewById(R.id.detailPage);
        ani_bottom= AnimationUtils.loadAnimation(this, R.anim.translate_bottom);
        ani_top = AnimationUtils.loadAnimation(this, R.anim.translate_top);
    }
}