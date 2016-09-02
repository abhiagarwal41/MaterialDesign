package com.abhishek.materialdesign;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private ProgressBar mPbar;


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
        firstRecyclerView = (RecyclerView)view.findViewById(R.id.first_recycler_view);
        mPbar = (ProgressBar) view.findViewById(R.id.progress_bar);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        firstRecyclerView.setLayoutManager(gridLayoutManager);
        new FetchMoviesData().execute("/movie/popular");
        return view;


    }

   private class FetchMoviesData extends AsyncTask<String,Void,String>{

       public FetchMoviesData(){
           super();
       }

       @Override
       protected String doInBackground(String... params) {
           String moviesType = params[0];
           String url= Constants.BASE_URL + moviesType + "?api_key="+ Constants.MOVIEDB_API_KEY;
           String response = Utils.sendGETRequest(url);
           return response;
       }

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           mPbar.setVisibility(View.VISIBLE);
       }

       @Override
       protected void onPostExecute(String result) {
           super.onPostExecute(result);
           mPbar.setVisibility(View.GONE);

           if (result != null && !"".equals(result)) {
               try {
                   /*JSONObject jsonObj = new JSONObject(result);
                   JSONArray movies = jsonObj.getJSONArray("results");
                   for(int i=0;i<movies.length();i++){
                       JSONObject movie = movies.getJSONObject(i);
                       moviesImages.add("http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path"));
                   }*/
                   Response response = (new Gson()).fromJson(result, Response.class);
                   List<Movie> movies = response.results;
                   moviesAdapter = new MoviesAdapter(getActivity(),movies);
                   firstRecyclerView.setAdapter(moviesAdapter);

               }catch (Exception e){
                   e.printStackTrace();
               }
           }else{

           }

       }
   }

}
