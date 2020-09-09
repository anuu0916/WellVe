package com.diary.jimin.wellve.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InfoModifyActivity extends AppCompatActivity {

    private Button veganModifyButton;
    private Button modifyPhotoButton;
    private Button nicknameModifyButton;
    private Button backButton;

    private final int GET_GALLERY_IMAGE = 200;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String nickname;
    private String photoUri;
    private Uri selectedImageUri;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_modify);

        init();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {

                        if(document.getData().get("nickname") == null) {
                            nickname = "null";
                        } else {
                            nickname = document.getData().get("nickname").toString();
                        }

                        if(document.getData().get("profileImageUrl") == null) {
                            photoUri = "null";
                        } else {
                            photoUri = document.getData().get("profileImageUrl").toString();
                        }
                    }
                }
            }
        });

        veganModifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoModifyActivity.this, ReTypeCheckActivity.class);
                intent.putExtra("getNickname", nickname);
                intent.putExtra("getUri", photoUri);
                startActivity(intent);
            }
        });

        nicknameModifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(InfoModifyActivity.this, NicknameModifyActivity.class);
                intent.putExtra("getNickname", nickname);
                intent.putExtra("getUri", photoUri);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        modifyPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            Log.d("gallery", ""+selectedImageUri);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            String filename = user.getUid() + ".jpeg";

            StorageReference storageReference = storage.getReferenceFromUrl("gs://wellve.appspot.com")
                    .child(filename);
            storageReference.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            db.collection("users").document(user.getUid())
                                    .update("profileImageUrl",filename)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(InfoModifyActivity.this, "프로필 사진을 바꿨습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InfoModifyActivity.this, "프로필 사진을 바꾸지 못했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }


    void init(){
        veganModifyButton = (Button)findViewById(R.id.infoVeganModifyButton);
        modifyPhotoButton = (Button)findViewById(R.id.modifyPhotoButton);
        nicknameModifyButton=(Button)findViewById(R.id.nicknameModifyButton);
        backButton = (Button)findViewById(R.id.infoModifyBackButton);
    }
}