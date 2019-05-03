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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hand_knitted.R;
import com.example.hand_knitted.activity.WorkDetailActivity;
import com.example.hand_knitted.bean.Comment;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.User;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.util.MyUtils;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
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
    private AlertDialog.Builder builder0;
    private AlertDialog dialog0;
    private AlertDialog.Builder builder0s;
    private AlertDialog dialog0s;
    private ViewHolderForDetail viewHolderForDetail;
    private ViewHolderForDetails viewHolderForDetails;
    private ListView listView;
    private Button submit;
    private View dialogComment;
    private View dialogDetail;
    private View dialogDetails;
    private EditText etComment;
    private Boolean isSnap = false;


    public FeedAdapter(IHKPresenter presenter) {
        this.presenter = presenter;
    }

    public void setWorkList(List<Work> workList) {
        this.workList = workList;
    }

    public void setSnap(Boolean snap) {
        isSnap = snap;
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
        ImageView img2;
        ImageView img3;
        TextView comment;
        ToggleButton likes;
        Button commentBT;
        Button share;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.IMGavatar);
            title = view.findViewById(R.id.TVtitle);
            tool = view.findViewById(R.id.TVtool);
            group = view.findViewById(R.id.TVgroup);
            style = view.findViewById(R.id.TVstyle);
            img = view.findViewById(R.id.IMGw2);
            img2 = view.findViewById(R.id.IMGw22);
            img3 = view.findViewById(R.id.IMGw23);
            comment = view.findViewById(R.id.TVcomment);
            likes = view.findViewById(R.id.BTfavorite);
            commentBT = view.findViewById(R.id.BTcomment);
            share = view.findViewById(R.id.BTshare);
            cardView = (CardView) view;


        }
    }

    static class ViewHolderSnap extends RecyclerView.ViewHolder {


        AvatarImageView avatar;
        TextView title;
        ImageView img;
        TextView comment;
        ToggleButton likes;
        Button commentBT;
        Button share;
        CardView cardView;

        ViewHolderSnap(View view) {
            super(view);
            avatar = view.findViewById(R.id.IMGavatars);
            title = view.findViewById(R.id.TVtitles);
            img = view.findViewById(R.id.IMGw2s);
            comment = view.findViewById(R.id.TVcomments);
            likes = view.findViewById(R.id.BTfavorites);
            commentBT = view.findViewById(R.id.BTcomments);
            share = view.findViewById(R.id.BTshares);
            cardView = (CardView) view;


        }


    }

    class ViewHolderForDetail {
        AvatarImageView avatar;
        TextView title;
        TextView tool;
        TextView group;
        TextView style;
        TextView date;
        ImageView img;
        ImageView img2;
        ImageView img3;
        TextView content;
        TextView name;

        ViewHolderForDetail(View view) {
            avatar = view.findViewById(R.id.IMGavatar0);
            title = view.findViewById(R.id.TVtitle0);
            tool = view.findViewById(R.id.TVtool0);
            group = view.findViewById(R.id.TVgroup0);
            style = view.findViewById(R.id.TVstyle0);
            date = view.findViewById(R.id.date0);
            img = view.findViewById(R.id.IMG02);
            img2 = view.findViewById(R.id.IMG022);
            img3 = view.findViewById(R.id.IMG023);
            content = view.findViewById(R.id.TVcontent0);
            name = view.findViewById(R.id.name0);

        }


    }

    class ViewHolderForDetails {
        AvatarImageView avatar;
        TextView title;
        TextView date;
        ImageView img;
        TextView content;
        TextView name;

        ViewHolderForDetails(View view) {
            avatar = view.findViewById(R.id.IMGavatar0s);
            title = view.findViewById(R.id.TVtitle0s);
            date = view.findViewById(R.id.date0s);
            img = view.findViewById(R.id.IMG02s);
            content = view.findViewById(R.id.TVcontent0s);
            name = view.findViewById(R.id.name0s);

        }


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();

        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_work, parent, false);
        View viewsnap = LayoutInflater.from(context).inflate(R.layout.item_work_snap, parent, false);
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
        if (dialogDetail == null) {
            dialogDetail = LayoutInflater.from(context).inflate(R.layout.dialog_detail, parent, false);
            viewHolderForDetail = new ViewHolderForDetail(dialogDetail);
            builder0 = new AlertDialog.Builder(context);
            builder0.setTitle("当前帖子");
            builder0.setView(dialogDetail);
            builder0.setCancelable(true);
        }
        if (dialogDetails == null) {
            dialogDetails = LayoutInflater.from(context).inflate(R.layout.dialog_detail_snap, parent, false);
            viewHolderForDetails = new ViewHolderForDetails(dialogDetails);
            builder0s = new AlertDialog.Builder(context);
            builder0s.setTitle("当前随拍");
            builder0s.setView(dialogDetails);
            builder0s.setCancelable(true);
        }
        if (isSnap) {
            return new ViewHolderSnap(viewsnap);
        } else {
            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Work work = workList.get(position);
        currentWork = work;
        List<Comment> comments = work.getComments();

        if (isSnap) {
            ViewHolderSnap viewHolder = (ViewHolderSnap) holder;
            String name = work.getPost().getAuthor().getUsername();
            viewHolder.avatar.setTextAndColorSeed(name.substring(0, 1), name);
            viewHolder.title.setText(work.getPost().getTitle());
            BmobFile bmobFile = work.getPost().getImage();
            if (bmobFile.getLocalFile() == null) {
                Glide.with(context).load(bmobFile.getFileUrl()).into(viewHolder.img);
            } else {
                Glide.with(context).load(bmobFile.getLocalFile()).into(viewHolder.img);
            }
            viewHolder.comment.setText(commentHelper(comments));

            //如果当前帖子已经被收藏，则显示出来
            if (ids.size() != 0) {
                if (ids.contains(currentWork.getPost().getObjectId())) {
                    isClicked = false;
                    // Log.i("按道理收藏的按钮次时被点亮一次", "当前的位置：" + position);
                    //   Log.i("Like表返回的postid打印", "当前的position"+position+"/"+id + "/当前的id：" + currentWork.getPost().getObjectId());
                    viewHolder.likes.setChecked(true);
                } else {
                    isClicked = false;
                    viewHolder.likes.setChecked(false);
                }


            }

            viewHolder.cardView.setOnClickListener(v -> {
                detailDisplayHelper(work);
                if (dialog0s == null) {
                    dialog0s = builder0s.show();
                } else {
                    dialog0s.show();
                }
            });
            viewHolder.commentBT.setOnClickListener(v -> {

                commentCommitHelper(comments, work, position);
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
            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog0s == null) {
                        dialog0s = builder0.show();
                    } else {
                        dialog0s.show();
                    }
                    MyUtils.shotShare(context,dialog0s.getWindow().getDecorView());
                }
            });


        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            String name = work.getPost().getAuthor().getUsername();
            viewHolder.avatar.setTextAndColorSeed(name.substring(0, 1), name);
            viewHolder.title.setText(work.getPost().getTitle());
            viewHolder.tool.setText(MyUtils.tool[Integer.parseInt(work.getPost().getTool()) - 1]);
            viewHolder.group.setText(MyUtils.group[Integer.parseInt(work.getPost().getGroup()) - 1]);
            viewHolder.style.setText(MyUtils.style[Integer.parseInt(work.getPost().getStyle()) - 1]);

            BmobFile bmobFile = work.getPost().getImage();
            if (bmobFile.getLocalFile() == null) {
                Glide.with(context).load(bmobFile.getFileUrl()).into(viewHolder.img);
                Glide.with(context).load(work.getPost().getImage2().getFileUrl()).into(viewHolder.img2);
                Glide.with(context).load(work.getPost().getImage3().getFileUrl()).into(viewHolder.img3);
            } else {
                Glide.with(context).load(bmobFile.getLocalFile()).into(viewHolder.img);
                Glide.with(context).load(work.getPost().getImage2().getLocalFile()).into(viewHolder.img2);
                Glide.with(context).load(work.getPost().getImage3().getLocalFile()).into(viewHolder.img3);
            }
            viewHolder.comment.setText(commentHelper(comments));


            //如果当前帖子已经被收藏，则显示出来
            if (ids.size() != 0) {
                if (ids.contains(currentWork.getPost().getObjectId())) {
                    isClicked = false;
                    // Log.i("按道理收藏的按钮次时被点亮一次", "当前的位置：" + position);
                    //   Log.i("Like表返回的postid打印", "当前的position"+position+"/"+id + "/当前的id：" + currentWork.getPost().getObjectId());
                    viewHolder.likes.setChecked(true);
                } else {
                    isClicked = false;
                    viewHolder.likes.setChecked(false);
                }


            }

            viewHolder.cardView.setOnClickListener(v -> {
                detailDisplayHelper(work);
                if (dialog0 == null) {
                    dialog0 = builder0.show();
                } else {
                    dialog0.show();
                }

            });
            viewHolder.commentBT.setOnClickListener(v -> {

                commentCommitHelper(comments, work, position);
                if (dialog == null) {
                    dialog = builder.show();
                } else {
                    dialog.show();
                }
            });
            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog0 == null) {
                        dialog0 = builder0.show();
                    } else {
                        dialog0.show();
                    }
                    MyUtils.shotShare(context,dialog0.getWindow().getDecorView());
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


    }

    @Override
    public int getItemCount() {
        return workList.size();
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


    private void detailDisplayHelper(Work work) {
        Post post = work.getPost();
        String name = post.getAuthor().getUsername();
        if (isSnap) {
            viewHolderForDetails.avatar.setTextAndColorSeed(name.substring(0, 1), name);
            viewHolderForDetails.content.setText(post.getContent());
            Glide.with(context).load(post.getImage().getFileUrl()).into(viewHolderForDetails.img);
            viewHolderForDetails.date.setText(post.getUpdatedAt());
            viewHolderForDetails.title.setText(post.getTitle());
            viewHolderForDetails.name.setText(post.getAuthor().getUsername());
        } else {
            viewHolderForDetail.avatar.setTextAndColorSeed(name.substring(0, 1), name);
            viewHolderForDetail.content.setText(post.getContent());
            Glide.with(context).load(post.getImage().getFileUrl()).into(viewHolderForDetail.img);
            Glide.with(context).load(post.getImage2().getFileUrl()).into(viewHolderForDetail.img2);
            Glide.with(context).load(post.getImage3().getFileUrl()).into(viewHolderForDetail.img3);
            viewHolderForDetail.tool.setText(MyUtils.tool[Integer.parseInt(work.getPost().getTool()) - 1]);
            viewHolderForDetail.group.setText(MyUtils.group[Integer.parseInt(work.getPost().getGroup()) - 1]);
            viewHolderForDetail.style.setText(MyUtils.style[Integer.parseInt(work.getPost().getStyle()) - 1]);
            viewHolderForDetail.date.setText(post.getUpdatedAt());
            viewHolderForDetail.title.setText(post.getTitle());
            viewHolderForDetail.name.setText(post.getAuthor().getUsername());
        }

    }


    private void commentCommitHelper(List<Comment> comments, Work work, int position) {

        CommentAdapter adapter = new CommentAdapter(context, R.layout.item_comment, comments);
        //  if (comments.size() > 0) {
        listView.setAdapter(adapter);
        //  }
        submit.setOnClickListener(v1 -> {
            Comment comment = new Comment();
            comment.setAuthor(BmobUser.getCurrentUser(User.class));
            comment.setContent(etComment.getText().toString());
            comment.setPost(work.getPost());
            presenter.addComment(comment);
            etComment.setText("");
            // listView.setAdapter(adapter);
            comments.add(comment);
            //     adapter.add(comment);
            adapter.notifyDataSetChanged();
            notifyItemChanged(position);
        });


    }


}
