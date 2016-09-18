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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView movieTitle, movieDate, movieSynopsis;
    private RecyclerView reviewsRecyclerView;
    private RatingBar movieRating;
    private ImageView movieImage;
    Context context;
    YouTubePlayerSupportFragment frag;
    ArrayList<String> trailers = new ArrayList<>();

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
        reviewsRecyclerView = (RecyclerView)findViewById(R.id.reviews_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);

        frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);

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
        new FetchTrailers(this).execute(movie.id);
        new FetchReviews().execute(movie.id);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(trailers.get(0));
        }
    }

    @Override
        public void onInitializationFailure (YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
            if (error.isUserRecoverableError()) {
                //error.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
            } else {
                String errorMessage = error.toString();
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }

  /*  private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1500);
        getWindow().setEnterTransition(fade);
    }*/

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

    public void initializeYoutube(){
        frag.initialize(Constants.DEVELOPER_KEY, this);
    }

    private class FetchTrailers extends AsyncTask<String,Void,String>{
        Context context;

        public FetchTrailers(){
            super();
        }

        public FetchTrailers(Context context){
           this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String movie_id = params[0];
            String url= Constants.BASE_URL + Constants.GET_TRAILERS + "?api_key="+ Constants.MOVIEDB_API_KEY ;
            url = url.replace("movie_id",movie_id);
            System.out.println(url);
            String response = Utils.sendGETRequest(url);
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (result != null && !"".equals(result) && !"error".equals(result)) {
                try {

                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray results = jsonObj.getJSONArray("results");
                   for(int i=0;i<results.length();i++){
                       JSONObject movie = results.getJSONObject(i);
                       trailers.add(movie.getString("key"));
                   }
                    initializeYoutube();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                HomeActivity.showSnackbar("Internet connection problem");
            }

        }
    }

    private class FetchReviews extends AsyncTask<String,Void,String>{

        public FetchReviews(){
            super();
        }


        @Override
        protected String doInBackground(String... params) {
            String movie_id = params[0];
            String url= Constants.BASE_URL + Constants.GET_REVIEWS + "?api_key="+ Constants.MOVIEDB_API_KEY ;
            url = url.replace("movie_id",movie_id);
            System.out.println(url);
            String response = Utils.sendGETRequest(url);
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (result != null && !"".equals(result) && !"error".equals(result)) {
                try {
                    ArrayList<String> authors = new ArrayList<>();
                    ArrayList<String> content = new ArrayList<>();
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray results = jsonObj.getJSONArray("results");
                    for(int i=0;i<results.length();i++){
                        JSONObject movie = results.getJSONObject(i);
                        authors.add(movie.getString("author"));
                        content.add(movie.getString("content"));
                    }

                    reviewsRecyclerView.setAdapter(new ReviewsAdapter(authors,content,context));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                HomeActivity.showSnackbar("Internet connection problem");
            }

        }
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
