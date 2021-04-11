package ca.unb.mobiledev.projectcs2063;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.text.style.BackgroundColorSpan;
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

import ca.unb.mobiledev.projectcs2063.entity.Item;
import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;


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
    //private EditText addWaterTN;
    private Button addButton;
    private Button removeButton;
    private Button clearButton;

    private TextView dateDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private int goal_intake = 2000;
    private int current_water_intake = 0;
    private int selectedOption = 0;



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
        View rootView = inflater.inflate(R.layout.fragment_water, container, false);


        currentWaterTV = (TextView) rootView.findViewById(R.id.currentWaterTV);
        progressCircle = (ProgressBar) rootView.findViewById(R.id.waterProgressBar);
        progressText = (TextView) rootView.findViewById(R.id.progressWaterTV);
        //addWaterTN = (EditText) rootView.findViewById(R.id.addWaterTN);
        addButton = (Button) rootView.findViewById(R.id.add_drink_button);
        removeButton = rootView.findViewById(R.id.remove_drink_button);
        clearButton = rootView.findViewById(R.id.clear_drink_button);

        //display the date
        dateDisplay = rootView.findViewById(R.id.header_title);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE MMM d, yyyy" );
        date = dateFormat.format(calendar.getTime());
        dateDisplay.setText(date);


        LinearLayout option1 = rootView.findViewById(R.id.option1);
        LinearLayout option2 =  rootView.findViewById(R.id.option2);
        LinearLayout option3 = rootView.findViewById(R.id.option3);
        LinearLayout option4 = rootView.findViewById(R.id.option4);
        LinearLayout option5 = rootView.findViewById(R.id.option5);
        LinearLayout optionCustom = rootView.findViewById(R.id.optionCustom);



        //(for now) if the custom TN is empty, then take the selected option isntead and add it
        //through the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String add_water = addWaterTN.getText().toString();
                //if (add_water.isEmpty()){
                    //add_water = selectedOption + "";
                    System.out.println("selected option: " + selectedOption);
                    //System.out.println("add water: " + add_water);
                //}

                addWater(selectedOption);
                //current_water_intake += selectedOption;

            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println("selected option: " + selectedOption);
                removeWater(selectedOption);

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder clearAlert = new AlertDialog.Builder(getContext());
                clearAlert.setMessage("Warning! This will delete your data for the day!");
                clearAlert.setCancelable(true);

                clearAlert.setPositiveButton("Delete my data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearWaterData();
                    }
                });

                clearAlert.setNeutralButton(android.R.string.no, null);
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

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked! previous selected option is: " + selectedOption);
                selectedOption = 50;
                System.out.println("Clicked! new selected option is: " + selectedOption);
                option1.setBackgroundColor(Color.rgb(60, 80, 100));


            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 100;
                //option2.setBackgroundColor(Color.rgb(65, 85, 110));
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 150;
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 200;
            }
        });

        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 250;
            }
        });

        optionCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                    }
                });

                alertDialogBuilder.setNeutralButton(android.R.string.no, null);

                alertDialogBuilder.create();
                alertDialogBuilder.show();


            }

        });


        return rootView;


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
        CharSequence waterSequence = "You Drank " + current_water_intake + " / " + goal_intake + "mL of Water Today";
        currentWaterTV.setText(waterSequence);
        //addWaterTN.setText("");
        double progress = ((double) current_water_intake /(double) goal_intake) * 100;
        progressCircle.setProgress((int)progress);
        progressText.setText((int)progress + "%");
    }

    public void removeWater (int waterToRemove) {
        if (waterToRemove == 0){
            Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_LONG).show();
            return;
        }
        //System.out.println("before removing: "  + current_water_intake);
        current_water_intake = current_water_intake - waterToRemove;
        //System.out.println("after removing: "  + current_water_intake);


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
        }
    }

}