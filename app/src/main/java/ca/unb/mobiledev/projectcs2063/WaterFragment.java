package ca.unb.mobiledev.projectcs2063;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static ca.unb.mobiledev.projectcs2063.R.menu.*;
import static ca.unb.mobiledev.projectcs2063.R.layout.*;

public class WaterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match (don't have to worry about this yet)
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters (don't have to worry about this yet)
    private String mParam1;
    private String mParam2;
    private final static String TAG = "INFO Water Fragment";

    public WaterFragment() {
        Log.i(TAG, "water fragment constructer called");
        // Required empty public constructor
    }

    public static WaterFragment newInstance(String param1, String param2) {
        //System.out.println("param is" + param1);
        //System.out.println(param2);
        WaterFragment fragment = new WaterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreate method called");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "fragment water method about to be called");

        // Inflate the layout for this fragment
        return inflater.inflate(fragment_water, container, false);

    }

}
