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
    public void inqueryPost() {
        view.showProgress(true);
        model.inqueryPost();
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
    public void deletePost(Post post) {
        model.deletePost(post);
    }

    @Override
    public void addPost(Post post) {
        model.addPost(post);
    }


    @Override
    public void updatePost(Post post) {
        model.updatePost(post);
    }

    @Override
    public void inquerySuccess(List<Post> posts) {
        view.showProgress(false);
        view.showPostData(posts);
    }

    @Override
    public void requestSuccess(List<Work> result) {
        view.showProgress(false);
        view.showWorkData(result);
    }

    @Override
    public void requestFail(String info) {
        view.showProgress(false);
        view.showResultToast(info);

    }


    @Override
    public void updateResult(String info) {
        view.showResultToast(info);
    }
}
