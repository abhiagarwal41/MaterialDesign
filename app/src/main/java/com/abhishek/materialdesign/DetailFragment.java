package com.abhishek.materialdesign;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    final static String KEY_POSITION = "position";
    final static String KEY_NAME= "name";
    int mCurrentPosition = -1;
    String mCurrentName = "-1";

    String[] mVersionDescriptions;
    TextView mVersionDescriptionTextView;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // If the Activity is recreated, the savedInstanceStare Bundle isn't empty
        // we restore the previous version name selection set by the Bundle.
        // This is necessary when in two pane layout
        if (savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
            mCurrentName = savedInstanceState.getString(KEY_NAME);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mVersionDescriptionTextView = (TextView) view.findViewById(R.id.version_description);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During the startup, we check if there are any arguments passed to the fragment.
        // onStart() is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method below
        // that sets the description text
        Bundle args = getArguments();
        if (args != null){
            // Set description based on argument passed in
            setDescription(args.getInt(KEY_POSITION),args.getString(KEY_NAME));
        } else if(mCurrentPosition != -1){
            // Set description based on savedInstanceState defined during onCreateView()
            setDescription(mCurrentPosition,mCurrentName);
        }
    }

    public void setDescription(int descriptionIndex, String name){
        mVersionDescriptionTextView.setText(name);
        mCurrentPosition = descriptionIndex;
        mCurrentName = name;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION,mCurrentPosition);
        outState.putString(KEY_NAME,mCurrentName);

    }

}
