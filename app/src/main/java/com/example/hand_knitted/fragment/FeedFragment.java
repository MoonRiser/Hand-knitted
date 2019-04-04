package com.example.hand_knitted.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.hand_knitted.R;
import com.example.hand_knitted.bean.Work;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class FeedFragment extends Fragment {




    @BindView(R.id.feed)
    public RecyclerView recyclerView;

    private List<Work> works;
    private Unbinder unbinder;


    @OnItemSelected(R.id.spinner1)//tool spinner监听
    void onToolItemSelected(int position) {
       //回调对应的方法
    }

    @OnItemSelected(R.id.spinner2)//group spinner监听
    void onGroupItemSelected(int position) {
        //回调对应的方法
    }

    @OnItemSelected(R.id.spinner3)//style spinner监听
    void onStyleItemSelected(int position) {
        //回调对应的方法
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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


}
