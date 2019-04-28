package com.example.hand_knitted.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hand_knitted.R;
import com.example.hand_knitted.bean.Comment;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private int resourceID;


    public CommentAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Comment> objects) {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Comment comment = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.commentUser = view.findViewById(R.id.TVcommentuser);
            viewHolder.commentDate = view.findViewById(R.id.TVcommentdate);
            viewHolder.commentContent = view.findViewById(R.id.TVcommentdetail);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.commentUser.setText(comment.getAuthor().getUsername());
        viewHolder.commentContent.setText(comment.getContent());
        if (comment.getCreatedAt() != null) {
            viewHolder.commentDate.setText(comment.getCreatedAt());
        } else {
            viewHolder.commentDate.setText("刚刚");
        }

        return view;


    }


    class ViewHolder {
        TextView commentUser;
        TextView commentDate;
        TextView commentContent;

    }

}
