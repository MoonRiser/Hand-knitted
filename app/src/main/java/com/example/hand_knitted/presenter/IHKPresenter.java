package com.example.hand_knitted.presenter;

import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;

import java.util.List;

public interface IHKPresenter {

    void request(String keyword,Boolean isSnap);
    void setFavoritePost(Post post);
    void cancelFavoritePost(Post post);

    void requestSuccess(List<Work> result);
    void requestFail(String info);
    void updateSuccess(String info);
    void updateFail(String info);


}
