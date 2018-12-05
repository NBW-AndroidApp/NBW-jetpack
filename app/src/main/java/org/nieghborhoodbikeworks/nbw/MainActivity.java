package org.nieghborhoodbikeworks.nbw;

import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, LoginFragment.newInstance())
//                    .commitNow();
//        }
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

                        switch (menuItem.getItemId()) {
                            case R.id.nav_login: {
                                navController.navigate(R.id.loginFragment);
                            }
                            case R.id.nav_waiver: {
                                navController.navigate(R.id.waiverFragment);
                            }
                            case R.id.nav_map: {
                                navController.navigate(R.id.mapFragment);
                            }
                        }

                        return true;
                    }
                });
    }

}
