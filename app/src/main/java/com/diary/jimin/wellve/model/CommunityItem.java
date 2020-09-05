package com.diary.jimin.wellve.model;


public class CommunityItem {

    public String name;
    public String photo;
    public String title;
    public String time;
    public String category;
    public String comment;


    public CommunityItem(String name, String photo, String title, String time, String category, String comment) {
        this.name = name;
        this.photo = photo;
        this.title = title;
        this.time = time;
        this.category = category;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }


    public String getComment() {
        return comment;
    }





}
