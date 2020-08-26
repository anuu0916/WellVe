package com.diary.jimin.wellve.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.diary.jimin.wellve.model.CommunityItem;
import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.RecyclerViewAdapter;
import com.diary.jimin.wellve.activity.PostInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Page1Fragment extends Fragment {

    private ArrayList<CommunityItem> items = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private FirebaseFirestore db;

    private List<String> idList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();

    private int freeSize;
    private int restaurantSize;
    private int QnASize;
    private int literSize;

    private ProgressBar progressBar;


    public static Page1Fragment getInstance() {
        Page1Fragment page1Fragment = new Page1Fragment();
        return page1Fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);


        progressBar = view.findViewById(R.id.progressBar);
        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.page1RecyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(context, items);

        initDataset();

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("pagePos", "pos : "+ pos);
                Log.d("pagePos","getpos : "+ idList.get(pos));
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
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                db.collection("comments")
                                        .whereEqualTo("postId",documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    freeSize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        freeSize++;
                                                    }

                                                    items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                            "https://d20aeo683mqd6t.cloudfront.net/ko/articles/title_images/000/039/143/medium/IMG_5649%E3%81%AE%E3%82%B3%E3%83%92%E3%82%9A%E3%83%BC.jpg?2019",
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
                                        });
                            }
                        }
                    }
                });


        collectionReference = db.collection("QnAPosts");

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                db.collection("comments")
                                        .whereEqualTo("postId",documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    QnASize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        QnASize++;
                                                    }

                                                    idList.add(documentSnapshot.getId());
                                                    categoryList.add("QnAPosts");
                                                    recyclerView.setAdapter(adapter);
                                                    progressBar.setVisibility(View.GONE);
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
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                db.collection("comments")
                                        .whereEqualTo("postId",documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    restaurantSize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        restaurantSize++;
                                                    }

                                                    items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                            "https://d20aeo683mqd6t.cloudfront.net/ko/articles/title_images/000/039/143/medium/IMG_5649%E3%81%AE%E3%82%B3%E3%83%92%E3%82%9A%E3%83%BC.jpg?2019",
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
                                        });
                            }
                        }
                    }
                });

        collectionReference = db.collection("literPosts");

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                db.collection("comments")
                                        .whereEqualTo("postId",documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    literSize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        literSize++;
                                                    }

                                                    items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                            "https://d20aeo683mqd6t.cloudfront.net/ko/articles/title_images/000/039/143/medium/IMG_5649%E3%81%AE%E3%82%B3%E3%83%92%E3%82%9A%E3%83%BC.jpg?2019",
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
                                        });
                            }
                        }
                    }
                });



//        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int pos) {
////                Intent intent = new Intent(getActivity(), PostInActivity.class);
////                intent.putExtra("setId", idList.get(pos));
////                intent.putExtra("setCategory", categoryList.get(pos));
////                startActivity(intent);
////                Log.d("pagePos", "pos : "+ pos);
//            }
//        });


//        items.add(new CommunityItem("a", "htttp", "a", "a", "a ", "a"));
    }
}
