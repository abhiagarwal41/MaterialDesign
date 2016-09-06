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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
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
        private RippleView rippleView;

        public ViewHolder(View v) {
            super(v);
            movieImage = (ImageView) v.findViewById(R.id.movieImage);
            rippleView = (RippleView)v.findViewById(R.id.rippleView);
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Movie movie = moviesList.get(position);
        final String movieImage ="http://image.tmdb.org/t/p/w185/"+ movie.poster_path;

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

        viewHolder.rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(context,MovieDetailsActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context , new Pair<View, String>(viewHolder.movieImage,
                        context.getString(R.string.transition_name)));
                intent.putExtra("movie",movie);
                ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
               // context.startActivity(intent,options.toBundle());
            }

        });

    }

    @Override
    public int getItemCount() {
        return moviesList == null ? 0 : moviesList.size();
    }

    public void setData(List<Movie> movies) {
       moviesList = movies;
    }

}