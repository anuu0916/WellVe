package com.diary.jimin.wellve.adapter;

import com.diary.jimin.wellve.fragment.Page1Fragment;
import com.diary.jimin.wellve.fragment.Page2Fragment;
import com.diary.jimin.wellve.fragment.Page3Fragment;
import com.diary.jimin.wellve.fragment.Page4Fragment;
import com.diary.jimin.wellve.fragment.Page5Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int num;

    public PagerAdapter(@NonNull FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Page1Fragment tab1 = new Page1Fragment();
                return tab1;
            case 1:
                Page2Fragment tab2 = new Page2Fragment();
                return tab2;
            case 2:
                Page3Fragment tab3 = new Page3Fragment();
                return tab3;
            case 3:
                Page4Fragment tab4 = new Page4Fragment();
                return tab4;
            case 4:
                Page5Fragment tab5 = new Page5Fragment();
                return tab5;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
