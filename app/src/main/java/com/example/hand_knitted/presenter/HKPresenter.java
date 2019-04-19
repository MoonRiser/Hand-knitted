package com.example.hand_knitted.presenter;


import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.model.HKModel;
import com.example.hand_knitted.model.IHKModel;
import com.example.hand_knitted.view.IHKView;

import java.util.List;

public class HKPresenter implements IHKPresenter {

    private IHKModel model;



    private IHKView view;


    public HKPresenter(IHKView view) {
        this.view = view;
        model = new HKModel(this);
    }

    @Override
    public void request(String keyword,Boolean isSnap) {
        view.showProgress(true);
        model.requestData(keyword,isSnap);
    }


    @Override
    public void setFavoritePost(Post post) {
        model.setFavorite(post);
    }


    @Override
    public void cancelFavoritePost(Post post) {
        model.cancelFavorite(post);
    }

    @Override
    public void requestSuccess(List<Work> result) {
        view.showProgress(false);
        view.showData(result);
    }

    @Override
    public void requestFail(String info) {
        view.showProgress(false);
        view.showFailInfo(info);

    }


    @Override
    public void updateSuccess(String info) {
        view.showSuccessInfo(info);
    }

    @Override
    public void updateFail(String info) {
        view.showFailInfo(info);
    }
}
