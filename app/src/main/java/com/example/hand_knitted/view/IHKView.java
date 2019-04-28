package com.example.hand_knitted.view;

import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;

import java.util.List;

public interface IHKView {
    void showWorkData(List<Work> list);
    void showPostData(List<Post> posts);
    void showProgress(Boolean show);
    void showResultToast(String info);


}
