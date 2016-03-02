package com.shellming.sparkathon_mvp.presenters.adapter;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.models.Timeline;
import com.shellming.sparkathon_mvp.utils.ToastUtil;
import com.shellming.sparkathon_mvp.utils.TwitterUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.Twitter;

/**
 * Created by ruluo1992 on 12/24/2015.
 */
public class TimelineRecyclerViewAdapter extends RecyclerView.Adapter<TimelineRecyclerViewAdapter.ViewHolder> {

    private List data;

    public TimelineRecyclerViewAdapter(List data) {
        this.data = data;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView location;
        public TextView time;
        public TextView like;
        public CircleImageView avatar;
        public TextView userName;
        public ImageView likeIcon;
        public boolean isLiked = false;
        public Long twitterId;

        public ViewHolder(final View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.position);
            like = (TextView) itemView.findViewById(R.id.run_like);
            time = (TextView) itemView.findViewById(R.id.run_time);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            likeIcon = (ImageView) itemView.findViewById(R.id.run_like_icon);
            userName = (TextView) itemView.findViewById(R.id.username);

            likeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ModifyFavoriteTask().execute();
                }
            });
        }

        public void setData(Timeline model){
            location.setText(model.getLocation());
            time.setText(model.getDateTime());
            like.setText(String.valueOf(model.getLike()));
            userName.setText("@" + model.getUserName());
            ImageLoader.getInstance().displayImage(model.getAvatar(), avatar);
            twitterId = model.getTwitterId();
            if(model.isFavorite()) {
                likeIcon.setImageResource(R.drawable.ic_like);
                isLiked = true;
            }
            else {
                likeIcon.setImageResource(R.drawable.ic_like_grey);
                isLiked = false;
            }
        }

        class ModifyFavoriteTask extends AsyncTask<Boolean, Integer, Boolean>{

            @Override
            protected void onPostExecute(Boolean success) {
                if(success){
                    if(isLiked){
                        likeIcon.setImageResource(R.drawable.ic_like_grey);
                        isLiked = false;
                        Integer count = Integer.valueOf(like.getText().toString());
                        count--;
                        like.setText(count.toString());
                    }
                    else{
                        likeIcon.setImageResource(R.drawable.ic_like);
                        isLiked = true;
                        Integer count = Integer.valueOf(like.getText().toString());
                        count++;
                        like.setText(count.toString());
                    }
                }
                else{
                    ToastUtil.showToast(itemView.getContext(), "Modify Like Failed", Toast.LENGTH_SHORT);
                }
            }

            @Override
            protected Boolean doInBackground(Boolean... params) {
                Twitter twitter = TwitterUtil.getInstance().getTwitter();
                try {
                    if(isLiked){
                        twitter.destroyFavorite(twitterId);
                    }
                    else{
                        twitter.createFavorite(twitterId);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }

    public void setData(List data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public TimelineRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TimelineRecyclerViewAdapter.ViewHolder holder, int position) {
        Timeline model = (Timeline) data.get(position);
        holder.setData(model);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
