package com.example.hand_knitted.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.WorkDetailActivity;
import com.example.hand_knitted.bean.Work;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

import static android.content.Context.VIBRATOR_SERVICE;

public class MyWorkAdapter extends RecyclerView.Adapter implements View.OnClickListener , View.OnLongClickListener {

    private List<Work> workList;
    private Context context;
    private int position;
    private ViewGroup parent;



    public MyWorkAdapter(List<Work> workList) {
        this.workList = workList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        AvatarImageView avatar;
        TextView title;
        TextView tool;
        TextView group;
        TextView style;
        ImageView img;
        CardView cardView;


        ViewHolder(View view){
            super(view);
            avatar = view.findViewById(R.id.IMGavatar);
            title = view.findViewById(R.id.TVtitle);
            tool = view.findViewById(R.id.TVtool);
            group = view.findViewById(R.id.TVgroup);
            style = view.findViewById(R.id.TVstyle);
            img = view.findViewById(R.id.IMGwork);
            cardView = (CardView) view;

        }
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(context==null){
            context = parent.getContext();
        }

        this.parent = parent;
        View view = LayoutInflater.from(context).inflate(R.layout.item_mywork,parent,false);
        //final FeedAdapter.ViewHolder holder = new FeedAdapter.ViewHolder(view);
        final MyWorkAdapter.ViewHolder holder = new MyWorkAdapter.ViewHolder(view);
        holder.img.setOnClickListener(this);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        this.position = position;
        Work work = workList.get(position);
        //FeedAdapter.ViewHolder viewHolder = (FeedAdapter.ViewHolder)holder;
        MyWorkAdapter.ViewHolder viewHolder = (MyWorkAdapter.ViewHolder)holder;
        String name = work.getPost().getAuthor().getUsername();
        viewHolder.cardView.setOnLongClickListener(this);
        viewHolder.avatar.setTextAndColorSeed(name.substring(0,1),name);
        viewHolder.title.setText(work.getPost().getTitle());
        viewHolder.tool.setText(work.getPost().getTool());
        viewHolder.group.setText(work.getPost().getGroup());
        viewHolder.style.setText(work.getPost().getStyle());
        Glide.with(context).load(work.getPost().getImage()).into(viewHolder.img);




    }

    @Override
    public int getItemCount() {
        return workList.size();
    }




    //likes，comment，share按钮的点击事件
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


    @Override
    public boolean onLongClick(View v) {


        //长按触发振动效果
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        assert vibrator != null;
        //vibrator.vibrate(50);
        vibrator.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE));

        Snackbar.make(parent, "确定删除当前内容吗?", Snackbar.LENGTH_LONG).setAction("确定", v1 -> {
            workList.remove(position);
            notifyItemRemoved(position);
        }).show();

        return true;

    }




    private void jumpAnotherActivity(Class cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }



}
