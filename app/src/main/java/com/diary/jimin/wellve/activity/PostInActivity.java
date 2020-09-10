package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diary.jimin.wellve.adapter.CommentAdapter;
import com.diary.jimin.wellve.model.PostInfo;
import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostInActivity extends AppCompatActivity {


    private TextView postInTitleTextView;
    private TextView postInContentTextView;
    private TextView postInIdTextView;
    private TextView postInDateTextView;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CircleImageView postProfileImage;
    private ImageButton deleteButton;


    private ListView commentListView;
    private CommentAdapter adapter;

    private EditText postInCommentEditText;
    private ImageButton postInSubmitButton;
    private ImageButton postInMarkButton;
    private TextView postInCommentNumText;
    private TextView postInCategory;
    private ImageView postInImageView;
    private Button backButton;
    private Toolbar toolbar;

    private String getId;   //문서 id
    private String getCategory; //문서 컬렉션 이름
    private String postId; //문서 uid

    private String comment;
    private String time;
    private String name;

    private String imageStr;
    private String deleteStr;

    private int commentCount;

    private PostInfo postInfo;
    private Boolean photoBool;
    private PostInfo categoryInfo;

    private Boolean isUser=false; //글쓴이인지 구

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_in2);
        init();


        Intent intent = getIntent();
        getId = intent.getStringExtra("setId");
        getCategory = intent.getStringExtra("setCategory");

        Log.d("PostInActivity", getId);
        Log.d("PostInActivity", getCategory);


        user = FirebaseAuth.getInstance().getCurrentUser();
        adapter = new CommentAdapter();

        DocumentReference documentReference1 = db.collection("users").document(user.getUid());
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        name = documentSnapshot.getData().get("nickname").toString();

                    } else {
                        Log.d("commentAdapter", "No such document");
                    }
                }else {

                }
            }
        });



        //  postId=
        //Log.d("PostInActivity", );

        Log.d("PostInActivity",user.getUid());

