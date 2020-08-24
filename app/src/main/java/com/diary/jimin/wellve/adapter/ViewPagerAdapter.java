package com.diary.jimin.wellve.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.diary.jimin.wellve.activity.FragmentCameraResult;
import com.diary.jimin.wellve.activity.FragmentCameraResultDetail;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public int mCount;

    public ViewPagerAdapter(FragmentCameraResult fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new FragmentCameraResultDetail();
        else return new FragmentCameraResult();

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public int getRealPosition(int position) { return position % mCount; }

}