package com.example.hand_knitted.model;

import android.util.Log;

import com.example.hand_knitted.bean.Comment;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.User;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.IHKPresenter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class HKModel implements IHKModel {


    private IHKPresenter presenter;
    //private List<Work> works = new ArrayList<>();
    private User currentUser = BmobUser.getCurrentUser(User.class);

    public HKModel(IHKPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void requestData(String keyword, Boolean isSnap) {

        BmobQuery<Post> query = new BmobQuery<>();

        String[] str = keyword.split("\\.");//传入的keyword形式必须如 3.1.5  ;分裂成 3(工具：钩织结合)，1(适用：儿童)，5(款式：叶子花);0表示全部all


        if (!"0".equals(str[0])) {
            query.addWhereEqualTo("tool", str[0]);
        }


        if (!"0".equals(str[1])) {
            query.addWhereEqualTo("group", str[1]);
        }


        if (!"0".equals(str[2])) {
            query.addWhereEqualTo("style", str[2]);
        }

        Log.i("传入的keyword是否正确", str[0] + "/" + str[1] + "/" + str[2]);

        query.addWhereEqualTo("isSnap", isSnap);//判断查询帖子还是随拍
        query.include("author");
        query.setLimit(500);
        query.order("createdAt"); //排序
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {

                if (e == null) {


                    if (list == null) {
                        presenter.requestFail("没有查询到数据");
                        return;
                    }

                    prepareData(list);


                } else {
                    presenter.requestFail("bmob 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }


    @Override
    public void deletePost(Post post) {

        post.delete(post.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    presenter.updateResult("删除帖子成功");
                } else {
                    presenter.updateResult("删除帖子失败" + e.getErrorCode() + e.getErrorCode());
                }
            }
        });

        BmobQuery<Comment> queryComment = new BmobQuery<>();
        queryComment.addWhereEqualTo("post", post.getAuthor());
        queryComment.setLimit(500);
        queryComment.include("author");
        queryComment.order("createdAt"); //排序
        queryComment.findObjects(new FindListener<Comment>() {
                                     @Override
                                     public void done(List<Comment> list, BmobException e) {
                                         deleteBatchCommment(list);
                                     }
                                 }

        );


    }

    @Override
    public void addPost(Post post) {


        //添加一对一关联，用户关联帖子

        post.setAuthor(BmobUser.getCurrentUser(User.class));
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    presenter.updateResult("发帖成功");
                } else {
                    presenter.updateResult("发帖失败" + e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    @Override
    public void inqueryPost() {
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("author", currentUser);
        query.order("-updatedAt");
        //包含作者信息
        query.include("author");
        query.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> posts, BmobException e) {
                if (e == null) {
                    presenter.inquerySuccess(posts);
                } else {
                    presenter.requestFail("查询当前用户帖子失败");
                    Log.i("查询本人作品出错", e.getMessage() + e.getErrorCode());
                }
            }

        });
    }

    @Override
    public void updatePost(Post post) {

        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    presenter.updateResult("帖子更新成功");
                } else {
                    presenter.updateResult("帖子更新失败：" + e.getMessage() + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void setFavorite(Post post) {

        User user = BmobUser.getCurrentUser(User.class);

        BmobRelation relation = new BmobRelation();
//将当前用户添加到多对多关联中
        relation.add(user);
//多对多关联指向`post`的`likes`字段
        post.setLikes(relation);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //Log.i("bmob","多对多关联添加成功");
                    presenter.updateResult("帖子收藏成功");

                } else {
                    //Log.i("bmob","失败："+e.getMessage());
                    presenter.updateResult("帖子收藏失败" + e.getMessage());
                }
            }

        });

    }

    @Override
    public void cancelFavorite(Post post) {

        User user = BmobUser.getCurrentUser(User.class);

        BmobRelation relation = new BmobRelation();
//将当前用户从多对多关联中删除
        relation.remove(user);
//多对多关联指向`post`的`likes`字段
        post.setLikes(relation);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //Log.i("bmob","多对多关联添加成功");
                    presenter.updateResult("取消收藏成功");

                } else {
                    //Log.i("bmob","失败："+e.getMessage());
                    presenter.updateResult("取消收藏失败" + e.getMessage());
                }
            }

        });
    }

    //将Post和comment构造成Work类型
    private void prepareData(List<Post> posts) {

        BmobQuery<Comment> queryComment = new BmobQuery<>();
        List<Work> works= new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            queryComment.addWhereEqualTo("post", post);
            queryComment.setLimit(500);
            queryComment.include("author");
            queryComment.order("createdAt"); //排序
            queryComment.findObjects(new FindListener<Comment>() {
                @Override
                public void done(List<Comment> list, BmobException e) {
                    if (e == null) {
                        works.add(new Work(post, list));
                        if (works.size() == posts.size())//等数据都准备好再回调
                            presenter.requestSuccess(works);
                    } else {
                        presenter.requestFail("bmob评论部分失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }


    }

    //批量删除
    private void deleteBatchCommment(List<Comment> comments) {

        List<BmobObject> bmobObjects = new ArrayList<>(comments);

        new BmobBatch().deleteBatch(bmobObjects).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> results, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < results.size(); i++) {
                        BatchResult result = results.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            presenter.updateResult("第" + i + "个评论批量删除成功：");
                        } else {
                            presenter.updateResult("第" + i + "个评论批量删除失败：" + ex.getMessage() + "," + ex.getErrorCode());

                        }
                    }
                } else {

                    presenter.updateResult("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


}
