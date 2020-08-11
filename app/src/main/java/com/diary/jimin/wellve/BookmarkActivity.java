package com.diary.jimin.wellve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private ListView bookMarkListView;
    private ListViewAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private List<String> titleList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();

    private PostInfo postInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        init();

        db = FirebaseFirestore.getInstance();
        adapter = new ListViewAdapter();
        user = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference document = db.collection("users").document(user.getUid())
                .collection("bookmarks");

        document.orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                adapter.addItem(documentSnapshot.get("title").toString(),
                                        documentSnapshot.get("text").toString(),
                                        documentSnapshot.get("id").toString(),
                                        documentSnapshot.get("time").toString(),
                                        documentSnapshot.get("name").toString());
                                titleList.add(documentSnapshot.getId());
                                categoryList.add(documentSnapshot.get("category").toString());

                            }

                            bookMarkListView.setAdapter(adapter);
                        }
                    }
                });



        bookMarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookmarkActivity.this, PostInActivity.class);
                intent.putExtra("ID",titleList.get(position));
                intent.putExtra("setCategory", categoryList.get(position));
                startActivity(intent);
            }
        });



    }

    private void init() {
        bookMarkListView = (ListView) findViewById(R.id.bookMarkListView);



    }
}
