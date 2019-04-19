package com.example.hand_knitted.model;

import com.example.hand_knitted.bean.Post;

public interface IHKModel {

    void requestData(String keyword,Boolean isSnap);
    void setFavorite(Post post);
    void cancelFavorite(Post post);
}


