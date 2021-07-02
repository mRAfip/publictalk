package com.example.publictalk;

public class SliderClass {

    // string for our image url.
    private String imgUrl;

    public SliderClass(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public SliderClass() {

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
