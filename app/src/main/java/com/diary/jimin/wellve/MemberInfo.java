package com.diary.jimin.wellve;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class MemberInfo {

    private String nickname;
    private String type;
    private String profileImageUrl;



    public MemberInfo(String nickname, String type, String profileImageUrl) {
        this.nickname = nickname;
        this.type = type;
        this.profileImageUrl = profileImageUrl;
    }


    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }



}
