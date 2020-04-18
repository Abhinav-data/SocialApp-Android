package com.example.socialapp;

public class Upload {
    private String name;
    private String imageUrl;
    private String userId;
    private String uploadId;

    public Upload() {
        //Needed
    }

    public String getUploadId() {
        return uploadId;
    }

    public Upload(String name, String imageUrl, String userId, String uploadId){
        this.name=name;
        this.imageUrl=imageUrl;
        this.userId=userId;
        this.uploadId=uploadId;
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
