package com.example.publictalk;

public class PostModel {

    // variables for storing our image and name.
    private String userPublicName;
    private String userPhoto;

    private String topicName;
    private String postHead;
    private String postDetail;

    public PostModel(String userPublicName, String userPhoto, String topicName, String postHead, String postDetail) {
        this.userPublicName = userPublicName;
        this.userPhoto = userPhoto;
        this.topicName = topicName;
        this.postHead = postHead;
        this.postDetail = postDetail;
    }

    public PostModel() {
    }

    public String getUserPublicName() {
        return userPublicName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getPostHead() {
        return postHead;
    }

    public String getPostDetail() {
        return postDetail;
    }

    public void setUserPublicName(String userPublicName) {
        this.userPublicName = userPublicName;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setPostHead(String postHead) {
        this.postHead = postHead;
    }

    public void setPostDetail(String postDetail) {
        this.postDetail = postDetail;
    }
}
