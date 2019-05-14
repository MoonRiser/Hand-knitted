package com.example.hand_knitted.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hand_knitted.R;
import com.example.hand_knitted.adapter.FeedAdapter;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.HKPresenter;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.view.IHKView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;


public class FeedFragment extends Fragment implements IHKView {


    @BindView(R.id.feed)
    public RecyclerView recyclerView;
    @BindView(R.id.swipe)
    public SwipeRefreshLayout refreshLayout;


    private IHKPresenter hkPresenter;
    private Unbinder unbinder;
    private FeedAdapter feedAdapter;

    private int tool;
    private int group;
    private int style;
    private Toast toast;

    private Boolean isFirstTime = true;
    private Boolean isSnap = false;

    //tool spinner监听
    @OnItemSelected(R.id.spinner1)
    void onToolItemSelected(int position) {
        Log.i("spinner点击监听回调已执行tool：", String.valueOf(position));
        tool = position;
        refreshData();
    }


    //group spinner监听
    @OnItemSelected(R.id.spinner2)
    void onGroupItemSelected(int position) {
        group = position;
        refreshData();

    }


    //style spinner监听
    @OnItemSelected(R.id.spinner3)
    void onStyleItemSelected(int position) {
        style = position;
        refreshData();
    }

    public FeedFragment() {
        hkPresenter = new HKPresenter(this);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        feedAdapter = new FeedAdapter(hkPresenter);
        List<String> list = hkPresenter.inqueryLikePost();
        feedAdapter.setIds(list);
        hkPresenter.request("0.0.0", isSnap);//表示全选
        refreshLayout.setOnRefreshListener(() -> refreshData());
        recyclerView.setLayoutManager(new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


    }


    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void refreshData() {
        String keyword = tool + "." + group + "." + style;
        hkPresenter.request(keyword, isSnap);
    }


    @Override
    public void showWorkData(List<Work> list) {

        feedAdapter.setWorkList(list);
        if (isFirstTime) {
            recyclerView.setAdapter(feedAdapter);
            isFirstTime = false;
        } else {
            feedAdapter.notifyDataSetChanged();
        }
        recyclerView.scrollToPosition(0);

    }

    @Override
    public void showProgress(Boolean show) {
        refreshLayout.setRefreshing(show);
    }

    @Override
    public void showResultToast(String info) {

        if (toast == null) {
            toast = Toast.makeText(this.getActivity(), info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }

    @Override
    public void showPostData(List<Post> posts) {
        //空着/此View中用不上
    }

    public void setSnap(Boolean snap) {
        isSnap = snap;

    }
}
