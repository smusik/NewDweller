package com.example.newdweller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    Context context;
    List<Post> postArrayList;

    public PostAdapter(Context context, List<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_post_item,parent,false);
        return new PostHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = postArrayList.get(position);
        holder.tvTitle.setText(post.getTitle());
        Glide.with(context).load(post.getImg()).into(holder.imgPost);
        holder.txtDesc.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,txtDesc;
        ImageView imgPost;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            txtDesc = itemView.findViewById(R.id.row_post_desc);

        }
    }
}
