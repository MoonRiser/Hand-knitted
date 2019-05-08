package com.example.hand_knitted.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

public class Post extends BmobObject {

    private Boolean isSnap;//
    private String tool;//编织工具
    private String group;//适用人群
    private String style;//款式
    private String title;//帖子的标题
    private BmobFile image;//帖子的配图
    private BmobFile image2;
    private BmobFile image3;
    private String content;//帖子的内容
  //  private BmobRelation likes;//点赞喜欢
    private User author;//帖子的作者
    private String date;//发帖日期，这里选用字符串类型，省事

    public Boolean getSnap() {
        return isSnap;
    }

    public void setSnap(Boolean snap) {
        isSnap = snap;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BmobFile getImage2() {
        return image2;
    }

    public void setImage2(BmobFile image2) {
        this.image2 = image2;
    }

    public BmobFile getImage3() {
        return image3;
    }

    public void setImage3(BmobFile image3) {
        this.image3 = image3;
    }
}
