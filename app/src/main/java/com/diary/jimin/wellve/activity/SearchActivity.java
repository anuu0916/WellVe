package com.diary.jimin.wellve.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.adapter.RecyclerViewAdapter;
import com.diary.jimin.wellve.model.CommunityItem;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {

    private Button backButton;
    private EditText searchEditText;
    private ImageButton searchButton;
    private RecyclerView searchRecyclerview;
    private ProgressBar progressBar;
    private Spinner spinner;

    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CommunityItem> items = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();

    private FirebaseFirestore db;
    private String[] spinnerItems = {"자유", "QnA", "식당", "문학"};
    private String category;
    private String categoryItem;
    private String search;
    private String imageUrL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        searchRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        searchRecyclerview.setLayoutManager(mLayoutManager);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    category = "freePosts";
                    categoryItem = "자유 ";
                } else if (position == 1) {
                    category = "QnAPosts";
                    categoryItem = "QnA ";
                } else if (position == 2) {
                    category = "restaurantPosts";
                    categoryItem = "식당 ";
                } else if (position == 3) {
                    category = "literPosts";
                    categoryItem = "문학 ";
                }

                Log.d("spinner", category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "freePosts";
            }
        });


        adapter = new RecyclerViewAdapter(getApplicationContext(), items);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                initDataSet();
                progressBar.setVisibility(View.GONE);
            }
        });

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(SearchActivity.this, PostInActivity.class);
                intent.putExtra("setId",idList.get(pos));
                intent.putExtra("setCategory", category);
                startActivity(intent);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    void init() {
        backButton = (Button) findViewById(R.id.searchBackButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchRecyclerview = (RecyclerView) findViewById(R.id.page1RecyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        spinner = findViewById(R.id.spinner);
        progressBar.setVisibility(View.GONE);
    }

    private void initDataSet() {
        db = FirebaseFirestore.getInstance();

        items.clear();

        search = searchEditText.getText().toString();
        Log.d("searchList", search);
        Log.d("searchList", category);
        if (search.length() > 0) {
            CollectionReference collectionReference = db.collection(category);
            collectionReference
                    .orderBy("time", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("searchList", "successful");
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Boolean photoBool = (Boolean) document.getData().get("photo");
                                    if (document.getData().get("title").toString().contains(search)) {
                                        Log.d("searchList", document.getData().toString());
                                        if (photoBool) {
                                            String imageStr = document.getData().get("time").toString().replace("/", "").replace(" ", "_").replace(":", "");
                                            String newUri = imageStr.substring(0, imageStr.length() - 2);
                                            FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                                            StorageReference storageReference = storage.getReference().child(document.getData().get("title") + "_" + newUri + ".jpeg");
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageUrL = uri.toString();

                                                    items.add(new CommunityItem(document.getData().get("name").toString(),
                                                            imageUrL,
                                                            document.getData().get("title").toString(),
                                                            document.getData().get("time").toString(),
                                                            categoryItem,
                                                            null
                                                    ));
                                                    idList.add(document.getId());
                                                    searchRecyclerview.setAdapter(adapter);
                                                }
                                            });
                                        } else {
                                            items.add(new CommunityItem(document.getData().get("name").toString(),
                                                    null,
                                                    document.getData().get("title").toString(),
                                                    document.getData().get("time").toString(),
                                                    categoryItem,
                                                    null
                                            ));
                                            idList.add(document.getId());
                                            searchRecyclerview.setAdapter(adapter);
                                        }
                                    }
                                }
                            } else {
                                Log.d("searchList", task.getException().toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("searchList", e + "");
                        }
                    });
        } else {
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }


    }
}