package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CameraActivity extends AppCompatActivity {

    private Bundle bundle = new Bundle();
    private Bundle bundle2 = new Bundle();
//    private String type = "";
    private Bitmap bm;
    private String resultText;
    private String[] resultArray;

    private ImageView photo;
    private String isStr;


    //private String userType = "";
    ViewPager vp;

    private String [] VeganTypeArray = {
            "Pesco", "LactoOvo", "Lacto", "Ovo", "Vegan"
    };
    ArrayList<String> VeganType = new ArrayList<>(Arrays.asList(VeganTypeArray));

    private String [] UnknownArray = {
    };
    ArrayList<String> Unknown = new ArrayList<>(Arrays.asList(UnknownArray));

    private String [] VeganIngredientArray = {
    };
    ArrayList<String> VeganIngredient = new ArrayList<>(Arrays.asList(VeganIngredientArray));

    private String [] LactoOvoIngredientArray = {
    };
    ArrayList<String> LactoOvoIngredient = new ArrayList<>(Arrays.asList(LactoOvoIngredientArray));

    private String [] LactoIngredientArray = {
    };
    ArrayList<String> LactoIngredient = new ArrayList<>(Arrays.asList(LactoIngredientArray));

    private String [] OvoIngredientArray = {
    };
    ArrayList<String> OvoIngredient = new ArrayList<>(Arrays.asList(OvoIngredientArray));

    private String [] PescoIngredientArray = {
    };
    ArrayList<String> PescoIngredient = new ArrayList<>(Arrays.asList(PescoIngredientArray));

    private static final String [] Animal = {
            "젤라틴", "인산골", "아교", "칼슘", "육즙", "골탄", "골분", "콜라겐", "선지",
            "올레오스테아린", "동물성단백질", "엘라스틴", "케라틴", "레티쿨린", "레닛", "런넷", "가죽", "펩신", "케라틴",
            "판크레아틴", "트립신", "수지", "라드", "돈유", "돈지유", "탤로", "드리핑", "마지", "말기름", "돈지", "돼지기름",
            /*"우지",*/ "쇠기름", "양지", "거위기름", "계유", "콘드로이틴", "고기"
    };
    private static final String [] Ocean = {
            "용연향", "자개", "조개", "캐비어", "키틴", "산호", "생선", "비늘", "어분", "부레풀", "해면", "진주",
            /*"알",*/ "경랍", "어유", "간유", "경유", "바다표범", "키토산", "새우"
    };
    private static final String [] Ovo= {
            "달걀", "난황", "난백", "난각", "알부민"
    };
    private static final String [] Lacto = {
            "젖", "카세인", "락토오스", "유당", "유청", "락티톨", "우유", "버터", "탈지분유", "전지분유"
    };
    private static final String [] Insect = {
            "꿀", "벌 화분", "비폴렌", "봉독", "벌침", "밀랍", "로열젤리", "프로폴리스",
            "E120 코치닐", "카민", "카르민", "달팽이", "곤충", "셸락"
    };
    private static final String [] Else = {
            "비타민D3", "철분", "혈액"
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();

//        if (null == savedInstanceState) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, Camera2BasicFragment.newInstance())
//                    .commit();
//        }

        Intent intent = getIntent();
        resultText = intent.getStringExtra("resultText");
        isStr = intent.getStringExtra("isStr");

        if (resultText == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }else if(!resultText.contains(",")){
            Toast.makeText(CameraActivity.this, "원재료명을 촬영해주세요!", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
        else {
            try {
                Log.d("inputStream", "try");
                Uri uri = Uri.parse(isStr);
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
                bm = BitmapFactory.decodeStream(inputStream);

//                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//                cursor.moveToNext();
//                String filepath = cursor.getString(cursor.getColumnIndex("_data"));
//                cursor.close();
//
//                ExifInterface ei = new ExifInterface(filepath);
//                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                        ExifInterface.ORIENTATION_UNDEFINED);
//
//                Bitmap rotatedBitmap = null;
//                switch (orientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        rotatedBitmap = rotateImage(bm, 90);
//                        break;
//
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        rotatedBitmap = rotateImage(bm, 180);
//                        break;
//
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        rotatedBitmap = rotateImage(bm, 270);
//                        break;
//
//                    case ExifInterface.ORIENTATION_NORMAL:
//                    default:
//                        rotatedBitmap = bm;
//                }

                photo.setImageBitmap(bm);

            } catch (FileNotFoundException e) {
                bm = BitmapFactory.decodeFile(isStr);
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(isStr);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bm, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bm, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bm, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bm;
                }

                photo.setImageBitmap(rotatedBitmap);

                e.printStackTrace();
                Log.d("inputStream", "no");
            } catch (IOException e) {
                e.printStackTrace();
            }

            resultText = resultText.replaceAll(System.getProperty("line.separator"), " ");
            resultArray = resultText.split(",");
            ArrayList<String> resultList = new ArrayList<>();
            for(String s : resultArray){
                resultList.add(s);
            }
            Log.d("resultText", resultList+"");

            for(String s : resultList){
                if(s.contains("가공품") || s.contains("씨즈닝") || s.contains("시즈닝")){
                    Unknown.add(s);
                }
            }

            for(String s : Else){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganIngredient.add(s);
                    Log.d("veganType", "Else : " + s);
                    //break;
                }
            }

            for(String s : Insect){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    VeganIngredient.add(s);
                    OvoIngredient.add(s);
                    Log.d("veganType", "Insect : " + s);
                    //break;
                }
            }

            for(String s : Lacto){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    VeganIngredient.add(s);
                    OvoIngredient.add(s);
                    Log.d("veganType", "Lacto : " + s);
                    //break;
                }
            }

            for(String s : Ovo){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    VeganType.remove("Lacto");
                    VeganIngredient.add(s);
                    OvoIngredient.add(s);
                    LactoIngredient.add(s);
                    Log.d("veganType", "Ovo : " + s);
                    //break;
                }
            }

            for(String s : Ocean){
                if(resultText.contains(s)){
                    VeganType.remove("Vegan");
                    VeganType.remove("Ovo");
                    VeganType.remove("Lacto");
                    VeganType.remove("LactoOvo");
                    VeganIngredient.add(s);
                    OvoIngredient.add(s);
                    LactoIngredient.add(s);
                    LactoOvoIngredient.add(s);
                    Log.d("veganType", "Ocean : " + s);
                    //break;
                }
            }

            for(String s : Animal){
                if(resultText.contains(s)){
                    VeganType.clear();
                    bundle.putString("VeganIngredient", s);
                    bundle.putString("OvoIngredient", s);
                    bundle.putString("LactoIngredient", s);
                    bundle.putString("LactoOvoIngredient", s);
                    bundle.putString("PescoIngredient", s);
                    VeganIngredient.add(s);
                    OvoIngredient.add(s);
                    LactoIngredient.add(s);
                    LactoOvoIngredient.add(s);
                    PescoIngredient.add(s);
                    Log.d("veganType", "Animal : " + s);
                    //break;
                }
            }

            vp = (ViewPager)findViewById(R.id.vp);
            vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
            vp.setCurrentItem(0);

