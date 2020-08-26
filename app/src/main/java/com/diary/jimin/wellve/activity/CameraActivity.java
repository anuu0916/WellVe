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

public class CameraActivity extends AppCompatActivity {

    private Bundle bundle = new Bundle();
    private Bundle detailbundle = new Bundle();
    private String type = "";
    private Bitmap bm;
    private ImageView imageView;
    private String resultText;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userType = "";
    ViewPager vp;

    private static final String [] Pesco = {
            "E441젤라틴", "E542 식용 인산골", "인산골", "아교", "칼슘", "육즙", "골탄", "고기"
    };
    private static final String [] LactoOvo = {
            "용연향", "자개", "캐비어", "키틴", "산호"
    };
    private static final String [] Lacto = {
            "달걀", "난황", "난백", "난각", "알부민"
    };
    private static final String [] Ovo = {
            "소젖", "우유", "버터"
    };
    private static final String [] Vegan = {
            "비타민D3", "철분"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference = db.collection("users").document(user.getUid());
        Log.d("userType", "userID : "+user.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        userType = (String) documentSnapshot.getString("type");
                        Log.d("userType", userType);
                    }
                } else {
                    Log.d("CameraActivity", ""+task.getException());
                }
            }
        });

        init();

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

        Intent intent = getIntent();
        resultText = intent.getStringExtra("resultText");
        //byte[] byteArray = getIntent().getByteArrayExtra("image");


        imageView.setImageBitmap(bm);

        if(resultText != null){
            resultText = resultText.replaceAll(System.getProperty("line.separator"), " ");

            for(String s : Pesco){
                if(resultText.contains(s)){
                    type += "None";
                }
            }

            for(String s : LactoOvo){
                if(resultText.contains(s)){
                    type += "페스코";
                }
            }

            for(String s : Ovo){
                if(resultText.contains(s)){
                    type += "페스코, 락토오보, 락토";
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
                case 0:
                    return new CameraBlankFragment();
                case 1:
                    FragmentCameraResult fragmentCameraResult;
                    fragmentCameraResult = new FragmentCameraResult();
                    bundle.putString("veganType", type);
                    bundle.putString("userType", userType);
                    Log.d("userType", "before Result : " + type + userType);
                    fragmentCameraResult.setArguments(bundle);
                    return fragmentCameraResult;
                case 2:
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
            return 3;
        }
    }

    void init(){
        imageView = (ImageView)findViewById(R.id.cameraPhoto);
    }
}
