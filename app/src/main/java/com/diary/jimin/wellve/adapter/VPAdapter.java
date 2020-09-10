package com.diary.jimin.wellve.adapter;

import android.os.Parcelable;

import com.diary.jimin.wellve.fragment.Mypage1Fragment;
import com.diary.jimin.wellve.fragment.Mypage2Fragment;
import com.diary.jimin.wellve.fragment.Mypage3Fragment;
import com.diary.jimin.wellve.fragment.Page1Fragment;
import com.diary.jimin.wellve.fragment.Page2Fragment;
import com.diary.jimin.wellve.fragment.Page3Fragment;
import com.diary.jimin.wellve.fragment.Page4Fragment;
import com.diary.jimin.wellve.fragment.Page5Fragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class VPAdapter extends FragmentStatePagerAdapter {
    private int num;

    public VPAdapter(@NonNull FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Mypage1Fragment tab1 = new Mypage1Fragment();
                return tab1;
            case 1:
                Mypage2Fragment tab2 = new Mypage2Fragment();
                return tab2;
            case 2:
                Mypage3Fragment tab3 = new Mypage3Fragment();
                return tab3;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return num;
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
        super.restoreState(state, loader);
    }
}
