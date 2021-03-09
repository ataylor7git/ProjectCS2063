package ca.unb.mobiledev.projectcs2063;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "INFO Main Activity";

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "on create called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openFragment(StepsFragment.newInstance("", ""));
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

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
                            case R.id.water:
                                openFragment(WaterFragment.newInstance("", ""));
                                return true;
                            case R.id.steps:
                                openFragment(StepsFragment.newInstance("", ""));
                                return true;
                            case R.id.user:
                                openFragment(UserFragment.newInstance("", ""));
                                return true;
                        }
                        return false;
                    }
                };
    }