package com.abhishek.materialdesign;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    RecyclerView secondRecyclerView;


    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        secondRecyclerView = (RecyclerView)view.findViewById(R.id.second_recycler_view);
        secondRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
        secondRecyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();

            int itemsCount =5;
            for (int i = 0; i < itemsCount; i++) {
                itemList.add("Item " + i);
            }

        return itemList;
    }

}
