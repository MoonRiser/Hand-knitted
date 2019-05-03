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
import com.example.hand_knitted.activity.EditSnapActivity;
import com.example.hand_knitted.activity.MainActivity;
import com.example.hand_knitted.activity.WorkDetailActivity;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.fragment.MyWorkFragment;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.util.MyUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import cn.bmob.v3.datatype.BmobFile;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

import static android.content.Context.VIBRATOR_SERVICE;

public class MyWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Post> posts;
    private Context context;
    private IHKPresenter presenter;
    private final int TYPE_POST = 0;
    private final int TYPE_SNAP = 1;


    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }



    public MyWorkAdapter(IHKPresenter presenter) {
        this.presenter = presenter;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AvatarImageView avatar;
        TextView title;
        TextView tool;
        TextView group;
        TextView style;
        ImageView img;
        ImageView img2;
        ImageView img3;
        CardView cardView;
        TextView date;
        TextView time;


        ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.IMGavatar);
            title = view.findViewById(R.id.TVtitle);
            tool = view.findViewById(R.id.TVtool);
            group = view.findViewById(R.id.TVgroup);
            style = view.findViewById(R.id.TVstyle);
            img = view.findViewById(R.id.IMGmy2);
            img2 = view.findViewById(R.id.IMGmy22);
            img3 = view.findViewById(R.id.IMGmy23);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);

            cardView = (CardView) view;

        }
    }

    static class ViewHoldersnap extends RecyclerView.ViewHolder {
        AvatarImageView avatar;
        TextView title;
        ImageView img;
        CardView cardView;
        TextView date;
        TextView time;
        TextView content;


        ViewHoldersnap(View view) {
            super(view);
            avatar = view.findViewById(R.id.IMGavatarms);
            title = view.findViewById(R.id.TVtitlems);
            img = view.findViewById(R.id.IMGms2);
            date = view.findViewById(R.id.datems);
            time = view.findViewById(R.id.timems);
            content = view.findViewById(R.id.TVcontentms);
            cardView = (CardView) view;

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_mywork, parent, false);
        View viewsnap = LayoutInflater.from(context).inflate(R.layout.item_mywork_snap, parent, false);
        //final FeedAdapter.ViewHolder holder = new FeedAdapter.ViewHolder(view);
        final MyWorkAdapter.ViewHolder holder = new MyWorkAdapter.ViewHolder(view);
        final MyWorkAdapter.ViewHoldersnap holderSnap = new MyWorkAdapter.ViewHoldersnap(viewsnap);

        if(viewType==TYPE_POST){
            return holder;
        }else{
            return holderSnap;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Post post = posts.get(position);

        String dateTime = post.getDate();
        String[] str = dateTime.split("\\.");


        if(holder instanceof ViewHoldersnap){
            MyWorkAdapter.ViewHoldersnap viewHolder = (MyWorkAdapter.ViewHoldersnap) holder;
            viewHolder.cardView.setOnClickListener(v -> {

                        Intent intent = new Intent(context, EditSnapActivity.class);
                        intent.putExtra("post", posts.get(position));
                        context.startActivity(intent);
                    }
            );
            viewHolder.cardView.setOnLongClickListener(v -> {

                        //长按触发振动效果
                        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                        assert vibrator != null;
                        //vibrator.vibrate(50);
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        Log.i("position被回调：", position + "");

                        Snackbar.make(((MainActivity) context).fab, "确定删除当前内容吗?", Snackbar.LENGTH_LONG).setAction("确定", v1 -> {

                            presenter.deletePost(posts.get(position));
                            posts.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                        }).show();

                        return true;

                    }
            );
            String name = post.getAuthor().getUsername();

            viewHolder.avatar.setTextAndColorSeed(name.substring(0, 1), name);
            viewHolder.title.setText(post.getTitle());
            viewHolder.date.setText(str[0]);
            viewHolder.time.setText(str[1]);
            viewHolder.content.setText(post.getContent());
            BmobFile bmobFile = post.getImage();
            if (bmobFile.getLocalFile() == null) {
                Glide.with(context).load(bmobFile.getFileUrl()).into(viewHolder.img);
            } else {
                Glide.with(context).load(bmobFile.getLocalFile()).into(viewHolder.img);
            }

        }else{
            MyWorkAdapter.ViewHolder viewHolder = (MyWorkAdapter.ViewHolder) holder;
            viewHolder.cardView.setOnClickListener(v -> {

                        Intent intent = new Intent(context, EditPostActivity.class);
                        intent.putExtra("post", posts.get(position));
                        context.startActivity(intent);
                    }
            );
            viewHolder.cardView.setOnLongClickListener(v -> {

                        //长按触发振动效果
                        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                        assert vibrator != null;
                        //vibrator.vibrate(50);
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        Log.i("position被回调：", position + "");

                        Snackbar.make(((MainActivity) context).fab, "确定删除当前内容吗?", Snackbar.LENGTH_LONG).setAction("确定", v1 -> {

                            presenter.deletePost(posts.get(position));
                            posts.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                        }).show();

                        return true;

                    }
            );
            String name = post.getAuthor().getUsername();

            viewHolder.avatar.setTextAndColorSeed(name.substring(0, 1), name);
            viewHolder.title.setText(post.getTitle());
            viewHolder.tool.setText(MyUtils.tool[Integer.parseInt(post.getTool()) - 1]);
            viewHolder.group.setText(MyUtils.group[Integer.parseInt(post.getGroup()) - 1]);
            viewHolder.style.setText(MyUtils.style[Integer.parseInt(post.getStyle()) - 1]);
            viewHolder.date.setText(str[0]);
            viewHolder.time.setText(str[1]);
            BmobFile bmobFile = post.getImage();
            if (bmobFile.getLocalFile() == null) {
                Glide.with(context).load(bmobFile.getFileUrl()).into(viewHolder.img);
                Glide.with(context).load(post.getImage2().getFileUrl()).into(viewHolder.img2);
                Glide.with(context).load(post.getImage3().getFileUrl()).into(viewHolder.img3);
            } else {
                Glide.with(context).load(bmobFile.getLocalFile()).into(viewHolder.img);
                Glide.with(context).load(post.getImage2().getLocalFile()).into(viewHolder.img2);
                Glide.with(context).load(post.getImage3().getLocalFile()).into(viewHolder.img3);
            }


        }




    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        Post post = posts.get(position);
        if(post.getSnap()){
            return TYPE_SNAP;
        }else{
            return TYPE_POST;
        }

    }
}
