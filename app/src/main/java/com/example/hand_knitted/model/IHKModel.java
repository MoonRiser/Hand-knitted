package com.example.hand_knitted.model;

import com.example.hand_knitted.bean.Post;

public interface IHKModel {

    void requestData(String keyword,Boolean isSnap);
    void setFavorite(Post post);
    void cancelFavorite(Post post);
    void deletePost(Post post);
    void addPost(Post post);
    void inqueryPost();
    void updatePost(Post post);

}


