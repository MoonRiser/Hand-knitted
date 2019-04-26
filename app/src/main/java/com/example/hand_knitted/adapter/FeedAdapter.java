package com.example.hand_knitted.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.WorkDetailActivity;
import com.example.hand_knitted.bean.Comment;
import com.example.hand_knitted.bean.User;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.HKPresenter;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.util.MyUtils;
import com.example.hand_knitted.view.IHKView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private List<Work> workList;
    private Context context;
    private IHKPresenter presenter;
    private Work currentWork;
    private List<String> ids;
    private Boolean isClicked = true;


    public FeedAdapter(IHKPresenter presenter) {
        this.presenter = presenter;
    }

    public void setWorkList(List<Work> workList) {
        this.workList = workList;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AvatarImageView avatar;
        TextView title;
        TextView tool;
        TextView group;
        TextView style;
        ImageView img;
        TextView comment;
        ToggleButton likes;
        Button commentBT;
        Button share;

        ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.IMGavatar);
            title = view.findViewById(R.id.TVtitle);
            tool = view.findViewById(R.id.TVtool);
            group = view.findViewById(R.id.TVgroup);
            style = view.findViewById(R.id.TVstyle);
            img = view.findViewById(R.id.IMGwork);
            comment = view.findViewById(R.id.TVcomment);
            likes = view.findViewById(R.id.BTfavorite);
            commentBT = view.findViewById(R.id.BTcomment);
            share = view.findViewById(R.id.BTshare);

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();

        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_work, parent, false);
        final ViewHolder holder = new ViewHolder(view);
         //currentWork = workList.get(holder.getLayoutPosition());



        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Work work = workList.get(position);
        currentWork= work;
        ViewHolder viewHolder = (ViewHolder) holder;
        //viewHolder.likes.setOnCheckedChangeListener(this);
        String name = work.getPost().getAuthor().getUsername();
        viewHolder.avatar.setTextAndColorSeed(name.substring(0, 1), name);
        viewHolder.title.setText(work.getPost().getTitle());
        viewHolder.tool.setText(MyUtils.tool[Integer.parseInt(work.getPost().getTool()) - 1]);
        viewHolder.group.setText(MyUtils.group[Integer.parseInt(work.getPost().getGroup()) - 1]);
        viewHolder.style.setText(MyUtils.style[Integer.parseInt(work.getPost().getStyle()) - 1]);
        Glide.with(context).load(work.getPost().getImage().getFileUrl()).into(viewHolder.img);
        viewHolder.comment.setText(commentHelper(work));
        viewHolder.img.setOnClickListener(v -> jumpAnotherActivity(WorkDetailActivity.class));
        viewHolder.commentBT.setOnClickListener(v -> {

        });
        viewHolder.likes.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isClicked) {
                if (isChecked) {
                    presenter.setFavoritePost(currentWork.getPost());
                    Log.i("点击收藏","已经被回调");
                } else {
                    presenter.cancelFavoritePost(currentWork.getPost());
                    Log.i("点击取消", "已经被回调");
                }
            } else {
                isClicked = true;
            }
        });

        //如果当前帖子已经被收藏，则显示出来
        if (ids.size() != 0) {


        //    Log.i("当前position为：",position+"/对应的postID为："+currentWork.getPost().getObjectId());
          //  Log.i("ids的size是多少呢？","是："+ids.size());
                //    Log.i("Like表返回的postid打印", id + "/当前的id：" + currentWork.getPost().getObjectId());
                if (ids.contains(currentWork.getPost().getObjectId())) {
                    isClicked = false;
                 //   Log.i("Like表返回的postid打印", "当前的position"+position+"/"+id + "/当前的id：" + currentWork.getPost().getObjectId());
                    viewHolder.likes.setChecked(true);
                }else {
                    isClicked=false;
                    viewHolder.likes.setChecked(false);
                }


        }


    }

    @Override
    public int getItemCount() {
        return workList.size();
    }




    private void jumpAnotherActivity(Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }


    private String commentHelper(Work work) {

        StringBuilder str = new StringBuilder("\n");
        String item;
        List<Comment> comments = work.getComments();

        for (Comment comment : comments) {
            item = comment.getAuthor().getUsername() + ": " + comment.getContent() + "\n";
            str.append(item);
        }
        return str.toString();

    }


}
