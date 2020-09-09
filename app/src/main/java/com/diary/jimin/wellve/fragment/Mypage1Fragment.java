package com.diary.jimin.wellve.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.jimin.wellve.activity.PostInActivity;
import com.diary.jimin.wellve.model.CommunityItem;
import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public class Mypage1Fragment extends Fragment {

    private ArrayList<CommunityItem> items = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private List<String> idList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();

    private ProgressBar progressBar;

    private int freeSize;
    private int QnASize;
    private int restaurantSize;
    private int literSize;

    private String imageURL;

    public static Mypage1Fragment getInstance() {
        Mypage1Fragment mypage1Fragment = new Mypage1Fragment();
        return mypage1Fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page,container,false);
        progressBar = view.findViewById(R.id.progressBar);

        initDataset();

        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.page1RecyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(context, items);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(getActivity(), PostInActivity.class);
                intent.putExtra("setId", idList.get(pos));
                intent.putExtra("setCategory", categoryList.get(pos));
                startActivity(intent);
            }
        });

        return view;
    }

    private void initDataset() {

        db = FirebaseFirestore.getInstance();

        items.clear();

        CollectionReference collectionReference = db.collection("freePosts");

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Boolean photoBool = (Boolean) documentSnapshot.getData().get("photo");
                                db.collection("comments")
                                        .whereEqualTo("postId", documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    freeSize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        freeSize++;
                                                    }

                                                    if (photoBool) {
                                                        String imageStr = documentSnapshot.getData().get("time").toString().replace("/","").replace(" ","_").replace(":","");
                                                        String newUri = imageStr.substring(0, imageStr.length()-2);
                                                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                                                        StorageReference storageReference = storage.getReference().child(documentSnapshot.getData().get("title")+"_"+newUri+".jpeg");
                                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                imageURL = uri.toString();

                                                                items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                        imageURL,
                                                                        documentSnapshot.getData().get("title").toString(),
                                                                        documentSnapshot.getData().get("time").toString(),
                                                                        "자유 ",
                                                                        String.valueOf(freeSize)
                                                                ));
//                                                                Log.d("comment", "이미지 O : "+freeSize);
                                                                idList.add(documentSnapshot.getId());
                                                                categoryList.add("freePosts");
                                                                recyclerView.setAdapter(adapter);
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    } else {
                                                        items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                null,
                                                                documentSnapshot.getData().get("title").toString(),
                                                                documentSnapshot.getData().get("time").toString(),
                                                                "자유 ",
                                                                String.valueOf(freeSize)
                                                        ));
                                                        idList.add(documentSnapshot.getId());
                                                        categoryList.add("freePosts");
                                                        recyclerView.setAdapter(adapter);
                                                        progressBar.setVisibility(View.GONE);
                                                    }



                                                }
                                            }
                                        });

                            }
                        }
                    }
                });


        collectionReference = db.collection("QnAPosts");

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Boolean photoBool = (Boolean) documentSnapshot.getData().get("photo");
                                db.collection("comments")
                                        .whereEqualTo("postId", documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    QnASize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        QnASize++;
                                                    }

                                                    if (photoBool) {
                                                        String imageStr = documentSnapshot.getData().get("time").toString().replace("/","").replace(" ","_").replace(":","");
                                                        String newUri = imageStr.substring(0, imageStr.length()-2);
                                                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                                                        StorageReference storageReference = storage.getReference().child(documentSnapshot.getData().get("title")+"_"+newUri+".jpeg");
                                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                imageURL = uri.toString();

                                                                items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                        imageURL,
                                                                        documentSnapshot.getData().get("title").toString(),
                                                                        documentSnapshot.getData().get("time").toString(),
                                                                        "QnA ",
                                                                        String.valueOf(QnASize)
                                                                ));
                                                                idList.add(documentSnapshot.getId());
                                                                categoryList.add("QnAPosts");
                                                                recyclerView.setAdapter(adapter);
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    } else {
                                                        items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                null,
                                                                documentSnapshot.getData().get("title").toString(),
                                                                documentSnapshot.getData().get("time").toString(),
                                                                "QnA ",
                                                                String.valueOf(QnASize)
                                                        ));
                                                        idList.add(documentSnapshot.getId());
                                                        categoryList.add("QnAPosts");
                                                        recyclerView.setAdapter(adapter);
                                                        progressBar.setVisibility(View.GONE);
                                                    }

                                                }
                                            }
                                        });

                            }
                        }
                    }
                });

        collectionReference = db.collection("restaurantPosts");

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Boolean photoBool = (Boolean) documentSnapshot.getData().get("photo");
                                db.collection("comments")
                                        .whereEqualTo("postId", documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    restaurantSize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        restaurantSize++;
                                                    }

                                                    if (photoBool) {
                                                        String imageStr = documentSnapshot.getData().get("time").toString().replace("/","").replace(" ","_").replace(":","");
                                                        String newUri = imageStr.substring(0, imageStr.length()-2);
                                                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                                                        StorageReference storageReference = storage.getReference().child(documentSnapshot.getData().get("title")+"_"+newUri+".jpeg");
                                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                imageURL = uri.toString();

                                                                items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                        imageURL,
                                                                        documentSnapshot.getData().get("title").toString(),
                                                                        documentSnapshot.getData().get("time").toString(),
                                                                        "식당 ",
                                                                        String.valueOf(restaurantSize)
                                                                ));
