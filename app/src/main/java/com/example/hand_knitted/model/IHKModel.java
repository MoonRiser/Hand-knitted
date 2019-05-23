package com.example.hand_knitted.model;

import com.example.hand_knitted.bean.Comment;
import com.example.hand_knitted.bean.Post;

import java.util.List;

public interface IHKModel {

    void requestData(String keyword,Boolean isSnap);
    void setFavorite(Post post);
    void cancelFavorite(Post post);
    void addPost(Post post);
    void deletePost(Post post);
    void inqueryPost();
    void updatePost(Post post);

    void addComment(Comment comment);
    void deleteBatchCommment(List<Comment> comments);

    List<String> inqueryLikePost();

}


