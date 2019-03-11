package org.nieghborhoodbikeworks.nbw;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    public NavController mNavController;
    public Toolbar toolbar;
    private SharedViewModel mViewModel;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        mUser = mViewModel.getUser();

        setSupportActionBar(toolbar);

        setupNavigation();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mDrawerLayout, Navigation.findNavController(this, R.id.nav_host_fragment));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Setting Up One Time Navigation
    private void setupNavigation(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.nav_view);

        mNavController = findNavController(this, R.id.nav_host_fragment);

        setupActionBarWithNavController(this, mNavController, mDrawerLayout);

        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_login: {
                        mNavController.navigate(R.id.loginFragment);
                        break;
                    }
                    case R.id.nav_logout: {
                        FirebaseAuth.getInstance().signOut();
                        mUser = null;
                        Toast.makeText(MainActivity.this, "Logged out successfully!",
                                Toast.LENGTH_SHORT).show();
                        mNavigationView.getMenu().setGroupVisible(R.id.signedOut, true);
                        mNavigationView.getMenu().setGroupVisible(R.id.signedIn, false);
                        break;
                    }
                    case R.id.nav_user_choice: {
                        mNavController.navigate(R.id.userChoiceFragment);
                        break;
                    }
                    case R.id.nav_waiver: {
                        mNavController.navigate(R.id.waiverFragment);
                        break;
                    }
                    case R.id.nav_orientation: {
                        mNavController.navigate(R.id.orientationFragment);
                        break;
                    }
                    case R.id.nav_queue: {
                        if(mUser != null && mUser.isAdmin()) {
                            mNavController.navigate(R.id.queueAdminFragment);
                        } else {
                            mNavController.navigate(R.id.queueFragment);
                        }
                        break;
                    }
                    case R.id.nav_map: {
                        mNavController.navigate(R.id.mapFragment);
                        break;
                    }
                }
                return true;
            }
        });
    }

    // Method for fragments to set their own titles on relevant screens
    public void setActionBarTitle(String title) {
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}