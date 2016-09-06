package com.abhishek.materialdesign;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sony on 02-09-2016.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Movie implements Serializable {
    String poster_path;
    boolean adult;
    String overview;
    String release_date;
    Integer[] genre_ids;
    String id;
    String original_title;
    String original_language;
    String title;
    String backdrop_path;
    Double popularity;
    Integer vote_count;
    boolean video;
    Double vote_average;

    public static List<Movie> movies = new LinkedList<Movie>();

}
