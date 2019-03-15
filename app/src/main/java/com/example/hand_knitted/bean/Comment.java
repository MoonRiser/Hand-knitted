package com.example.hand_knitted.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;

public class Comment extends BmobObject {

    private String content;//评论内容
    private BmobPointer post;//评论对应的帖子
    private BmobPointer author;//发评论的人


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobPointer getPost() {
        return post;
    }

    public void setPost(BmobPointer post) {
        this.post = post;
    }

    public BmobPointer getAuthor() {
        return author;
    }

    public void setAuthor(BmobPointer author) {
        this.author = author;
    }
}
