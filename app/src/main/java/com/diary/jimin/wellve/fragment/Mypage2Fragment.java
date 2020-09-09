package com.diary.jimin.wellve.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Mypage2Fragment extends Fragment {

    private ArrayList<CommunityItem> items = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private List<String> idList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();

    private ProgressBar progressBar;


    public static Mypage2Fragment getInstance() {
        Mypage2Fragment mypage2Fragment = new Mypage2Fragment();
        return mypage2Fragment;
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

        CollectionReference collectionReference = db.collection("comments");

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .whereEqualTo("id", user.getUid())
                .orderBy("time",Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String getCategory = null;
                                if(documentSnapshot.getData().get("category").toString().equals("freePosts")) {
                                    getCategory = "자유 ";
                                } else if(documentSnapshot.getData().get("category").toString().equals("QnAPosts")) {
                                    getCategory = "QnA ";
                                } else if(documentSnapshot.getData().get("category").toString().equals("restaurantPosts")) {
                                    getCategory = "식당 ";
                                } else if(documentSnapshot.getData().get("category").toString().equals("literPosts")) {
                                    getCategory = "문학 ";
                                }

                                items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                        null,
                                        documentSnapshot.getData().get("text").toString(),
                                        documentSnapshot.getData().get("time").toString(),
                                        getCategory,
                                        null
                                ));
                                idList.add(documentSnapshot.getData().get("postId").toString());
                                categoryList.add(documentSnapshot.getData().get("category").toString());
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    }
                });



    }
}
