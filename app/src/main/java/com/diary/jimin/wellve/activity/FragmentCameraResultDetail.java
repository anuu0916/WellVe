package com.diary.jimin.wellve.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.diary.jimin.wellve.R;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FragmentCameraResultDetail extends Fragment {

        public FragmentCameraResultDetail() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_camera_result_detail, container, false);
                return layout;
        }
}