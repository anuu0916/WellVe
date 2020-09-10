package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.ListViewAdapter;
import com.diary.jimin.wellve.adapter.VPAdapter;
import com.diary.jimin.wellve.fragment.Mypage1Fragment;
import com.diary.jimin.wellve.fragment.Mypage2Fragment;
import com.diary.jimin.wellve.fragment.Mypage3Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkActivity extends AppCompatActivity {


    private ListView bookMarkListView;
    private ListViewAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private List<String> titleList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();

    private CircleImageView myPageProfileImage;
    private TextView myPageNickName;

    private Button infoModifyButton;
    private Button backButton;

    private ViewPager vp;
    private VPAdapter vpAdapter;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        init();

        vp = findViewById(R.id.categoryViewPager);

        db = FirebaseFirestore.getInstance();
        adapter = new ListViewAdapter();
        user = FirebaseAuth.getInstance().getCurrentUser();

        getTabs();

        CollectionReference document = db.collection("users").document(user.getUid())
                .collection("bookmarks");

        DocumentReference document2 = db.collection("users").document(user.getUid());

        document2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        myPageNickName.setText(documentSnapshot.getData().get("nickname")+"님!");
                        if(documentSnapshot.getData().get("profileImageUrl")==null) {
                            myPageProfileImage.setImageResource(R.drawable.default_user);
                        } else {
                            FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                            StorageReference storageReference = storage.getReference().child(documentSnapshot.getData().get("profileImageUrl").toString());
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Glide.with(BookmarkActivity.this)
                                                .load(task.getResult())
                                                .apply(new RequestOptions().centerCrop())
                                                .into(myPageProfileImage);
                                    } else {
                                        Toast.makeText(BookmarkActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Log.d("BookmarkActivity", ""+task.getException());
                }
            }
        });

        infoModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookmarkActivity.this, InfoModifyActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        document.orderBy("time", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()) {
//                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                adapter.addItem(documentSnapshot.get("title").toString(),
//                                        documentSnapshot.get("text").toString(),
//                                        documentSnapshot.get("id").toString(),
//                                        documentSnapshot.get("time").toString(),
//                                        documentSnapshot.get("name").toString());
//                                titleList.add(documentSnapshot.getId());
//                                categoryList.add(documentSnapshot.get("category").toString());
//
//                            }
//
//                        }
//                    }
//                });



    }

    private void init() {
        myPageProfileImage = (CircleImageView) findViewById(R.id.mypage_profile_image);
        myPageNickName = (TextView) findViewById(R.id.myPageNickName);
        infoModifyButton=(Button)findViewById(R.id.infoModifyButton);
        backButton = (Button)findViewById(R.id.bookmarkBackButton);
    }

    public void getTabs() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                vpAdapter = new VPAdapter(getSupportFragmentManager());

                vpAdapter.addFragment(Mypage1Fragment.getInstance(), "내가 쓴 글");
                vpAdapter.addFragment(Mypage2Fragment.getInstance(), "내가 쓴 댓글");
                vpAdapter.addFragment(Mypage3Fragment.getInstance(), "북마크");

                vp.setAdapter(vpAdapter);

                tab = findViewById(R.id.tab);
                tab.setupWithViewPager(vp);
            }
        });
    }
}
