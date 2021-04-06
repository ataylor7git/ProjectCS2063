package ca.unb.mobiledev.projectcs2063;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.unb.mobiledev.projectcs2063.entity.Item;
import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "INFO Main Activity";

    private BottomNavigationView bottomNavigation;
    private Toolbar toolbar;
    private ItemRepository itemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "on create called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openFragment(StepsFragment.newInstance("", ""));
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        itemRepository = new ItemRepository(getApplication());
        LiveData<Item> item = itemRepository.getGoals();
        item.observe(this, item1 -> {
            if(item1 == null)
                itemRepository.insertRecord(-1, 10000,2000);
        });
        WaterFragment.setRepository(itemRepository);
        StepsFragment.setRepository(itemRepository);
        UserFragment.setRepository(itemRepository);

    }

    public void openFragment(Fragment fragment) {
        Log.i(TAG, "open fragment called");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Log.i(TAG, "listener actions called");

                    switch (item.getItemId()) {
                        case R.id.steps:
                            openFragment(StepsFragment.newInstance("", ""));
                            toolbar.setTitle(R.string.steps_name);
                            return true;
                        case R.id.water:
                            openFragment(WaterFragment.newInstance("", ""));
                            toolbar.setTitle(R.string.water_name);
                            return true;
                        case R.id.user:
                            openFragment(UserFragment.newInstance("", ""));
                            toolbar.setTitle(R.string.user_name);
                            return true;
                    }
                    return false;
                }
            };


    public static int getDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyy", Locale.getDefault());
        String formattedDate = df.format(c);
        int date = Integer.parseInt(formattedDate);
        return date;
    }
}