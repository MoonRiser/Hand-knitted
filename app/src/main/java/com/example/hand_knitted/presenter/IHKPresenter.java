package com.example.hand_knitted.presenter;

import com.example.hand_knitted.bean.Comment;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;

import java.util.List;

public interface IHKPresenter {

    void request(String keyword,Boolean isSnap);//请求所有的作品
    void setFavoritePost(Post post);//设置收藏帖子
    void cancelFavoritePost(Post post);//取消收藏
    void deletePost(Post post);//删除选中的帖子
    void addPost(Post post);//发表新的帖子
    void addComment(Comment comment);//发表新的评论
    void inqueryPost();//查询本用户所有的发帖，可以不带参数
    void updatePost(Post post);//更新用户对帖子做的更改
    List<String> inqueryLikePost();



    void requestSuccess(List<Work> result);
    void inquerySuccess(List<Post> posts);
    void requestFail(String info);
    void updateResult(String info);






}