//        DocumentReference documentReference1 = db.collection("users").document(user.getUid());
//
//        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document1 = task.getResult();
//                    if(document1.exists()){
//                        adapter.titleTextView.setText((CharSequence) document1.getData().get("nickname"));
//                    }
//                }
//                else {
//                    Log.d("PostInActivity", ""+task.getException());
//                }
//            }
//        });

        DocumentReference documentReference = db.collection(getCategory).document(getId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { //게시글 데이터 가져오기
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        photoBool = (Boolean) document.getData().get("photo");
                        postInTitleTextView.setText(document.getData().get("title").toString());
                        postInContentTextView.setText(document.getData().get("text").toString());
                        postInIdTextView.setText(document.getData().get("name").toString());
                        postInDateTextView.setText(document.getData().get("time").toString());
                        if (getCategory.equals("freePosts")) {
                            postInCategory.setText("자유");
                            Log.d("PostInActivity", "test");
                        } else if (getCategory.equals("QnAPosts")) {
                            postInCategory.setText("QnA");
                        } else if (getCategory.equals("literPosts")) {
                            postInCategory.setText("문학");
                        } else if (getCategory.equals("restaurantPosts")) {
                            postInCategory.setText("식당");
                        }

                        DocumentReference documentReference1 = db.collection("users").document(document.getData().get("id").toString());
                        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.getData().get("profileImageUrl") == null) {
                                        postProfileImage.setImageResource(R.drawable.default_user);
                                    } else {
                                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                                        StorageReference storageReference = storage.getReference().child(documentSnapshot.getData().get("profileImageUrl").toString());
                                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    Glide.with(PostInActivity.this)
                                                            .load(task.getResult())
                                                            .apply(new RequestOptions().centerCrop())
                                                            .into(postProfileImage);
                                                } else {
                                                    Toast.makeText(PostInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                    //bookmarkButton 사용자에 따라 변경
                                   if(user.getUid().equals(document.getData().get("id").toString())){
                                        postInMarkButton.setBackgroundResource(R.drawable.community_mod_btn);
                                        isUser=true;

                                    } else{
                                        postInMarkButton.setBackgroundResource(R.drawable.bookmark_button);
                                        isUser=false;
                                    }
                                }
                            }
                        });


                        postInfo = new PostInfo(document.getData().get("title").toString(),
                                document.getData().get("text").toString(),
                                document.getData().get("id").toString(),
                                document.getData().get("time").toString(),
                                document.getData().get("name").toString());
                        imageStr = document.getData().get("time").toString().replace("/", "").replace(" ", "_").replace(":", "");
                        String newUri = imageStr.substring(0, imageStr.length() - 2);

                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://wellve.appspot.com");
                        StorageReference storageReference = storage.getReference(document.getData().get("title") + "_" + newUri + ".jpeg");

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageURL = uri.toString();
                                Log.d("postInImageView", imageURL);
                                Glide.with(getApplicationContext()).load(imageURL).into(postInImageView);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("path", "" + e);
                            }
                        });


                    } else {
                        Toast.makeText(PostInActivity.this, "No such document " + document.exists(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PostInActivity.this, "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("comments");
        //댓글 데이터 리스트 가져오기
        collectionReference
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentCount = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("postId").equals(getId)) {
                                    commentCount++;

                                    adapter.addItem(document.getData().get("text").toString(),
                                            document.getData().get("id").toString(),
                                            document.getData().get("time").toString(),
                                            document.getData().get("name").toString());

                                }
                            }
                            postInCommentNumText.setText(Integer.toString(commentCount));
                            if (adapter.getCount() != 0) {
                                commentListView.setAdapter(adapter);
                            } else {
                                Log.d("PostInActivity", "adapter is empty ?: " + adapter.getCount());
                            }
                        }
                    }
                });


        db.collection("users").document(user.getUid()).collection("bookmarks").document(getId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d("testLog", "DocumentSnapshot successfully updated!");
                                //초록색으로 바꾸기
                                postInMarkButton.setBackgroundResource(R.drawable.bookmark_yes);
                                postInMarkButton.setSelected(true);
                                Log.d("testLog", documentSnapshot.getData().toString());
                            }
                        } else {
                            Log.d("testLog", "DocumentSnapshot successfully updated!");
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
                Log.d("getId", user.getUid());
                if (isUser == false) {
                    if (!postInMarkButton.isSelected()) {
                        if (postInfo != null) {
                            db.collection("users").document(user.getUid())
                                    .collection("bookmarks").document(getId)
                                    .set(postInfo);

                            db.collection("users").document(user.getUid())
                                    .collection("bookmarks").document(getId)
                                    .update("category", getCategory);

                            db.collection("users").document(user.getUid())
                                    .collection("bookmarks").document(getId)
                                    .update("photo", photoBool);

                            postInMarkButton.setSelected(true);
                            postInMarkButton.setBackgroundResource(R.drawable.bookmark_yes);
                            Toast.makeText(PostInActivity.this, "북마크 성공", Toast.LENGTH_SHORT).show();
                        }
                    } else if (postInMarkButton.isSelected()) {
                        postInMarkButton.setBackgroundResource(R.drawable.bookmark_button);
                        postInMarkButton.setSelected(false);
                        db.collection("users").document(user.getUid())
                                .collection("bookmarks").document(getId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(PostInActivity.this, "북마크 삭제", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    } else {
                        deleteStr = getId.toString();
                        Log.d("out123", deleteStr);
                        Snackbar.make(v, "게시물 삭제", Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (deleteStr != null) {
                                    db.collection(getCategory).document(deleteStr)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("out123", getCategory);
                                                    Toast.makeText(view.getContext(), "게시물 삭제 완료", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(PostInActivity.this, CommunityActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);

                                                    //           listViewItemList.remove(pos);
                                                    //          notifyDataSetChanged();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(view.getContext(), "게시물 삭제 실패", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        }).show();
                    }
                }
            }

        });

        backButton.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                onBackPressed();
            }
            });

        }

    private void init() {

        postInTitleTextView = (TextView) findViewById(R.id.postInTitleTextView);
        postInContentTextView = (TextView) findViewById(R.id.postInContentTextView);
        postInIdTextView = (TextView) findViewById(R.id.postInIdTextView);
        postInDateTextView = (TextView) findViewById(R.id.postInDateTextView);
        postInCategory = (TextView) findViewById(R.id.postInCategory);
        db = FirebaseFirestore.getInstance();
        deleteButton = (ImageButton) findViewById(R.id.commentDeleteButton);


        commentListView = (ListView) findViewById(R.id.postInListView);
        postInCommentEditText = (EditText) findViewById(R.id.postInCommentEditText);
        postInSubmitButton = (ImageButton) findViewById(R.id.postInSubmitButton);
        postInMarkButton = (ImageButton) findViewById(R.id.postInMarkButton);
        postInCommentNumText = (TextView) findViewById(R.id.postInCommentNumText);
        postInImageView = (ImageView) findViewById(R.id.postInImageView);
        backButton = (Button) findViewById(R.id.postInBackButton);
        postProfileImage = (CircleImageView) findViewById(R.id.post_profile_image);

        toolbar = (Toolbar) findViewById(R.id.toolBar);

    }

    private void commentUpload() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        time = sdfNow.format(date);

        if (comment.length() > 0) {
            PostInfo postInfo = new PostInfo(comment, user.getUid(), time, name, getCategory, getId);
            if (user != null) {
                db.collection("comments")
                        .add(postInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(PostInActivity.this, "성공", Toast.LENGTH_SHORT).show();
                                adapter.addItem(comment, user.getUid(), time, name);
                                adapter.notifyDataSetChanged();
                                postInCommentEditText.setText("");
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(postInCommentEditText.getWindowToken(), 0);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostInActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        } else {
            Toast.makeText(PostInActivity.this, "댓글을 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }
}
