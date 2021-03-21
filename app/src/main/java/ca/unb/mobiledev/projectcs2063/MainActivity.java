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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "INFO Main Activity";
    final FragmentManager fm = getSupportFragmentManager();
    final Fragment fragment3 = new UserFragment();
    final Fragment fragment2 = new WaterFragment();
    final Fragment fragment1 = new StepsFragment();
    Fragment active = fragment1;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "on create called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment3).commit();
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
    }