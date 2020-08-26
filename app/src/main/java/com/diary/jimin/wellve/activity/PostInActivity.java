package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.jimin.wellve.adapter.CommentAdapter;
import com.diary.jimin.wellve.model.PostInfo;
import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostInActivity extends AppCompatActivity {


    private TextView postInTitleTextView;
    private TextView postInContentTextView;
    private TextView postInIdTextView;
    private TextView postInDateTextView;
    private FirebaseFirestore db;
    private FirebaseUser user;

    private ListView commentListView;
    private CommentAdapter adapter;

    private EditText postInCommentEditText;
    private ImageButton postInSubmitButton;
//    private Button postInLikeButton;
  //  private TextView postInLikeTextView;
    private ImageButton postInMarkButton;

    private String getId;   //문서 uid
    private String getCategory; //문서 컬렉션 이름

    private String comment;
    private String time;
    private String name;


    private PostInfo postInfo;
    private PostInfo categoryInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_in2);

        init();

        Intent intent = getIntent();
        getId = intent.getStringExtra("setId");
        getCategory = intent.getStringExtra("setCategory");

        user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference = db.collection(getCategory).document(getId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { //게시글 데이터 가져오기
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        postInTitleTextView.setText(document.getData().get("title").toString());
                        postInContentTextView.setText(document.getData().get("text").toString());
                        postInIdTextView.setText(document.getData().get("name").toString());
                        postInDateTextView.setText(document.getData().get("time").toString());

                        name = document.getData().get("name").toString();

                        postInfo = new PostInfo(document.getData().get("title").toString(),
                                document.getData().get("text").toString(),
                                document.getData().get("id").toString(),
                                document.getData().get("time").toString(),
                                document.getData().get("name").toString());


                    } else {
                        Toast.makeText(PostInActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PostInActivity.this, "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        adapter = new CommentAdapter();
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("comments");
        //댓글 데이터 리스트 가져오기
        collectionReference
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("postId").equals(getId)) {
                                    adapter.addItem(document.getData().get("text").toString(),
                                            document.getData().get("id").toString(),
                                            document.getData().get("time").toString(),
                                            document.getData().get("name").toString());
                                }
                            }
                            commentListView.setAdapter(adapter);
                        }
                    }
                });



        postInSubmitButton.setOnClickListener(new View.OnClickListener() {  //댓글 전송
            @Override
            public void onClick(View v) {
                comment = postInCommentEditText.getText().toString();
                commentUpload();
            }
        });

        postInMarkButton.setOnClickListener(new View.OnClickListener() { //북마크
            @Override
            public void onClick(View v) {
                Log.d("getId",user.getUid());
                if(postInfo != null) {
                    db.collection("users").document(user.getUid())
                            .collection("bookmarks").document(getId)
                            .set(postInfo);

                    db.collection("users").document(user.getUid())
                            .collection("bookmarks").document(getId)
                            .update("category", getCategory);
//                    DocumentReference washingtonRef = db.collection("users").document(user.getUid())
//                            .collection("bookmarks").document(getId);
//                    washingtonRef.update("category", FieldValue.arrayUnion(getCategory));

                    Toast.makeText(PostInActivity.this, "북마크 성공", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void init() {

        postInTitleTextView = (TextView) findViewById(R.id.postInTitleTextView);
        postInContentTextView = (TextView) findViewById(R.id.postInContentTextView);
        postInIdTextView = (TextView) findViewById(R.id.postInIdTextView);
        postInDateTextView = (TextView) findViewById(R.id.postInDateTextView);
        db = FirebaseFirestore.getInstance();

        commentListView = (ListView) findViewById(R.id.postInListView);
        postInCommentEditText = (EditText) findViewById(R.id.postInCommentEditText);
        postInSubmitButton = (ImageButton) findViewById(R.id.postInSubmitButton);
//        postInLikeButton = (Button) findViewById(R.id.postInLikeButton);
  //      postInLikeTextView = (TextView) findViewById(R.id.postInLikeTextView);
        postInMarkButton = (ImageButton) findViewById(R.id.postInMarkButton);

    }

    private void commentUpload() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        time = sdfNow.format(date);

        //(String text, String id, String time, String name, String category, String postId)
        if(comment.length() > 0) {
            PostInfo postInfo = new PostInfo(comment, user.getUid(), time, name, getCategory, getId);
            if(user != null) {
                db.collection("comments")
                        .add(postInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(PostInActivity.this, "성공",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostInActivity.this, "실패",Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        } else {
            Toast.makeText(PostInActivity.this, "댓글을 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }
}
