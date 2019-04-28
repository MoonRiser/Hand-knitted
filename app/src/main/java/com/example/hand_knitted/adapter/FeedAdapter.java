package com.example.hand_knitted.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.WorkDetailActivity;
import com.example.hand_knitted.bean.Comment;
import com.example.hand_knitted.bean.User;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.util.MyUtils;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Work> workList;
    private Context context;
    private IHKPresenter presenter;
    private Work currentWork;
    private List<String> ids;
    private Boolean isClicked = true;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ListView listView;
    private Button submit;
    private View dialogComment;
    private EditText etComment;


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
        if (dialogComment == null) {
            dialogComment = LayoutInflater.from(context).inflate(R.layout.dialog_comment, null, false);
            listView = dialogComment.findViewById(R.id.LV);
            submit = dialogComment.findViewById(R.id.BTsubmit);
            etComment = dialogComment.findViewById(R.id.ETcomment);
            builder = new AlertDialog.Builder(context);
            builder.setTitle("当前评论");
            builder.setView(dialogComment);
            builder.setCancelable(true);


        }

        final ViewHolder holder = new ViewHolder(view);


        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Work work = workList.get(position);
        currentWork = work;
        List<Comment> comments = work.getComments();
        ViewHolder viewHolder = (ViewHolder) holder;
        //viewHolder.likes.setOnCheckedChangeListener(this);
        String name = work.getPost().getAuthor().getUsername();
        viewHolder.avatar.setTextAndColorSeed(name.substring(0, 1), name);
        viewHolder.title.setText(work.getPost().getTitle());
        viewHolder.tool.setText(MyUtils.tool[Integer.parseInt(work.getPost().getTool()) - 1]);
        viewHolder.group.setText(MyUtils.group[Integer.parseInt(work.getPost().getGroup()) - 1]);
        viewHolder.style.setText(MyUtils.style[Integer.parseInt(work.getPost().getStyle()) - 1]);
        Glide.with(context).load(work.getPost().getImage().getFileUrl()).into(viewHolder.img);
        viewHolder.comment.setText(commentHelper(comments));


        //如果当前帖子已经被收藏，则显示出来
        if (ids.size() != 0) {


            if (ids.contains(currentWork.getPost().getObjectId())) {
                isClicked = false;
                Log.i("按道理收藏的按钮次时被点亮一次", "当前的位置：" + position);
                //   Log.i("Like表返回的postid打印", "当前的position"+position+"/"+id + "/当前的id：" + currentWork.getPost().getObjectId());
                viewHolder.likes.setChecked(true);
            } else {
                isClicked = false;
                viewHolder.likes.setChecked(false);
            }


        }

        commentCommitHelper(comments, work);
        viewHolder.img.setOnClickListener(v -> jumpAnotherActivity(WorkDetailActivity.class));
        viewHolder.commentBT.setOnClickListener(v -> {

            if (dialog == null) {
                dialog = builder.show();

            } else {
                dialog.show();
            }
        });
        viewHolder.likes.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isClicked) {
                if (isChecked) {
                    presenter.setFavoritePost(currentWork.getPost());
                    Log.i("点击收藏", "已经被回调");
                } else {
                    presenter.cancelFavoritePost(currentWork.getPost());
                    Log.i("点击取消", "已经被回调");
                }
            } else {
                isClicked = true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return workList.size();
    }


    private void jumpAnotherActivity(Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }


    private String commentHelper(List<Comment> comments) {

        StringBuilder str = new StringBuilder();
        String item;

        for (Comment comment : comments) {
            item = comment.getAuthor().getUsername() + ": " + comment.getContent() + "\n";
            str.append(item);
        }
        return str.toString();

    }


    private void commentCommitHelper(List<Comment> comments, Work work) {
        CommentAdapter adapter = new CommentAdapter(context, R.layout.item_comment, comments);

        if (comments.size() > 0) {
            listView.setAdapter(adapter);
        }
        submit.setOnClickListener(v1 -> {
            Comment comment = new Comment();
            comment.setAuthor(BmobUser.getCurrentUser(User.class));
            comment.setContent(etComment.getText().toString());
            comment.setPost(work.getPost());
            presenter.addComment(comment);
            etComment.setText("");
            listView.setAdapter(adapter);
            adapter.add(comment);
            adapter.notifyDataSetChanged();
        });


    }


}
