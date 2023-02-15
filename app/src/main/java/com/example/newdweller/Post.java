package com.example.newdweller;

public class Post {
    String title;
    String description;
    String img;
    String userID;


    public Post(){}

    public Post(String title, String description, String img, String userID) {
        this.title = title;
        this.description = description;
        this.img = img;
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getUserID() {
        return userID;
    }
}
