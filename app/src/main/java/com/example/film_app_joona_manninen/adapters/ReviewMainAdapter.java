package com.example.film_app_joona_manninen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.film_app_joona_manninen.R;
import com.example.film_app_joona_manninen.activities.ReviewListActivity;

public class ReviewMainAdapter extends BaseAdapter {
    ReviewListActivity reviewListActivity;
    String[] title;
    Animation animation;

    public ReviewMainAdapter(ReviewListActivity reviewListActivity, String[] title){
        this.reviewListActivity = reviewListActivity;
        this.title = title;
    }

    public static int getRandom(int max) {
        return (int) (Math.random()*max);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        view = LayoutInflater.from(reviewListActivity).inflate(R.layout.review_layout, viewGroup, false);
        animation = AnimationUtils.loadAnimation(reviewListActivity, R.anim.animation1);

        TextView textView;
        LinearLayout ll_background;
        ll_background = view.findViewById(R.id.ll_background1);
        textView = view.findViewById(R.id.textView3);

        int num = getRandom(8);

        // Making the list view have random gradient color out of 10 different ones
        if(num==1){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_1));
        } else if(num == 2){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_2));
        } else if(num == 3){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_3));
        } else if(num == 4){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_4));
        } else if(num == 5){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_5));
        } else if(num == 6){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_6));
        } else if(num == 7){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_7));
        } else if(num == 8){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_8));
        } else if(num == 9){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_9));
        } else if(num == 10){
            ll_background.setBackground(ContextCompat.getDrawable(reviewListActivity, R.drawable.gradient_10));
        }
        textView.setText(title[i]);
        textView.setAnimation(animation);

        return view;
    }
}
