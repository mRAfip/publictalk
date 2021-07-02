package com.example.publictalk;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    Context context;
    ArrayList<PostModel> postModelArrayList;

    public PostAdapter(Context context, ArrayList<PostModel> postModelArrayList) {
        this.context = context;
        this.postModelArrayList = postModelArrayList;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.result_main_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        PostModel postModel = postModelArrayList.get(position);
        holder.userPublicName.setText(postModel.getUserPublicName());
        holder.topicName.setText(postModel.getTopicName());
        holder.postHead.setText(postModel.getPostHead());
        Picasso.get().load(postModel.getUserPhoto()).into(holder.userImage);


    }

    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userPublicName,topicName,postHead,postDetail;
        ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userPublicName = itemView.findViewById(R.id.talker_user_name);
            topicName= itemView.findViewById(R.id.category_head);
            postHead  = itemView.findViewById(R.id.post_head);
            userImage = itemView.findViewById(R.id.item_profile_image);
        }
    }
}
