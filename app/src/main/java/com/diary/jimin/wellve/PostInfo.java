package com.diary.jimin.wellve;

public class PostInfo {

    private String title;
    private String text;
    private String id;
    private String time;
    private String name;
    private String postId;
    private String category;

    public PostInfo(String title, String text, String id, String time, String name) {
        this.title = title; //게시글 제목
        this.text = text;   //게시글 내용
        this.id = id;       //사용자 uid
        this.time = time;   //쓴 시간
        this.name = name;   //사용자 닉네임
    }

    public PostInfo(String text, String id, String time, String name, String category, String postId) {
        this.text = text;   //코멘트
        this.id = id;       //사용자 uid
        this.time = time;   //쓴 시간
        this.name = name;   //사용자 닉네임
        this.category = category; //카테고리
        this.postId = postId; //게시글 uid
    }

    public PostInfo(String text, String id, String time, String name) {
        this.text = text;   //코멘트
        this.id = id;       //사용자 uid
        this.time = time;   //쓴 시간
        this.name = name;   //사용자 닉네임
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



}
