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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ca.unb.mobiledev.projectcs2063.entity.Item;
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

    private TextInputEditText sGoalInput, wGoalInput;
    private TextInputEditText nameET, weightET, heightET, ageET;
    private TextView title;
    private String name = "";
    private String weight, height, age = "";

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

        title = rootView.findViewById(R.id.title);

        //The step goal edit text
        TextInputLayout sGoalLayout = rootView.findViewById(R.id.step_goal);
        sGoalInput = (TextInputEditText) rootView.findViewById(R.id.step_goalET);
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
        TextInputLayout wGoalLayout = rootView.findViewById(R.id.water_goal);
        wGoalInput = (TextInputEditText) rootView.findViewById(R.id.water_goalET);
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

        TextInputLayout nameLayout = rootView.findViewById(R.id.name);
        nameET = (TextInputEditText) rootView.findViewById(R.id.nameET);
        nameET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                if(nameET.getText().toString().isEmpty() )
                {
                    name = "";
                    System.out.println("Empty");
                    title.setText("Settings");
                }
                else {
                    String nameIn = nameET.getText().toString();
                    System.out.println("nameIn is : " + nameIn);
                    name = nameIn;
                    System.out.println("Title Name is: " + name);
                    title.setText(name + "'s Settings");
                }
                changed = true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        TextInputLayout weightLayout = rootView.findViewById(R.id.weight);
        weightET = (TextInputEditText) rootView.findViewById(R.id.weightET);
        weightET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                if(weightET.getText().toString().equals("") )
                {
                    weight = "";
                    weightLayout.setError("");
                }
                else if (weightET.getText().toString().equals("0")) {
                    weightLayout.setError("Input cannot be zero!");
                    weight = "";
                }
                else {
                    weightLayout.setError("");
                    String weightIn = weightET.getText().toString();
                    weight = weightIn;
                }
                changed = true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        TextInputLayout heightLayout = rootView.findViewById(R.id.height);
        heightET = (TextInputEditText) rootView.findViewById(R.id.heightET);
        heightET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                if(heightET.getText().toString().equals(""))
                {
                    height = "";
                    heightLayout.setError("");
                }
                else if (heightET.getText().toString().equals("0")) {
                    heightLayout.setError("Input cannot be zero!");
                    height = "";
                }
                else {
                    heightLayout.setError("");
                    String heightIn = heightET.getText().toString();
                    height = heightIn;
                }
                changed = true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        TextInputLayout ageLayout = rootView.findViewById(R.id.age);
        ageET = (TextInputEditText) rootView.findViewById(R.id.ageET);
        ageET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                if(ageET.getText().toString().equals(""))
                {
                    age = "";
                    ageLayout.setError("");
                }
                else if (ageET.getText().toString().equals("0")) {
                    ageLayout.setError("Input cannot be zero!");
                    age = "";
                }
                else {
                    ageLayout.setError("");
                    String ageIn = ageET.getText().toString();
                    age = ageIn;
                }
                changed = true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        LiveData<Item> item = itemRepository.getGoals();
        item.observe(this, item1 -> {
            if(item1 != null) {
                stepGoal = item1.getSteps();
                waterGoal = item1.getWater();
                name = item1.getName();
                System.out.println("name in  item observe is: " + name);
                age = item1.getAge();
                weight = item1.getWeight();
                height = item1.getHeight();

                CharSequence goalSequence = stepGoal + "";
                sGoalInput.setText(goalSequence);

                goalSequence = waterGoal + "";
                wGoalInput.setText(goalSequence);

                goalSequence = name;
                nameET.setText(goalSequence);

                goalSequence = age;
                ageET.setText(goalSequence);

                goalSequence = weight;
                weightET.setText(goalSequence);

                goalSequence = height;
                heightET.setText(goalSequence);
            }
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
            itemRepository.updateItemAll(stepGoal, waterGoal, name, weight, height, age, false, -1);
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