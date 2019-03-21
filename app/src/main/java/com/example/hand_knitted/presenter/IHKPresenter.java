package com.example.hand_knitted.presenter;

import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;

import java.util.List;

public interface IHKPresenter {

    void request(String keyword,Boolean isSnap);
    void requestSuccess(List<Work> result);
    void requestFail(String info);
}
