package com.diary.jimin.wellve.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diary.jimin.wellve.model.MemberInfo;
import com.diary.jimin.wellve.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TypeCheckActivity extends Activity {

    private Button typePescoButton;
    private Button typeLactoovoButton;
    private Button typeLactoButton;
    private Button typeOvoButton;
    private Button typeVeganButton;
    private Button typeSignUpButton;
    private Button backButton;

    private String nickName;
    private String type = null;
    private String profileImage = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_check);

        init();
        clickListener();

        Intent intent = getIntent();
        nickName = intent.getStringExtra("nickName");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        typePescoButton = (Button)findViewById(R.id.typePescoButton);
        typeLactoovoButton = (Button)findViewById(R.id.typeLactoovoButton);
        typeLactoButton = (Button)findViewById(R.id.typeLactoButton);
        typeOvoButton = (Button)findViewById(R.id.typeOvoButton);
        typeVeganButton = (Button)findViewById(R.id.typeVeganButton);
        typeSignUpButton = (Button)findViewById(R.id.typeSignUpButton);
        backButton = (Button)findViewById(R.id.typeCheckBackButton);
    }

    private void clickListener() {
        typePescoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typePescoButton.isSelected()) {
                    typePescoButton.setSelected(true);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);

                    typePescoButton.setTextColor(getResources().getColor(R.color.colorSelect));
                    typeLactoovoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeOvoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeVeganButton.setTextColor(getResources().getColor(R.color.colorGray));

                    typePescoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.select_pesco, 0, 0);
                    typeLactoovoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_lactoovo, 0, 0);
                    typeLactoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.milk, 0, 0);
                    typeOvoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_ovo, 0, 0);
                    typeVeganButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cabbage_icon, 0, 0);

                    typeSignUpButton.setSelected(true);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorSelect));

                    type = "Pesco";

                } else {
                    typePescoButton.setSelected(false);
                    typePescoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typePescoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fish, 0, 0);

                    typeSignUpButton.setSelected(false);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }
        });

        typeLactoovoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeLactoovoButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(true);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);

                    typePescoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoovoButton.setTextColor(getResources().getColor(R.color.colorSelect));
                    typeLactoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeOvoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeVeganButton.setTextColor(getResources().getColor(R.color.colorGray));

                    typePescoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fish, 0, 0);
                    typeLactoovoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.select_lactoovo, 0, 0);
                    typeLactoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.milk, 0, 0);
                    typeOvoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_ovo, 0, 0);
                    typeVeganButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cabbage_icon, 0, 0);

                    typeSignUpButton.setSelected(true);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorSelect));

                    type = "Lactoovo";

                } else {
                    typeLactoovoButton.setSelected(false);
                    typeLactoovoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoovoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_lactoovo, 0, 0);

                    typeSignUpButton.setSelected(false);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }
        });

        typeLactoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeLactoButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(true);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(false);

                    typePescoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoovoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoButton.setTextColor(getResources().getColor(R.color.colorSelect));
                    typeOvoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeVeganButton.setTextColor(getResources().getColor(R.color.colorGray));

                    typePescoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fish, 0, 0);
                    typeLactoovoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_lactoovo, 0, 0);
                    typeLactoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.select_lacto, 0, 0);
                    typeOvoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_ovo, 0, 0);
                    typeVeganButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cabbage_icon, 0, 0);

                    typeSignUpButton.setSelected(true);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorSelect));

                    type = "Lacto";
                } else {
                    typeLactoButton.setSelected(false);
                    typeLactoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.milk, 0, 0);

                    typeSignUpButton.setSelected(false);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }
        });

        typeOvoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeOvoButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(true);
                    typeVeganButton.setSelected(false);

                    typePescoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoovoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeOvoButton.setTextColor(getResources().getColor(R.color.colorSelect));
                    typeVeganButton.setTextColor(getResources().getColor(R.color.colorGray));

                    typePescoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fish, 0, 0);
                    typeLactoovoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_lactoovo, 0, 0);
                    typeLactoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.milk, 0, 0);
                    typeOvoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.select_ovo, 0, 0);
                    typeVeganButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cabbage_icon, 0, 0);

                    typeSignUpButton.setSelected(true);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorSelect));

                    type = "Ovo";
                } else {
                    typeOvoButton.setSelected(false);
                    typeOvoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeOvoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_ovo, 0, 0);

                    typeSignUpButton.setSelected(false);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }
        });

        typeVeganButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeVeganButton.isSelected()) {
                    typePescoButton.setSelected(false);
                    typeLactoovoButton.setSelected(false);
                    typeLactoButton.setSelected(false);
                    typeOvoButton.setSelected(false);
                    typeVeganButton.setSelected(true);

                    typePescoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoovoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeLactoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeOvoButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeVeganButton.setTextColor(getResources().getColor(R.color.colorSelect));

                    typePescoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fish, 0, 0);
                    typeLactoovoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_lactoovo, 0, 0);
                    typeLactoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.milk, 0, 0);
                    typeOvoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unselect_ovo, 0, 0);
                    typeVeganButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.select_vegan, 0, 0);

                    typeSignUpButton.setSelected(true);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorSelect));

                    type = "Vegan";
                } else {
                    typeVeganButton.setSelected(false);
                    typeVeganButton.setTextColor(getResources().getColor(R.color.colorGray));
                    typeVeganButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cabbage_icon, 0, 0);

                    typeSignUpButton.setSelected(false);
                    typeSignUpButton.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }
        });


        typeSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeSignUpButton.isSelected()) {
                    //intent
                    MemberInfo memberInfo = new MemberInfo(nickName, type, profileImage);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    if(user != null && type != null) {
                        db.collection("users").document(user.getUid()).set(memberInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(TypeCheckActivity.this, Signup_complete_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }

                } else {
                    Toast.makeText(TypeCheckActivity.this, "채식 단계를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