//            FragmentCameraResult fragmentCameraResult;
//            fragmentCameraResult = new FragmentCameraResult();
//            bundle.putStringArrayList("veganType", VeganType);
//            bundle.putStringArrayList("VeganIngredient", VeganIngredient);
//            bundle.putStringArrayList("LactoIngredient", LactoIngredient);
//            bundle.putStringArrayList("OvoIngredient", OvoIngredient);
//            bundle.putStringArrayList("LactoOvoIngredient", LactoOvoIngredient);
//            bundle.putStringArrayList("PescoIngredient", PescoIngredient);
//            bundle.putString("resultText", resultText);
//            fragmentCameraResult.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentCameraResult).commit();
        }


    }

    private static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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
                    FragmentCameraResult fragmentCameraResult;
                    fragmentCameraResult = new FragmentCameraResult();
                    bundle.putStringArrayList("veganType", VeganType);
                    bundle.putStringArrayList("VeganIngredient", VeganIngredient);
                    bundle.putStringArrayList("LactoIngredient", LactoIngredient);
                    bundle.putStringArrayList("OvoIngredient", OvoIngredient);
                    bundle.putStringArrayList("LactoOvoIngredient", LactoOvoIngredient);
                    bundle.putStringArrayList("PescoIngredient", PescoIngredient);
                    bundle.putStringArrayList("Unknown", Unknown);
                    bundle.putString("resultText", resultText);
                    fragmentCameraResult.setArguments(bundle);
                    return fragmentCameraResult;
//                case 1:
//                    FragmentCameraResultDetail fragmentCameraResultDetail;
//                    fragmentCameraResultDetail = new FragmentCameraResultDetail();
//                    detailbundle.putString("resultText", resultText);
//                    fragmentCameraResultDetail.setArguments(detailbundle);
//                    return fragmentCameraResultDetail;
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 1;
        }
    }

    void init(){
        photo = (ImageView)findViewById(R.id.cameraPhoto);
    }
}
