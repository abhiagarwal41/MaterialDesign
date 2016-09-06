package com.abhishek.materialdesign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView movieTitle, movieDate, movieSynopsis;
    private RatingBar movieRating;
    private ImageView movieImage;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_movie_details);
        //setupWindowAnimations();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        movieTitle = (TextView)findViewById(R.id.movieTitle);
        movieDate = (TextView)findViewById(R.id.movieDate);
        movieSynopsis = (TextView)findViewById(R.id.movieSynopsis);
        movieRating = (RatingBar) findViewById(R.id.movieRating);
        movieImage = (ImageView)findViewById(R.id.movieImage);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        final Movie movie = (Movie) i.getSerializableExtra("movie");
        initToolbar(movie.original_title);
        //new GetBitmapFromURLTask().execute("http://image.tmdb.org/t/p/w185/"+ movie.poster_path);
        movieTitle.setText(movie.original_title);
        movieDate.setText("Release Date : "+movie.release_date);
        Float rating = new Float(movie.vote_average);
        movieRating.setRating(rating);
        movieSynopsis.setText(movie.overview);
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + movie.poster_path)
                .into(movieImage);

       /* ViewTreeObserver vto = movieImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                movieImage.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalWidth = movieImage.getMeasuredWidth();
                int finalHeight = movieImage.getMeasuredHeight();
                Picasso.with(context)
                        .load("http://image.tmdb.org/t/p/w185/" + movie.poster_path)
                        .resize(finalWidth,finalHeight)
                        .into(movieImage);

                return true;
            }
        });*/

    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1500);
        getWindow().setEnterTransition(fade);
    }

    private void dynamicToolbarColor(Bitmap bitmap) {

       // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
         //       R.drawable.profile_pic);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(getResources().getColor(R.color.colorPrimary)));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(getResources().getColor(R.color.colorPrimaryDark)));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            //finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(String movieTitle) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(movieTitle);
        }
       // mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private class GetBitmapFromURLTask extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);
            dynamicToolbarColor(bm);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            return Utils.getBitmapFromURL(url);
        }
    }
}
