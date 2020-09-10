package com.diary.jimin.wellve.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.model.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionBuilder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    String[] items = {"자유", "QNA", "식당", "문학"};

    private FirebaseAuth mAuth;

    private EditText postTitleEditText;
    private EditText postTextEditText;
    private Button postButton;
    private Button backButton;
    private ImageButton postPhotoButton;
    private String name;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String getCategory;

    private static final String TAG = "blackjin";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;
    private Boolean isCamera = false;

    private Uri photoUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        
        tedPermission();

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        init();

        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getData().get("nickname").toString();
                        Log.d("getname", "DocumentSnapshot data: " + document.getData().get("nickname"));
                    } else {
                        Log.d("getname", "No such document");
                    }
                } else {
                    Log.d("getname", "get failed with ", task.getException());
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Spinner spinner = findViewById(R.id.postSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getCategory = "freePosts";
                } else if (position == 1) {
                    getCategory = "QnAPosts";
                } else if (position == 2) {
                    getCategory = "restaurantPosts";
                } else if(position == 3) {
                    getCategory = "literPosts";
                }
                Log.d("category", getCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getCategory = "freePosts";
            }
        });

        findViewById(R.id.postPhotoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

    }



    private void init() {

        postTitleEditText = findViewById(R.id.postTitleEditText);
        postTextEditText = findViewById(R.id.postTextEditText);
        postButton = findViewById(R.id.postButton);
        backButton = (Button)findViewById(R.id.postBackButton);
        postPhotoButton = (ImageButton)findViewById(R.id.postPhotoButton);

    }

    private void post() {
        String title;
        String text;
        String id;
        String time;


        title = postTitleEditText.getText().toString();
        text = postTextEditText.getText().toString();

        id = user.getUid();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        time = sdfNow.format(date);

        //filename = date + ".jpeg"


        if(title.length() > 0 && text.length()>0) {
            if(user != null && photoUri != null) {
                PostInfo postInfo = new PostInfo(title, text, id, time, name, true);
                db.collection(getCategory)
                        .add(postInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
                                Date now2 = new Date();
                                String filename = format.format(now2) + ".jpeg";

                                Log.d(TAG, "format1: "+ time+ " format2: "+filename);
                                StorageReference storageReference = storage.getReferenceFromUrl("gs://wellve.appspot.com")
                                        .child(title+"_"+filename);
                                Log.d(TAG, "filename : " + title+"_"+filename);
                                storageReference.putFile(photoUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(PostActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PostActivity.this, "이미지가 안올라가네여 ㅡㅡ", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else if (user != null && photoUri == null) {
                PostInfo postInfo = new PostInfo(title, text, id, time, name);
                db.collection(getCategory)
                        .add(postInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(PostActivity.this, "이미지없이 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(PostActivity.this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            setImage();

        }
    }

    /**
     *  앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        isCamera = false;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void setImage() {

        ImageView imageView = findViewById(R.id.imageView);

        resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView.setImageBitmap(originalBm);

        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        tempFile = null;

    }

    /**
     *  권한 설정
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                isPermission = false;
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    public static void resizeFile(File file, File newFile, int newWidth, Boolean isCamera) {

        String TAG = "blackjin";

        Bitmap originalBm = null;
        Bitmap resizedBitmap = null;

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inDither = true;

            originalBm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            if(isCamera) {

                // 카메라인 경우 이미지를 상황에 맞게 회전시킨다
                try {

                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    Log.d(TAG,"exifDegree : " + exifDegree);

                    originalBm = rotate(originalBm, exifDegree);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if(originalBm == null) {
                Log.e(TAG,("파일 에러"));
                return;
            }

            int width = originalBm.getWidth();
            int height = originalBm.getHeight();

            float aspect, scaleWidth, scaleHeight;
            if(width > height) {
                if(width <= newWidth) return;

                aspect = (float) width / height;

                scaleWidth = newWidth;
                scaleHeight = scaleWidth / aspect;

            } else {

                if(height <= newWidth) return;

                aspect = (float) height / width;

                scaleHeight = newWidth;
                scaleWidth = scaleHeight / aspect;

            }

            // create a matrix for the manipulation
            Matrix matrix = new Matrix();

            // resize the bitmap
            matrix.postScale(scaleWidth / width, scaleHeight / height);

            // recreate the new Bitmap
            resizedBitmap = Bitmap.createBitmap(originalBm, 0, 0, width, height, matrix, true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(newFile));

            } else {

                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 80, new FileOutputStream(newFile));

            }


        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } finally {

            if(originalBm != null){
                originalBm.recycle();
            }

            if (resizedBitmap != null){
                resizedBitmap.recycle();
            }
        }

    }

    /**
     * EXIF 정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    public static int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap 비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }
}