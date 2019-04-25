package com.example.hand_knitted.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.EditPostActivity;
import com.example.hand_knitted.activity.WorkDetailActivity;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.util.MyUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

import static android.content.Context.VIBRATOR_SERVICE;

public class MyWorkAdapter extends RecyclerView.Adapter implements View.OnClickListener , View.OnLongClickListener {


    private List<Post> posts;
    private Context context;
    private int position;
    private ViewGroup parent;



    public void setPosts(List<Post> posts) {
        this.posts = posts;
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
      //  position = holder.getAdapterPosition();
       // holder.img.setOnClickListener(this);
      //  holder.cardView.setOnLongClickListener(this);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

       // this.position = position;
        Post post = posts.get(position);
        //FeedAdapter.ViewHolder viewHolder = (FeedAdapter.ViewHolder)holder;
        MyWorkAdapter.ViewHolder viewHolder = (MyWorkAdapter.ViewHolder)holder;
        this.position =position;
        viewHolder.img.setOnClickListener(this);
        viewHolder.cardView.setOnLongClickListener(this);
        String name = post.getAuthor().getUsername();

        viewHolder.avatar.setTextAndColorSeed(name.substring(0,1),name);
        viewHolder.title.setText(post.getTitle());
        viewHolder.tool.setText(MyUtils.tool[Integer.parseInt(post.getTool())-1] );
        viewHolder.group.setText(MyUtils.group[Integer.parseInt(post.getGroup())-1]);
        viewHolder.style.setText(MyUtils.style[Integer.parseInt(post.getStyle())-1]);
        Glide.with(context).load(post.getImage().getFileUrl()).into(viewHolder.img);




    }

    @Override
    public int getItemCount() {
        return posts.size();
    }




    //likes，comment，share按钮的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IMGwork: Intent intent = new Intent(context, EditPostActivity.class);
                Log.i("这次被点击的是第几个item呢？：",String.valueOf(position) );
            intent.putExtra("post",posts.get(position));
            context.startActivity(intent);
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
            posts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }).show();

        return true;

    }




    private void jumpAnotherActivity(Class cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }



}
