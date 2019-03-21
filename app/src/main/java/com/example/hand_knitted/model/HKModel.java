package com.example.hand_knitted.model;

import com.example.hand_knitted.bean.Comment;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.IHKPresenter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HKModel extends FindListener<Post> implements IHKModel {


    private IHKPresenter presenter;
    private List<Work> works = new ArrayList<>();
    private Post post;

    public HKModel(IHKPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void requestData(String keyword, Boolean isSnap) {

        BmobQuery<Post> query = new BmobQuery<>();

        String str[] = keyword.split("\\.");//传入的keyword形式必须如 tool.2 ，分割成tool（工具） 和 2（编织）
        String key = str[0];//数据库表的列
        String option = str[1];//列对应的值

        switch (key) {
            case "tool":
                query.addWhereEqualTo("tool", option);
                break;
            case "group":
                query.addWhereEqualTo("group", option);
                break;
            case "style":
                query.addWhereEqualTo("style", option);
                break;
        }
        query.addWhereEqualTo("isSnap", isSnap);//判断查询帖子还是随拍
        query.setLimit(500);
        query.order("createdAt"); //排序
        query.findObjects(this);


    }

    private void prepareData(List<Post> list) {

        BmobQuery<Comment> queryComment = new BmobQuery<>();


        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                post = list.get(i);
                queryComment.addWhereEqualTo("post", post.getAuthor());
                queryComment.setLimit(500);
                queryComment.order("createdAt"); //排序
                queryComment.findObjects(new FindListener<Comment>() {
                    @Override
                    public void done(List<Comment> list, BmobException e) {
                        if (e != null) {
                            works.add(new Work(post, list));
                        } else {
                            presenter.requestFail("bmob评论失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }


        }
    }


    @Override
    public void done(List list, BmobException e) {

        if (e == null) {
            prepareData(list);
            presenter.requestSuccess(works);
        } else {
            presenter.requestFail("bmob 失败：" + e.getMessage() + "," + e.getErrorCode());
        }
    }
}
