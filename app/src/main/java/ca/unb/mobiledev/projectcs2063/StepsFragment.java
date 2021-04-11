package ca.unb.mobiledev.projectcs2063;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Service;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;


import java.util.Timer;
import java.util.TimerTask;

import ca.unb.mobiledev.projectcs2063.entity.Item;
import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;

import static ca.unb.mobiledev.projectcs2063.R.layout.fragment_steps;
import static ca.unb.mobiledev.projectcs2063.R.layout.fragment_user;

public class StepsFragment extends Fragment{
    private final static String TAG = "INFO Steps Fragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView currentStepTV;
    private TextView progressText;
    private ProgressBar progressBar;
    private int goal = 10000;
    private int currentSteps = 0;
    //TODO: Allow for the steps to be detected when not on this fragment
    private static StepDetector detector;

    private static ItemRepository itemRepository;

    private StepReceiver receiver;

    private Intent inte;

    public StepsFragment() {
        Log.i(TAG, "constructor method called");
        // Required empty public constructor
    }

    public static Fragment newInstance(String param1, String param2) {
        StepsFragment fragment = new StepsFragment();
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

        if(detector == null)
            detector = new StepDetector();
        inte = new Intent(getContext(), StepService.class);
        getContext().startService(inte);

        StepService.setRepository(itemRepository);

        receiver = new StepReceiver();

    }
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(fragment_steps, container, false);
        currentStepTV = (TextView)rootView.findViewById(R.id.currentStepsTV);

        progressBar = (ProgressBar) rootView.findViewById(R.id.stepProgressBar);
        progressText = (TextView) rootView.findViewById(R.id.progressStepsTV);

        //Write the current steps
        int date = MainActivity.getDate();
        if(itemRepository != null) {
            LiveData<Item> item = itemRepository.getRecordByDate(date);
            item.observe(this, item1 -> {
                if (item1 == null) {
                    itemRepository.insertRecord(date, 0, 0);
                }
                else {

                    currentSteps = item1.getSteps();
                    if(detector.getStepCount() == 0)
                        detector.setStepCount(currentSteps);
                    updateData();

                }

            });

            //Write the goal
            LiveData<Item> goalItem = itemRepository.getGoals();
            goalItem.observe(this, item1 -> {
                if(item1 != null) {
                    goal = item1.getSteps();
                    detector.setStepGoal(goal);
                    updateData();
                }

            });
        }

        return rootView;
    }

    public static void setRepository(ItemRepository ir)
    {
        itemRepository = ir;
    }

    public static StepDetector getDetector()
    {
        return detector;
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(receiver);

    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(receiver, new IntentFilter("GET_STEPS"));
        getContext().startService(inte);
        updateData();
    }

    private void updateData()
    {
        currentSteps = detector.getStepCount();
        CharSequence stepsSequence = currentSteps + " / " + goal + " Steps Today";

        currentStepTV.setText(stepsSequence);

        double progress = (double)currentSteps/(double)goal * 100;
        progressBar.setProgress((int)progress);
        progressText.setText((int)progress + "%");
    }

    class StepReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("GET_STEPS"))
            {
                updateData();

                if(itemRepository != null)
                {
                    int date = MainActivity.getDate();
                    itemRepository.updateStepItem(detector.getStepCount(), date);
                }

            }
        }

    }

}
