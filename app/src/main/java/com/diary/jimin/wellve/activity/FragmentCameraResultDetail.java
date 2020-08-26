package com.diary.jimin.wellve.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.diary.jimin.wellve.R;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FragmentCameraResultDetail extends Fragment {

        private TextView detailResult;
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
                detailResult = layout.findViewById(R.id.detailResult);
                Bundle bundle = getArguments();
                if(bundle != null){
                        String resultText = bundle.getString("resultText");
                        detailResult.setText(resultText);
                }

                return layout;
        }

}