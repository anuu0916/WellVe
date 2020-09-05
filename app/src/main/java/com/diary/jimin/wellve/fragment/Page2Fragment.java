package com.diary.jimin.wellve.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.diary.jimin.wellve.activity.PostInActivity;
import com.diary.jimin.wellve.model.CommunityItem;
import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Page2Fragment extends Fragment {

    private ArrayList<CommunityItem> items = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private FirebaseFirestore db;

    private List<String> idList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    private List<Integer> commentList = new ArrayList<>();

    private int freeSize;
    private String imageURL;
    private int commentPos=0;

    private ProgressBar progressBar;


    public static Page2Fragment getInstance() {
        Page2Fragment page2Fragment = new Page2Fragment();
        return page2Fragment;
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
                                Boolean photoBool = (Boolean) documentSnapshot.getData().get("photo");

                                if(freeSize!=0) freeSize = 0;
                                db.collection("comments")
                                        .orderBy("time", Query.Direction.ASCENDING)
                                        .whereEqualTo("postId",documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    if(freeSize!=0) freeSize = 0;
                                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d("comment", document.getData().get("text").toString());
                                                        freeSize++;
                                                    }
                                                    commentList.add(freeSize);

                                                    Log.d("comment", "commentPos: "+commentPos);
                                                    Log.d("comment", "comment:"+commentList.get(commentPos));
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
                                                                        String.valueOf(commentList.get(commentPos))
                                                                ));
//                                                                Log.d("comment", "이미지 O : "+freeSize);
                                                                idList.add(documentSnapshot.getId());
                                                                categoryList.add("freePosts");
                                                                recyclerView.setAdapter(adapter);
                                                                progressBar.setVisibility(View.GONE);
                                                                commentPos++;
                                                            }
                                                        });
                                                    } else {
                                                        items.add(new CommunityItem(documentSnapshot.getData().get("name").toString(),
                                                                "https://www.colorhexa.com/ffffff.png",
                                                                documentSnapshot.getData().get("title").toString(),
                                                                documentSnapshot.getData().get("time").toString(),
                                                                "자유 ",
                                                                String.valueOf(commentList.get(commentPos))
                                                        ));
//                                                        Log.d("comment", "이미지 X : "+freeSize);
                                                        idList.add(documentSnapshot.getId());
                                                        categoryList.add("freePosts");
                                                        recyclerView.setAdapter(adapter);
                                                        progressBar.setVisibility(View.GONE);
                                                        commentPos++;
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
