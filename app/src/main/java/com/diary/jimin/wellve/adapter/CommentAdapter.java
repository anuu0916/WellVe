package com.diary.jimin.wellve.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.jimin.wellve.R;
import com.diary.jimin.wellve.model.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends BaseAdapter {

    private FirebaseFirestore db;
    private FirebaseUser user;
    private ImageButton deleteButton;
    private String deleteStr;
    private String name;
    private String commentId;

//    public TextView titleTextView;
//    public TextView textView;
//    public TextView timeTextView;


    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
//    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;
    private ArrayList<PostInfo> listViewItemList = new ArrayList<PostInfo>() ;
    private TableLayout listItemLayout;



    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position : 리턴 할 자식 뷰의 위치
    // convertView : 메소드 호출 시 position에 위치하는 자식 뷰 ( if == null 자식뷰 생성 )
    // parent : 리턴할 부모 뷰, 어댑터 뷰
    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();
        final CommentViewHolder viewHolder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_comment_item, parent, false);
            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            viewHolder = new CommentViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.userID) ;
            viewHolder.textView = (TextView) convertView.findViewById(R.id.context) ;
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.userTime);
            viewHolder. deleteButton = (ImageButton)convertView.findViewById(R.id.commentDeleteButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommentViewHolder) convertView.getTag();
        }



        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        PostInfo postInfo = listViewItemList.get(pos);
        //현재 사용자 nickName 받아와서 댓글 쓸 때 반영되게

//         아이템 내 각 위젯에 데이터 반영 ( Test 중 )
        viewHolder.textView.setText(postInfo.getText());
        viewHolder.titleTextView.setText(postInfo.getName());
        viewHolder.timeTextView.setText(postInfo.getTime());


        db = FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();



        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference1 = db.collection("comments");
                collectionReference1
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        if (documentSnapshot.get("text").equals(viewHolder.textView.getText().toString()) && documentSnapshot.get("time").equals(viewHolder.timeTextView.getText().toString()) ) {
                                                Log.d("CommentActivity",documentSnapshot.getData().toString());
//                                                Log.d("CommentActivity",documentSnapshot.getId());
                                                deleteStr = documentSnapshot.getId();
                                                commentId = documentSnapshot.getData().get("id").toString();

                                            if(user.getUid().equals(commentId)) {
                                                Snackbar.make(view,"댓글 삭제",Snackbar.LENGTH_SHORT).setAction("OK",new View.OnClickListener(){
                                                    @Override
                                                    public void onClick(View view) {

                                                        if(deleteStr != null) {
                                                            db.collection("comments").document(deleteStr)
                                                                    .delete()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>(){
                                                                        @Override
                                                                        public void onSuccess(Void aVoid){
                                                                            Toast.makeText(view.getContext(), "댓글 삭제 완료", Toast.LENGTH_SHORT).show();
                                                                            listViewItemList.remove(pos);
                                                                            notifyDataSetChanged();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(view.getContext(), "댓글 삭제 실패", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                }).show();
                                            }

                                        }
                                    }
                                }
                            }
                        });

            }
        });


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수.

    public void addItem(String text, String id, String time, String name) {
        PostInfo item = new PostInfo(text, id, time, name);

        item.setText(text);
        item.setId(id);
        item.setTime(time);
        item.setName(name);

        listViewItemList.add(item);
    }

    static class CommentViewHolder {
        public TextView titleTextView;
        public TextView textView;
        public TextView timeTextView;
        public ImageButton deleteButton;

    }

}
