package com.example.hand_knitted.view;
import com.example.hand_knitted.bean.Work;

import java.util.List;

public interface IHKView {
    void showData(List<Work> list);
    void showProgress(Boolean show);
    void showFailInfo(String info);
    void showSuccessInfo(String info);

}
