package com.abhishek.materialdesign;


import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView firstRecyclerView;
    private List<String> moviesImages = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private GridLayoutManager gridLayoutManager;
    private ProgressBar mPbar , progressBarPaging;
    private int visibleThreshold = 5;
    int  visibleItemCount, totalItemCount, firstVisibleItem ;
    static boolean loadingMore = true;
    static boolean noMoreDataOnServer = false;
    RecyclerViewPositionHelper mRecyclerViewHelper;
    private int previousTotal = 0;
    static Integer currentPage = 1;
    List<Movie> movies = new LinkedList<Movie>();
    private static String movieType = Constants.GET_POPULAR_MOVIES;
    DBHandler dbHandler;


    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


   /* @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_popular_movies).setVisible(true);
        menu.findItem(R.id.action_toprated_movies).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_popular_movies){
            if(!movieType.equals(Constants.GET_POPULAR_MOVIES)){
                movieType = Constants.GET_POPULAR_MOVIES;
                currentPage =1;
                noMoreDataOnServer = false;
                moviesAdapter=null;
                firstRecyclerView.removeAllViews();
                movies.clear();
                new FetchMoviesData(currentPage).execute(movieType);
            }
        }
        else if(item.getItemId()==R.id.action_toprated_movies){
            if(!movieType.equals(Constants.GET_TOP_RATED_MOVIES)){
                movieType = Constants.GET_TOP_RATED_MOVIES;
                currentPage =1;
                noMoreDataOnServer = false;
                moviesAdapter=null;
                firstRecyclerView.removeAllViews();
                movies.clear();
                new FetchMoviesData(currentPage).execute(movieType);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(getActivity(),4);
            firstRecyclerView.setLayoutManager(gridLayoutManager);
            moviesAdapter = new MoviesAdapter(getActivity(), movies);
            firstRecyclerView.setAdapter(moviesAdapter);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(getActivity(),2);
            firstRecyclerView.setLayoutManager(gridLayoutManager);
            moviesAdapter = new MoviesAdapter(getActivity(), movies);
            firstRecyclerView.setAdapter(moviesAdapter);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_first, container, false);
        setHasOptionsMenu(true);
         dbHandler = new DBHandler(getActivity());

        firstRecyclerView = (RecyclerView)view.findViewById(R.id.first_recycler_view);
        mPbar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBarPaging = (ProgressBar) view.findViewById(R.id.progress_bar_paging);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        firstRecyclerView.setLayoutManager(gridLayoutManager);
        if(Utils.networkConnectivity(getActivity()))
            new FetchMoviesData(currentPage).execute(movieType);
        else {
           // movies = dbHandler.getAllRecords();
            movies = fetchMovies();
            Log.d(Constants.TAG,"movies size "+ movies.size());

            if(movies!=null && movies.size()>0) {
                moviesAdapter = new MoviesAdapter(getActivity(), movies);
                firstRecyclerView.setAdapter(moviesAdapter);
            }

            HomeActivity.showSnackbar("No internet connection");
        }

       /* firstRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // TODO Auto-generated method stub
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(recyclerView.getAdapter() != null && !noMoreDataOnServer){

                    mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loadingMore) {
                        if (totalItemCount > previousTotal) {
                            loadingMore = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if (!loadingMore && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        currentPage++;
                        new FetchMoviesData(currentPage).execute(movieType);
                        loadingMore = true;
                    }
                }
            }
        });*/

        return view;


    }

   private class FetchMoviesData extends AsyncTask<String,Void,String>{

       int currentPage;

       public FetchMoviesData(){
           super();
       }

       public FetchMoviesData(int currentPage){
           super();
           this.currentPage = currentPage;
       }

       @Override
       protected String doInBackground(String... params) {
           String moviesType = params[0];
           String url= Constants.BASE_URL + moviesType + "?api_key="+ Constants.MOVIEDB_API_KEY + "&page=" + currentPage;
           System.out.println(url);
           String response = Utils.sendGETRequest(url);
           return response;
       }

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           if(currentPage==1)
               mPbar.setVisibility(View.VISIBLE);
           else
               progressBarPaging.setVisibility(View.VISIBLE);
       }

       @Override
       protected void onPostExecute(String result) {
           super.onPostExecute(result);
           if(currentPage==1)
               mPbar.setVisibility(View.GONE);
           else
               progressBarPaging.setVisibility(View.GONE);


           if (result != null && !"".equals(result) && !"error".equals(result)) {
               try {
                   /*JSONObject jsonObj = new JSONObject(result);
                   JSONArray movies = jsonObj.getJSONArray("results");
                   for(int i=0;i<movies.length();i++){
                       JSONObject movie = movies.getJSONObject(i);
                       moviesImages.add("http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path"));
                   }*/
                   Response response = (new Gson()).fromJson(result, Response.class);
                   List<Movie> moviesReturned = response.results;
                  // dbHandler.deleteAllRecords();
                  // dbHandler.insertAllRecords(new ArrayList<Movie>(moviesReturned));
                   if(fetchMovies().size()==0)
                    insertAllMovies(new ArrayList<Movie>(moviesReturned));

                   if(moviesReturned.size()<20)
                       noMoreDataOnServer = true;
                   movies.addAll(moviesReturned);

                   if(currentPage==1) {
                       moviesAdapter = new MoviesAdapter(getActivity(), movies);
                       firstRecyclerView.setAdapter(moviesAdapter);
                   }
                   else{
                       moviesAdapter.setData(movies);
                       moviesAdapter.notifyDataSetChanged();
                   }


               }catch (Exception e){
                   e.printStackTrace();
               }
           }else{
               HomeActivity.showSnackbar("Internet connection problem");
           }

       }
   }

    public void insertAllMovies(List<Movie> movies){
         for(Movie movie : movies){
             ContentValues contentValues = new ContentValues();
             contentValues.put(DBHandler.COLUMN_ID, movie.id);
             contentValues.put(DBHandler.COLUMN_TITLE, movie.original_title);
             contentValues.put(DBHandler.COLUMN_POSTER_PATH, movie.poster_path);
             contentValues.put(DBHandler.COLUMN_RELEASE_DATE, movie.release_date);
             contentValues.put(DBHandler.COLUMN_OVERVIEW, movie.overview);
             contentValues.put(DBHandler.COLUMN_RATING, movie.vote_average);

             Uri uri = getActivity().getContentResolver().insert(
                     MyProvider.MOVIES_URI, contentValues);
         }
    }

    public List<Movie> fetchMovies() {

        String URL = MyProvider.MOVIES_URI.toString();

        Uri movies_uri = Uri.parse(URL);
        Cursor c = getActivity().getContentResolver().query(movies_uri, null, null, null, null);
        //Cursor c = getActivity().managedQuery(movies, null, null, null, "name");
        LinkedList<Movie> movies = new LinkedList<>();

        if (c.moveToFirst()) {
            do{
                Movie movie = new Movie();
                movie.setId(c.getString(c.getColumnIndex(DBHandler.COLUMN_ID)));
                movie.setOriginal_title(c.getString(c.getColumnIndex(DBHandler.COLUMN_TITLE)));
                movie.setPoster_path(c.getString(c.getColumnIndex(DBHandler.COLUMN_POSTER_PATH)));
                movie.setRelease_date(c.getString(c.getColumnIndex(DBHandler.COLUMN_RELEASE_DATE)));
                movie.setOverview(c.getString(c.getColumnIndex(DBHandler.COLUMN_OVERVIEW)));
                movie.setVote_average(c.getDouble(c.getColumnIndex(DBHandler.COLUMN_RATING)));
                movies.add(movie);
            } while (c.moveToNext());
        }

        return movies;
    }

}
