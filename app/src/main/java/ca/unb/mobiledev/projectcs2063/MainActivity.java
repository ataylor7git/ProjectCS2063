package ca.unb.mobiledev.projectcs2063;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    final FragmentManager fm = getSupportFragmentManager();
    final UserFragment fragment3 = new UserFragment();
    final WaterFragment fragment2 = new WaterFragment();
    final StepsFragment fragment1 = new StepsFragment();
    Fragment active = fragment1;

    BottomNavigationView bottomNavigation;

    private ItemRepository itemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "on create called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container, fragment1, "1").commit();



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
                                fm.beginTransaction().hide(active).show(fragment1).commit();
                                active = fragment1;
                                return true;
                            case R.id.water:
                                fm.beginTransaction().hide(active).show(fragment2).commit();
                                active = fragment2;
                                return true;
                            case R.id.user:
                                fm.beginTransaction().hide(active).show(fragment3).commit();
                                active = fragment3;
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