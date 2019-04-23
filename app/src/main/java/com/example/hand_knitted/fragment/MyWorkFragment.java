package com.example.hand_knitted.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.EditPostActivity;
import com.example.hand_knitted.adapter.MyWorkAdapter;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.HKPresenter;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.view.IHKView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyWorkFragment extends Fragment implements IHKView, View.OnClickListener {


    @BindView(R.id.RVmywork)
    public RecyclerView recyclerView;
    @BindView(R.id.BTrefresh)
    public Button refresh;
    @BindView(R.id.FABadd)
    public FloatingActionButton fab;

    private IHKPresenter presenter;
    private ProgressBar progressBar;
    private View view;
    private Unbinder unbinder;
    private MyWorkAdapter myWorkAdapter;
    private Toast toast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_mywork, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = view.findViewById(R.id.PB2);
        presenter = new HKPresenter(this);
        presenter.inqueryPost();
        recyclerView.setLayoutManager(new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        refresh.setOnClickListener(this);
        fab.setOnClickListener(this);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void showProgress(Boolean show) {

        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void showPostData(List<Post> posts) {

        myWorkAdapter = new MyWorkAdapter(posts);
        recyclerView.setAdapter(myWorkAdapter);
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
    public void showWorkData(List<Work> list) {
        //空着/此View中用不上
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTrefresh: presenter.inqueryPost();
                break;

            case R.id.FABadd:
                Intent intent = new Intent(getActivity(), EditPostActivity.class);
                startActivity(intent);
                break;
        }

    }
}
