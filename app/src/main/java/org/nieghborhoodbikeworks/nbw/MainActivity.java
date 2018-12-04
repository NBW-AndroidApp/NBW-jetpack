package org.nieghborhoodbikeworks.nbw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.nieghborhoodbikeworks.nbw.ui.login.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, LoginFragment.newInstance())
//                    .commitNow();
//        }
    }


}
