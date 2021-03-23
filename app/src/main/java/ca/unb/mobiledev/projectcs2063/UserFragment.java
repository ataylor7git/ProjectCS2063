package ca.unb.mobiledev.projectcs2063;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;

import static ca.unb.mobiledev.projectcs2063.R.layout.fragment_user;

public class UserFragment extends Fragment{
    private final static String TAG = " INFO User Fragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int waterGoal;
    private int stepGoal;
    private View rootView;

    private EditText sGoalInput;
    private EditText wGoalInput;
    private boolean changed = false;
    private static ItemRepository itemRepository;


    public UserFragment() {
        Log.i(TAG, "constructer method called");

        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        // Inflate the layout for this fragment
        if (rootView==null) {
            rootView = inflater.inflate(fragment_user, container, false);
        }
        else{
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }

        //The step goal edit text
        sGoalInput = (EditText) rootView.findViewById(R.id.stepGoalTN2);
        sGoalInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                if(sGoalInput.getText().toString().equals("") || sGoalInput.getText().toString().equals("0"))
                {
                    stepGoal = 1;
                }
                else {
                    String goalIn = sGoalInput.getText().toString();
                    stepGoal = Integer.parseInt(goalIn);
                }
                changed = true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //The water goal edit text
        wGoalInput = (EditText) rootView.findViewById(R.id.waterGoalTN);
        wGoalInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                if(wGoalInput.getText().toString().equals("") || wGoalInput.getText().toString().equals("0"))
                {
                    waterGoal = 1;
                }
                else {
                    String goalIn = wGoalInput.getText().toString();
                    waterGoal = Integer.parseInt(goalIn);
                }
                changed = true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(changed && itemRepository != null)
        {
            Log.i(TAG, "Update");
            itemRepository.updateItem(stepGoal, waterGoal, -1);
            changed = false;
        }
        else
        {
            Log.i(TAG, "Not update");
        }
    }

    public static void setRepository(ItemRepository ir)
    {
        itemRepository = ir;
    }
}
