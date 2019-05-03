package com.example.hand_knitted.fragment;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;
import com.example.hand_knitted.adapter.MyWorkAdapter;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.User;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.HKPresenter;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.view.IHKView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;


public class MyWorkFragment extends Fragment implements IHKView {


    @BindView(R.id.RVmywork)
    public RecyclerView recyclerView;

    @BindView(R.id.BTrefresh)
    public Button refresh;

    @BindView(R.id.TVme)
    public TextView me;

    @BindView(R.id.IMGsex)
    public ImageView sex;


    private IHKPresenter presenter;
    private ProgressBar progressBar;
    private View view;
    private Unbinder unbinder;
    private MyWorkAdapter myWorkAdapter;
    private MyWorkAdapter myWorkAdaptersnap;
    private Toast toast;
    //  private Boolean iSFirstTime = true;
    private Boolean isSnap = false;

    public MyWorkFragment() {
        presenter = new HKPresenter(this);
    }

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

        myWorkAdapter = new MyWorkAdapter(presenter);
        myWorkAdaptersnap = new MyWorkAdapter(presenter);
        myWorkAdapter.setSnap(false);
        myWorkAdaptersnap.setSnap(true);
        presenter.inqueryPost(isSnap);
        recyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        refresh.setOnClickListener(v -> reFreshData());

        User myself = BmobUser.getCurrentUser(User.class);
        me.setText(myself.getUsername());
        if ("male".equals(myself.getSex())) {
            Glide.with(getActivity()).load(R.drawable.ic_male).into(sex);
        } else {
            Glide.with(getActivity()).load(R.drawable.ic_female).into(sex);
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void reFreshData() {
        presenter.inqueryPost(isSnap);
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

        if (isSnap) {
            Log.i("随拍回调成功，size多少？", posts.size() + "");
            myWorkAdaptersnap.setPosts(posts);
            //   recyclerView.swapAdapter(mySnapAdapter,true);
            recyclerView.setAdapter(myWorkAdaptersnap);
            myWorkAdaptersnap.notifyDataSetChanged();
        } else {
            Log.i("作品回调成功，size多少？", posts.size() + "");
            myWorkAdapter.setPosts(posts);
            //     recyclerView.swapAdapter(myWorkAdapter,true);
            recyclerView.setAdapter(myWorkAdapter);
            myWorkAdapter.notifyDataSetChanged();
        }
        recyclerView.scrollToPosition(0);


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



    public void setSnap(Boolean snap) {
        isSnap = snap;
    }
}
