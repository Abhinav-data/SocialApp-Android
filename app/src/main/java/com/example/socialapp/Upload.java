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

    //    public Upload(String name, String imageUrl, String userId) {
//        if (name.trim().equals("")) {
//            name = "No Name";
//        }
//
//        mName = name;
//        mImageUrl = imageUrl;
//        mUid=userId;
//    }
//    public String getUid() {
//        return mUid;
//    }
//
//    public void setUid(String mUid) {
//        this.mUid = "Hello";
//    }
//    public String getName() {
//        return mName;
//    }
//
//    public void setName(String name) {
//        mName = name;
//    }
//
//    public String getImageUrl() {
//        return mImageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        mImageUrl = imageUrl;
//    }
}
