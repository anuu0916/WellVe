package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.ListViewAdapter;
import com.diary.jimin.wellve.fragment.Camera2BasicFragment;
import com.diary.jimin.wellve.fragment.CameraBlankFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class CameraActivity extends AppCompatActivity {

    private Bundle bundle = new Bundle();
    private Bundle detailbundle = new Bundle();
//    private String type = "";
//    private Bitmap bm;
    private ImageView imageView;
    private String resultText;

    //private String userType = "";
    ViewPager vp;

    private String [] VeganTypeArray = {
            "Pesco", "LactoOvo", "Lacto", "Ovo", "Vegan"
    };
    ArrayList<String> VeganType = new ArrayList<>(Arrays.asList(VeganTypeArray));

    private static final String [] Animal = {
            "젤라틴", "인산골", "아교", "칼슘", "육즙", "골탄", "골분", "콜라겐", "선지",
            "올레오스테아린", "동물성단백질", "엘라스틴", "케라틴", "레티쿨린", "레닛", "런넷", "가죽", "펩신", "케라틴",
            "판크레아틴", "트립신", "수지", "라드", "돈유", "돈지유", "탤로", "드리핑", "마지", "말기름", "돈지", "돼지기름",
            /*"우지",*/ "쇠기름", "양지", "거위기름", "계유", "콘드로이틴", "고기"
    };
    private static final String [] Ocean = {
            "용연향", "자개", "조개", "캐비어", "키틴", "산호", "생선", "비늘", "어분", "부레풀", "해면", "진주",
            "알", "경랍", "어유", "간유", "경유", "바다표범", "키토산"
    };
    private static final String [] Ovo= {
            "달걀", "난황", "난백", "난각", "알부민"
    };
    private static final String [] Lacto = {
            "젖", "카세인", "락토오스", "유당", "유청", "락티톨", "우유", "버터"
    };
    private static final String [] Insect = {
            "꿀", "벌 화분", "비폴렌", "봉독", "벌침", "밀랍", "로열젤리", "프로폴리스",
            "E120 코치닐", "카민", "카르민", "달팽이", "곤충"
    };
    private static final String [] Else = {
            "비타민D3", "철분", "혈액"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

        Intent intent = getIntent();
        resultText = intent.getStringExtra("resultText");
        //byte[] byteArray = getIntent().getByteArrayExtra("image");
        //imageView.setImageBitmap(bm);

        if(resultText != null){
            resultText = resultText.replaceAll(System.getProperty("line.separator"), " ");

            for(String s : Else){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    Log.d("veganType", "Else : " + s);
                    break;
                }
            }

            for(String s : Insect){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    Log.d("veganType", "Insect : " + s);
                    break;
                }
            }

            for(String s : Lacto){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    Log.d("veganType", "Lacto : " + s);
                    break;
                }
            }

            for(String s : Ovo){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    VeganType.remove("Lacto");
                    Log.d("veganType", "Ovo : " + s);
                    break;
                }
            }

            for(String s : Ocean){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    VeganType.remove("Lacto");
                    VeganType.remove("LactoOvo");
                    Log.d("veganType", "Ocean : " + s);
                    break;
                }
            }

            for(String s : Animal){
                if(resultText.contains(s)){
                    VeganType.clear();
                    Log.d("veganType", "Animal : " + s);
                    break;
                }
            }

            vp = (ViewPager)findViewById(R.id.vp);
            vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
            vp.setCurrentItem(0);

            //bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentCameraResult).commit();
        }


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
//                case 0:
//                    return new CameraBlankFragment();
                case 0:
                    FragmentCameraResult fragmentCameraResult;
                    fragmentCameraResult = new FragmentCameraResult();
                    bundle.putStringArrayList("veganType", VeganType);
                    fragmentCameraResult.setArguments(bundle);
                    return fragmentCameraResult;
                case 1:
                    FragmentCameraResultDetail fragmentCameraResultDetail;
                    fragmentCameraResultDetail = new FragmentCameraResultDetail();
                    detailbundle.putString("resultText", resultText);
                    fragmentCameraResultDetail.setArguments(detailbundle);
                    return fragmentCameraResultDetail;
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 2;
        }
    }

    void init(){
        imageView = (ImageView)findViewById(R.id.cameraPhoto);
    }
}
