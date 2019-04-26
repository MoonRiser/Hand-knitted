package com.example.hand_knitted.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.EditPostActivity;
import com.example.hand_knitted.adapter.MyWorkAdapter;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.User;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.HKPresenter;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.view.IHKView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;


public class MyWorkFragment extends Fragment implements IHKView, View.OnClickListener {


    @BindView(R.id.RVmywork)
    public RecyclerView recyclerView;

    @BindView(R.id.BTrefresh)
    public Button refresh;

    @BindView(R.id.TVme)
    public TextView me;

    @BindView(R.id.IMGsex)
    public ImageView sex;

    @BindView(R.id.FABadd)
    public FloatingActionButton fab;

    private IHKPresenter presenter;
    private ProgressBar progressBar;
    private View view;
    private Unbinder unbinder;
    private MyWorkAdapter myWorkAdapter;
    private Toast toast;
    private Boolean iSFirstTime = true;

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
        myWorkAdapter = new MyWorkAdapter();
        myWorkAdapter.setPresenter(presenter);
        presenter.inqueryPost();
        recyclerView.setLayoutManager(new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        refresh.setOnClickListener(this);
        fab.setOnClickListener(this);

        User myself = BmobUser.getCurrentUser(User.class);
        me.setText(myself.getUsername());
        if("male".equals(myself.getSex())){
            Glide.with(getActivity()).load(R.drawable.ic_male).into(sex);
        }else {
            Glide.with(getActivity()).load(R.drawable.ic_female).into(sex);
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public IHKPresenter getPresenter() {
        return presenter;
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

        myWorkAdapter.setPosts(posts);
        if(iSFirstTime){
            recyclerView.setAdapter(myWorkAdapter);
            iSFirstTime = false;
        }
        myWorkAdapter.notifyDataSetChanged();

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
                intent.putExtra("isADD",true);//发表新帖子和更新旧帖子用同一个EditPostActivity，所以用这个标志区分一下
                startActivity(intent);
                break;
        }

    }
}
