package com.shellming.sparkathon_mvp.views.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.constants.GlobalConstant;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.views.activity.SendTwitterActivity;

import java.util.List;

/**
 * Created by ruluo1992 on 2/28/2016.
 */
public class TendaysForcastViewHolder extends RecyclerView.ViewHolder{
    public final View mView;

    public TextView mTempView;
    public TextView mDesView;
    public ImageView mWeatherImgView;
    public TextView mDateTime;
    public TextView mDegreeView;
    public TextView mExpView;
    public ImageView expressionImgView;

    public TendaysForcastViewHolder(View view, final Context context) {
        super(view);
        mView = view;

        mTempView = (TextView) mView.findViewById(R.id.tempture);
        mDesView = (TextView) mView.findViewById(R.id.description);
        mWeatherImgView = (ImageView) mView.findViewById(R.id.weatherImg);
        mDateTime = (TextView) mView.findViewById(R.id.data_time);
        mDegreeView = (TextView) mView.findViewById(R.id.degree);
        mExpView = (TextView) mView.findViewById(R.id.run_exp);
        expressionImgView = (ImageView) mView.findViewById(R.id.expression);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendTwitterActivity.class);
                String dateTime = mDateTime.getText().toString();
                String[] parts = dateTime.split(",");
                intent.putExtra("date", parts[0]);
                context.startActivity(intent);
            }
        });
    }

    public void setData(Weather weather){
        mTempView.setText(weather.getTempMin() + "~" + weather.getTempMax());
        mDesView.setText(weather.getPhrase_32char());
        mWeatherImgView.setImageResource(weather.getIcon());
        mDateTime.setText(weather.getFormatedDate());
        mDegreeView.setText("℃");
        Integer exp = 3;
        if(exp != -1){
            mExpView.setText("Run Exp:" + exp);
            if(exp > GlobalConstant.THRESHOLD)
                expressionImgView.setImageResource(R.drawable.smile);
            else
                expressionImgView.setImageResource(R.drawable.cry);
        }
        mExpView.setText("Run Exp:" + 3);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mView.toString();
    }
}
