package com.diary.jimin.wellve.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
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
        private TextView detailFoodType;
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
                detailFoodType = layout.findViewById(R.id.detailFoodType);
                Bundle bundle = getArguments();
                if(bundle != null){
                        String resultText = bundle.getString("resultText");
                        int foodType;
                        int material;

//                        if((material = resultText.indexOf("원재료명")) != -1){
//                                detailResult.setText(resultText.substring(material+5));
//                        }
//                        else{
//                                detailResult.setText(resultText);
//                        }
                        detailResult.setText(resultText);

                }

                return layout;
        }

}