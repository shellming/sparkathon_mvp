package com.shellming.sparkathon_mvp.views.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shellming.sparkathon_mvp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 2/21/2016.
 */
public class CurrentObViewHolder extends RecyclerView.ViewHolder{

    private List<Map> data;

    public final View mView;

    public final TextView textView;
    public final ImageView imgView;

    public CurrentObViewHolder(View view, List<Map> data) {
        super(view);
        mView = view;
        textView = (TextView) view.findViewById(R.id.list_item_text);
        imgView = (ImageView) view.findViewById(R.id.list_item_img);

        this.data = data;

    }

    @Override
    public String toString() {
        return super.toString() + " '" + mView.toString();
    }


    public void setContent(final int position){
        try {
            Map<String, Object> item = data.get(position);
            Integer icon = (Integer) item.get("icon");
            String content = (String) item.get("content");
            textView.setText(content);
            imgView.setImageResource(icon);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
