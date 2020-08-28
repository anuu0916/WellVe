package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/*public class FragmentCameraResult extends Fragment {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 2;

    public FragmentCameraResult() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ViewPager2
        ViewPager2 mPager = (ViewPager2)getView().findViewById(R.id.vp);

        //Adapter
        pagerAdapter = new ViewPagerAdapter(this, num_page);
        mPager.setAdapter(pagerAdapter);
        //ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        mPager.setCurrentItem(1000);
        mPager.setOffscreenPageLimit(3);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }


        });


        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_VERTICAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    } else {
                    }
                } else {
                }
            }
        });

    }

}*/
/*
public class FragmentCameraResult extends Fragment {
    ViewPager viewPager;
    RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_result, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.vp);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relayout);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(new FragmentCameraResultDetail(), "First");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        public void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }
}*/

public class FragmentCameraResult extends Fragment implements View.OnClickListener {

    private TextView veganTypeResult;
    private TextView isVeganResult;
    private String userType;

    private FirebaseFirestore db;
    private FirebaseUser user;

    private FrameLayout detailPage = null;
    private Button btn_slide;
    private Animation ani_top = null;
    private Animation ani_bottom = null;
    private boolean isPageState = false;

    Context context;

    public FragmentCameraResult() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();





//        Intent intent = getActivity().getIntent();
//        String veganType = getActivity().getIntent().getStringExtra("veganType");
//        veganTypeResult.setText(veganType);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_camera_result, container, false);
        veganTypeResult = layout.findViewById(R.id.veganTypeResult);
        isVeganResult = layout.findViewById(R.id.isVeganResult);
        Bundle bundle = getArguments();
        if(bundle!=null){
            ArrayList<String> VeganType = bundle.getStringArrayList("veganType");

            db = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();

            DocumentReference documentReference = db.collection("users").document(user.getUid());

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()) {
                            userType = (String) documentSnapshot.getData().get("type");
                            if(VeganType.contains(userType)){
                                isVeganResult.setText("먹을 수 있는 제품");
                            } else{
                                isVeganResult.setText("먹을 수 없는 제품");
                            }
                        }
                    } else {
                        Log.d("FragmentCameraResult", ""+task.getException());
                    }
                }
            });

            if(VeganType.isEmpty()){
                veganTypeResult.setText("섭취 불가");
            } else{
                veganTypeResult.setText("섭취 가능 : "+VeganType);
            }

        }

        /*슬라이드애니메이션*/
        context = getContext();

        View view = inflater.inflate(R.layout.fragment_camera_result, container, false);
        detailPage = (FrameLayout) view.findViewById(R.id.detailPage);
        btn_slide =  (Button) view.findViewById(R.id.btn_slide);
        ani_bottom= AnimationUtils.loadAnimation(context, R.anim.translate_bottom);
        ani_top = AnimationUtils.loadAnimation(context, R.anim.translate_top);

        btn_slide.setOnClickListener(this);

        detailPage.setVisibility(View.VISIBLE);
        Log.d("slide","확인1");
        Log.d("slide","visible");
        return layout;
    }


    public void onClick(View view){
        Log.d("slide", "확인2");
        switch (view.getId()) {
            case R.id.btn_slide: {
                final SlidingAnimationListener listener = new SlidingAnimationListener(detailPage, btn_slide);
                ani_top.setAnimationListener(listener);
                ani_bottom.setAnimationListener(listener);
                isPageState = listener.getIsPageState();
                Log.d("slide", "확인3");
                if (isPageState) {
                    Log.d("slide", "Open  Page");
                    detailPage.startAnimation(ani_bottom);
                } else {
                    Log.d("slide", "Close Page");
                    detailPage.setVisibility(View.VISIBLE);
                    detailPage.startAnimation(ani_top);
                }

                break;
            }
        }
    }

    void init(){
        //veganTypeResult = (TextView)getActivity().findViewById(R.id.veganTypeResult);
    }
}