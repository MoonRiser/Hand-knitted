package com.example.hand_knitted.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.BaseActivity;
import com.example.hand_knitted.activity.WorkDetailActivity;
import com.example.hand_knitted.bean.Work;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {


    private List<Work> workList;
    private Context context;



    public FeedAdapter(List<Work> workList) {
        this.workList = workList;
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

        ViewHolder(View view){
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

        if(context==null){
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.work_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.img.setOnClickListener(this);
        holder.likes.setOnClickListener(this);
        holder.commentBT.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Work work = workList.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;
       // viewHolder.avatar.setTextAndColor(work.getPost().getAuthor(),R.color.colorAccent);
    }

    @Override
    public int getItemCount() {
        return workList.size();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IMGwork: jumpAnotherActivity(WorkDetailActivity.class);
                break;
            case R.id.BTfavorite:
                break;
            case R.id.BTcomment:
                break;

        }

    }


    private void jumpAnotherActivity(Class cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }



}
