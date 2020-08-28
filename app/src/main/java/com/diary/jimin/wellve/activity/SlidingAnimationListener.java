package com.diary.jimin.wellve.activity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SlidingAnimationListener implements Animation.AnimationListener {

    private Boolean isPageState= false;
    private FrameLayout layout;
    private Button button;

    public SlidingAnimationListener(FrameLayout layout, Button button){
        this.layout = layout;
        this.button = button;
    }
    @Override
    public void onAnimationStart(Animation animation){
        if (isPageState){
            Log.d("slide","Open Page");
        } else {
            Log.d("slide","Close Page");
        }
    }

    @Override
    public void onAnimationEnd(Animation animation){
        if (isPageState){
            layout.setVisibility(View.INVISIBLE);
            isPageState=false;
        } else {
            isPageState=true;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation){

    }
    public Boolean getIsPageState(){
        return isPageState;
    }
}

