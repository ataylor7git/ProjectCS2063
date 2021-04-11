package ca.unb.mobiledev.projectcs2063;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import ca.unb.mobiledev.projectcs2063.entity.Item;
import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;


public class WaterFragment extends Fragment {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";
        private static final String WATERCHANNELID = "WaterServiceNotif" ;
        private String mParam1;
        private String mParam2;
        private final static String TAG = "INFO Water Fragment";

        private ProgressBar progressCircle;
        private TextView progressText;
        private TextView currentWaterTV;
        private Button addButton, removeButton, clearButton;

        private TextView dateDisplay;
        private Calendar calendar;
        private SimpleDateFormat dateFormat;
        private String date;

        private int goal_intake;
        private int current_water_intake, steps = 0;
        private int selectedOption = 0;

        LinearLayout option1, option2, option3, option4, option5, optionCustom;
        Drawable option1Background, option2Background, option3Background, option4Background,
        option5Background, optionCustomBackground;
        int selectedColor;

        public static final String TIME_STAMP_FORMAT = "yyyMMdd_HHmmss";
        private AlarmManager alarmMgr;
        private PendingIntent alarmIntent;

        private static ItemRepository itemRepository;
        private boolean isGoalMet = false;

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

            alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getContext(),0,intent, 0);

            if (!isGoalMet) {
                System.out.println("onReceive Broadcast Receiver, goal hasnt been met");
                alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR, AlarmManager.INTERVAL_HOUR, alarmIntent);

            }

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
            getContext().registerReceiver(batteryInfoReceiver, intentFilter);
        }

        private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {
                    if (!isGoalMet) {
                        System.out.println("onReceive Broadcast Receiver, goal hasnt been met");
                        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR, AlarmManager.INTERVAL_HOUR, alarmIntent);
                    }
                }
            }
        };

        @SuppressLint("ResourceType")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.i(TAG, "fragment water method about to be called");

            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_water, container, false);

            currentWaterTV = (TextView) rootView.findViewById(R.id.currentWaterTV);
            progressCircle = (ProgressBar) rootView.findViewById(R.id.waterProgressBar);
            progressText = (TextView) rootView.findViewById(R.id.progressWaterTV);
            //addWaterTN = (EditText) rootView.findViewById(R.id.addWaterTN);
            addButton = (Button) rootView.findViewById(R.id.add_drink_button);
            removeButton = rootView.findViewById(R.id.remove_drink_button);
            clearButton = rootView.findViewById(R.id.clear_drink_button);

            //display the date
            dateDisplay = rootView.findViewById(R.id.date_title);
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("EEE MMM d, yyyy" );
            date = dateFormat.format(calendar.getTime());
            dateDisplay.setText(date);


            option1 = rootView.findViewById(R.id.option1);
            option2 =  rootView.findViewById(R.id.option2);
            option3 = rootView.findViewById(R.id.option3);
            option4 = rootView.findViewById(R.id.option4);
            option5 = rootView.findViewById(R.id.option5);
            optionCustom = rootView.findViewById(R.id.optionCustom);

            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    System.out.println("selected option: " + selectedOption);
                    addWater(selectedOption);
                    setNonSelected();
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    System.out.println("selected option: " + selectedOption);
                    removeWater(selectedOption);
                    setNonSelected();
                }
            });

            clearButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder clearAlert = new AlertDialog.Builder(getContext());
                    clearAlert.setMessage("Warning! This will permanently delete your data for today!");
                    clearAlert.setCancelable(true);

                    clearAlert.setPositiveButton("Delete my data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearWaterData();
                        }
                    });

                    clearAlert.setNeutralButton(android.R.string.cancel, null);
                    clearAlert.create();
                    clearAlert.show();
                }
            });

            //Write the current water
            int date = MainActivity.getDate();
            System.out.println("THE DATE TODAY IS: " + MainActivity.getDate());
            if(itemRepository != null) {
                LiveData<Item> item = itemRepository.getRecordByDate(date);
                item.observe(
                        this, item1 -> {
                    if (item1 == null) {
                        itemRepository.insertRecord(date, 0, 0);
                    }

                    else {
                        steps = item1.getSteps();
                        current_water_intake = item1.getWater();
                        updateWaterDisplay();
                    }

                });

                //Write the goal
                LiveData<Item> goalItem = itemRepository.getGoals();
                goalItem.observe(this, item1 -> {
                    if (item1 != null) {
                        goal_intake = item1.getWater();

                        updateWaterDisplay();
                    }

                });
            }

            //save original background
            option1Background = option1.getBackground();
            option2Background = option2.getBackground();
            option3Background = option3.getBackground();
            option4Background = option4.getBackground();
            option5Background = option5.getBackground();
            optionCustomBackground = optionCustom.getBackground();
            selectedColor = Color.rgb(60, 80, 100);

            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOption = 50;
                    System.out.println("Clicked! new selected option is: " + selectedOption);
                    setSelected(option1);
                }
            });

            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOption = 125;
                    setSelected(option2);
                }
            });

            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOption = 250;
                    setSelected(option3);
                }
            });

            option4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOption = 500;
                    setSelected(option4);
                }
            });

            option5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOption = 1000;
                    setSelected(option5);
                }
            });

            optionCustom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(optionCustom);

                    LayoutInflater layout = LayoutInflater.from(getActivity());
                    View promptsView = layout.inflate(R.layout.custom_input_dialog, null);
                    TextInputEditText userInput = (TextInputEditText) promptsView.findViewById(R.id.customInputET);

                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
                    alertDialogBuilder.setView(promptsView);

                    alertDialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                                String inputText = userInput.getText().toString();

                                if (inputText.equals("")) {
                                    selectedOption = 0;
                                }
                                else {
                                    selectedOption = Integer.parseInt(inputText);
                                }

                                addWater(selectedOption);
                                setNonSelected();

                        }
                    });

                    alertDialogBuilder.setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String inputText = userInput.getText().toString();

                            if (inputText.equals("")) {
                                selectedOption = 0;
                            }
                            else {
                                selectedOption = Integer.parseInt(inputText);
                            }
                            removeWater(selectedOption);
                            setNonSelected();
                        }
                    });

                    alertDialogBuilder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNonSelected();
                        }
                    });

                    alertDialogBuilder.create();
                    alertDialogBuilder.show();

                }

            });

            return rootView;

        }

        public void setSelected (LinearLayout layout) {
            setNonSelected();
            layout.setBackgroundColor(selectedColor);

        }

        public void setNonSelected (){
            option1.setBackground(option1Background);
            option2.setBackground(option2Background);
            option3.setBackground(option3Background);
            option4.setBackground(option4Background);
            option5.setBackground(option5Background);
            optionCustom.setBackground(optionCustomBackground);
        }

        public void addWater(int waterToAdd) {
            if (waterToAdd == 0){
                Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_LONG).show();
                return;
            }
            current_water_intake += waterToAdd;
            updateWaterDisplay();
            Toast.makeText(getContext(), waterToAdd + " ml of water has been added!" ,
                    Toast.LENGTH_LONG).show();

            if(itemRepository != null)
            {
                int date = MainActivity.getDate();
                itemRepository.updateItem(steps, current_water_intake, date);
            }
            selectedOption = 0;

        }


        public void updateWaterDisplay() {
            if (current_water_intake > 50000) {
                CharSequence waterSequence = "You drank 50000+ / " + goal_intake + "mL of water today!";
                currentWaterTV.setText(waterSequence);

            }
            else{
                CharSequence waterSequence = "You drank " + current_water_intake + " / " + goal_intake + "mL of water today!";
                currentWaterTV.setText(waterSequence);
            }
            double progress = ((double) current_water_intake /(double) goal_intake) * 100;
            dispatchNotification();
            if (progress > 200) {
                progressCircle.setProgress((int)progress);
                progressText.setText("200+% ");
            }
            else {
                progressCircle.setProgress((int) progress);
                progressText.setText((int) progress + "%");
            }

        }


        public void removeWater (int waterToRemove) {
            if (waterToRemove == 0){
                Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_LONG).show();
                return;
            }
            current_water_intake = current_water_intake - waterToRemove;

            if (current_water_intake < 0 ) {
                current_water_intake = 0;
            }
            updateWaterDisplay();
            Toast.makeText(getContext(), waterToRemove + " ml of water has been removed!" ,
                    Toast.LENGTH_LONG).show();

            if(itemRepository != null)
            {
                int date = MainActivity.getDate();
                itemRepository.updateItem(steps, current_water_intake, date);
            }
            selectedOption = 0;
        }

        public void clearWaterData () {
            current_water_intake = 0;
            updateWaterDisplay();
            Toast.makeText(getContext(), " water data for today has been removed!" ,
                    Toast.LENGTH_LONG).show();

            if(itemRepository != null)
            {
                int date = MainActivity.getDate();
                itemRepository.updateItem(steps, current_water_intake, date);
            }
            selectedOption = 0;
        }

        public int getGoalIntake(){
            LiveData<Item> goalItem = itemRepository.getGoals();
            goalItem.observe(this, item1 -> {
                if (item1 != null) {
                    goal_intake = item1.getWater();
                }

            });
            return goal_intake;
         }

    public void dispatchNotification() {
        double progress = ((double) current_water_intake / (double) goal_intake) * 100;
        if (progress >= 100) {
            isGoalMet = true;
            System.out.println("key Progress is: " + progress);
            System.out.println("key water intake is: " + current_water_intake +
                    "goal intake is: " + goal_intake);
            System.out.println("key: pulled fro mdatabase, goal intake isL " + getGoalIntake());
            System.out.println("key: isGoalMet has a value of: " + isGoalMet);
            Intent sendLevel = new Intent();
            sendLevel.setAction("GET_WATER");
            this.getContext().sendBroadcast(sendLevel);

            AlarmReceiver.createWaterNotificationChannel(getContext());
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(getContext(), WATERCHANNELID)
                    .setContentTitle("Congratulations")
                    .setContentText("You have reached your daily water goal!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notif = builder2.setOngoing(false)
                    .setSmallIcon(R.drawable.water)
                    .build();
            NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(getContext());
            notificationManager2.notify(1, notif);
        }
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
                itemRepository.updateWaterItem(current_water_intake, date);
                //dispatchNotification();
            }
        }


}