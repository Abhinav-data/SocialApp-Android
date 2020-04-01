package com.example.socialapp;

public class Upload {
    private String name;
    private String imageUrl;
    private String userId;

    public Upload() {
        //Needed
    }

    public Upload(String name,String imageUrl,String userId){
        this.name=name;
        this.imageUrl=imageUrl;
        this.userId=userId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserId() {
        return userId;
    }
}
