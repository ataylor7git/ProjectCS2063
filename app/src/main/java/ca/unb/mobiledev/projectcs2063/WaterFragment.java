package ca.unb.mobiledev.projectcs2063;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ca.unb.mobiledev.projectcs2063.entity.Item;
import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;

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

    private ProgressBar progressCircle;
    private TextView progressText;
    private TextView currentWaterTV;
    private EditText addWaterTN;
    private Button addButton;
    private int goal_intake = 2000;
    private int current_water_intake = 0;

    private static ItemRepository itemRepository;
    private int steps = 0;

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
        View rootView = inflater.inflate(fragment_water, container, false);

        currentWaterTV = (TextView) rootView.findViewById(R.id.currentWaterTV);
        progressCircle = (ProgressBar) rootView.findViewById(R.id.waterProgressBar);
        progressText = (TextView) rootView.findViewById(R.id.progressWaterTV);
        addWaterTN = (EditText) rootView.findViewById(R.id.addWaterTN);
        addButton = (Button) rootView.findViewById(R.id.add_drink_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String add_water = addWaterTN.getText().toString();
                current_water_intake += Integer.parseInt(add_water);

                CharSequence waterSequence = "You Drank " + current_water_intake + " / " + goal_intake + "mL of Water Today";
                currentWaterTV.setText(waterSequence);
                addWaterTN.setText("");
                double progress = ((double) current_water_intake /(double) goal_intake) * 100;
                progressCircle.setProgress((int)progress);
                progressText.setText((int)progress + "%");

            }
        });

        //Write the current water
        int date = MainActivity.getDate();
        if(itemRepository != null) {
            LiveData<Item> item = itemRepository.getRecordByDate(date);
            item.observe(this, item1 -> {
                if (item1 == null)
                    itemRepository.insertRecord(date, 0, 0);
                else {
                    steps = item1.getSteps();
                    current_water_intake = item1.getWater();

                    CharSequence waterSequence = "You Drank " + current_water_intake + " / " + goal_intake + "mL of Water Today";
                    currentWaterTV.setText(waterSequence);

                    double progress = (double) current_water_intake / (double) goal_intake * 100;
                    progressCircle.setProgress((int) progress);
                    progressText.setText((int)progress + "%");

                }

            });

            //Write the goal
            LiveData<Item> goalItem = itemRepository.getGoals();
            goalItem.observe(this, item1 -> {
                if (item1 != null) {
                    goal_intake = item1.getWater();

                    CharSequence waterSequence = "You Drank " + current_water_intake + " / " + goal_intake + "mL of Water Today";
                    currentWaterTV.setText(waterSequence);

                    double progress = (double) current_water_intake / (double) goal_intake * 100;
                    progressCircle.setProgress((int) progress);
                    progressText.setText((int)progress + "%");
                }

            });
        }


        return rootView;

    }

    public static void setRepository(ItemRepository ir)
    {
        itemRepository = ir;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(itemRepository != null)
        {
            int date = MainActivity.getDate();
            itemRepository.updateItem(steps, current_water_intake, date);
        }
    }

}