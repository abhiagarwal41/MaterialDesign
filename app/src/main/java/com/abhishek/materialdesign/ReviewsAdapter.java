package com.abhishek.materialdesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

/**
 * Created by sony on 31-08-2016.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> authors;
    private List<String> content;
    private Context context;

    public ReviewsAdapter(List<String> authors,List<String> content,Context context) {
        this.authors = authors;
        this.content = content;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView review_author;
        private final ExpandableTextView review;

        public ViewHolder(View v) {
            super(v);
            review_author = (TextView)v.findViewById(R.id.review_author);
            review = (ExpandableTextView) v.findViewById(R.id.review);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_review, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        String author = authors.get(position);
        String review = content.get(position);
        holder.review_author.setText("By: "+author);
        holder.review.setText(review);

    }

    @Override
    public int getItemCount() {
        return authors == null ? 0 : authors.size();
    }

}