//                                                                Log.d("comment", "이미지 O : "+freeSize);
                                                                idList.add(documentSnapshot.getId());
                                                                categoryList.add("restaurantPosts");
                                                                recyclerView.setAdapter(adapter);
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    } else {
                                                        items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                null,
                                                                documentSnapshot.getData().get("title").toString(),
                                                                documentSnapshot.getData().get("time").toString(),
                                                                "식당 ",
                                                                String.valueOf(restaurantSize)
                                                        ));
                                                        idList.add(documentSnapshot.getId());
                                                        categoryList.add("restaurantPosts");
                                                        recyclerView.setAdapter(adapter);
                                                        progressBar.setVisibility(View.GONE);
                                                    }

                                                }
                                            }
                                        });

                            }
                        }
                    }
                });

        collectionReference = db.collection("literPosts");

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Boolean photoBool = (Boolean) documentSnapshot.getData().get("photo");
                                db.collection("comments")
                                        .whereEqualTo("postId", documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    literSize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        literSize++;
                                                    }

                                                    if (photoBool) {
                                                        String imageStr = documentSnapshot.getData().get("time").toString().replace("/","").replace(" ","_").replace(":","");
                                                        String newUri = imageStr.substring(0, imageStr.length()-2);
                                                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                                                        StorageReference storageReference = storage.getReference().child(documentSnapshot.getData().get("title")+"_"+newUri+".jpeg");
                                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                imageURL = uri.toString();

                                                                items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                        imageURL,
                                                                        documentSnapshot.getData().get("title").toString(),
                                                                        documentSnapshot.getData().get("time").toString(),
                                                                        "문학 ",
                                                                        String.valueOf(literSize)
                                                                ));
//                                                                Log.d("comment", "이미지 O : "+freeSize);
                                                                idList.add(documentSnapshot.getId());
                                                                categoryList.add("literPosts");
                                                                recyclerView.setAdapter(adapter);
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    } else {
                                                        items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                null,
                                                                documentSnapshot.getData().get("title").toString(),
                                                                documentSnapshot.getData().get("time").toString(),
                                                                "문학 ",
                                                                String.valueOf(literSize)
                                                        ));
                                                        idList.add(documentSnapshot.getId());
                                                        categoryList.add("literPosts");
                                                        recyclerView.setAdapter(adapter);
                                                        progressBar.setVisibility(View.GONE);
                                                    }

                                                }
                                            }
                                        });

                            }
                        }
                    }
                });



    }
}
