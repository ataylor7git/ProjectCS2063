package ca.unb.mobiledev.projectcs2063;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;


import ca.unb.mobiledev.projectcs2063.entity.Item;
import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;

import static ca.unb.mobiledev.projectcs2063.R.layout.fragment_steps;
import static ca.unb.mobiledev.projectcs2063.R.layout.fragment_user;

public class StepsFragment extends Fragment implements SensorEventListener {
    private final static String TAG = "INFO Steps Fragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText goalInput;
    private TextView currentStepTV;
    private TextView progressText;
    private ProgressBar progressBar;
    private int goal = 10000;
    private int currentSteps = 0;
    private StepDetector stepDetect;
    private final double STEPTHRESH = 16;
    private int water;

    private static ItemRepository itemRepository;


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
    public void onSensorChanged(SensorEvent event) {
        //Log.i(TAG, "Sensor activated: ");
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double accelX = event.values[0];
            double accelY = event.values[1];
            double accelZ = event.values[2];
            double atot = Math.sqrt(accelX*accelX + accelY*accelY + accelZ*accelZ);
            boolean success = stepDetect.detect(atot, STEPTHRESH);
            if(success) {
                currentSteps = stepDetect.getStepCount();
                CharSequence stepsSequence = currentSteps + " / " + goal + " Steps Today";

                currentStepTV.setText(stepsSequence);

                double progress = (double)currentSteps/(double)goal * 100;
                progressBar.setProgress((int)progress);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        stepDetect = new StepDetector();
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sSensor= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean success = sensorManager.registerListener(this, sSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(fragment_steps, container, false);
       // View userView = inflater.inflate(fragment_user, container, false);
        currentStepTV = (TextView)rootView.findViewById(R.id.currentStepsTV);

        progressBar = (ProgressBar) rootView.findViewById(R.id.stepProgressBar);
        progressText = (TextView) rootView.findViewById(R.id.progressStepsTV);



        //Write the current steps
        int date = MainActivity.getDate();
        if(itemRepository != null) {
            LiveData<Item> item = itemRepository.getRecordByDate(date);
            item.observe(this, item1 -> {
                if (item1 == null)
                    itemRepository.insertRecord(date, 0, 0);
                else {
                    currentSteps = item1.getSteps();
                    water = item1.getWater();

                    CharSequence stepsSequence = currentSteps + " / " + goal + " Steps Today";

                    currentStepTV.setText(stepsSequence);

                    double progress = (double)currentSteps/(double)goal * 100;
                    progressBar.setProgress((int)progress);

                    stepDetect.setStepCount(currentSteps);
                }

            });

            //Write the goal
            LiveData<Item> goalItem = itemRepository.getGoals();
            goalItem.observe(this, item1 -> {
                if(item1 != null) {
                    goal = item1.getSteps();

                    currentSteps = stepDetect.getStepCount();
                    CharSequence stepsSequence = currentSteps + " / " + goal + " Steps Today";

                    currentStepTV.setText(stepsSequence);

                    double progress = (double)currentSteps/(double)goal * 100;
                    progressBar.setProgress((int)progress);
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
            Log.i(TAG, "Update");
            int date = MainActivity.getDate();
            itemRepository.updateItem(currentSteps, water, date);
        }
        else
        {
            Log.i(TAG, "Not update");
        }
    }

}
