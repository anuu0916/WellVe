package com.diary.jimin.wellve;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MemberInitActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private EditText dateEditText;
    private Button checkButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        init();

        checkButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });


    }

    void init() {
        nameEditText = (EditText)findViewById(R.id.memberNameEditText);
        phoneEditText = (EditText)findViewById(R.id.memberPhoneEditText);
        addressEditText = (EditText)findViewById(R.id.memberAddressEditText);
        dateEditText = (EditText)findViewById(R.id.memberDateEditText);
        checkButton = (Button)findViewById(R.id.memberCheckButton);


        mAuth = FirebaseAuth.getInstance();
    }

    private void update() {
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String date = dateEditText.getText().toString();

        if(name.length()>0 && phone.length()>0 && address.length()>0 && date.length()>0) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

//             MemberInfo memberInfo = new MemberInfo(name, phone, address, date);

            if(user != null) {
//                db.collection("users").document(user.getUid()).set(memberInfo)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(MemberInitActivity.this, "성공",
//                                        Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MemberInitActivity.this, "실패",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }

        } else {
            Toast.makeText(MemberInitActivity.this, "회원 정보를 입력하세요",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
