package com.example.hand_knitted.bean;

import java.util.List;

public class Work {


    private Post post;
    private List<Comment> comments;


    public Work(Post post, List<Comment> comments) {
       // post.setSnap(false);
        this.post = post;
        this.comments = comments;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
