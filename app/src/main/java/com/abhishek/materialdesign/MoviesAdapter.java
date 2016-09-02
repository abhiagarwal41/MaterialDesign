package com.abhishek.materialdesign;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sony on 31-08-2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movie> moviesList;
    private Context context;


    public MoviesAdapter(Context context,List<Movie> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView movieImage;

        public ViewHolder(View v) {
            super(v);
            movieImage = (ImageView) v.findViewById(R.id.movieImage);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_movies, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final String movieImage ="http://image.tmdb.org/t/p/w185/"+ moviesList.get(position).poster_path;

        ViewTreeObserver vto = viewHolder.movieImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                viewHolder.movieImage.getViewTreeObserver().removeOnPreDrawListener(this);
                //int finalHeight = holder.trendImage.getMeasuredHeight();
                int finalWidth = viewHolder.movieImage.getMeasuredWidth();
                int finalHeight = viewHolder.movieImage.getMeasuredHeight();
              /*  FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(finalWidth, finalWidth);
                holder.trendImage.setLayoutParams(layoutParams);*/
                Picasso.with(context)
                        .load(movieImage)
                        //.placeholder(R.drawable.placeholder)   // optional
                        //.error(R.drawable.error)      // optional
                        .resize(finalWidth,finalHeight)// optional
                        .into(viewHolder.movieImage);

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList == null ? 0 : moviesList.size();
    }

}