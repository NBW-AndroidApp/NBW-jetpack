package org.nieghborhoodbikeworks.nbw.ui.postsignin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserChoiceFragment extends Fragment {
    private SharedViewModel mViewModel;
    private NavigationView mNavigationView;
    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mFragments;

    public static UserChoiceFragment newInstance() { return new UserChoiceFragment(); }

    /**
     * This initializes the UI variables once the fragment starts up, and returns the view
     * to its parent.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("User Choice Fragment");
        // Get the view from fragment XML
        view = inflater.inflate(R.layout.user_choice_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        ((DrawerLocker)getActivity()).setDrawerLocked(true);
        mNavigationView = getActivity().findViewById(R.id.nav_view);

        mFragments = new ArrayList<>();
        mFragments.add("Queue");
        mFragments.add("Waiver");
        mFragments.add("Orientation");
        mFragments.add("Map");

        // Initialize queue UI elements
        mRecyclerView = view.findViewById(R.id.user_choice_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new UserChoiceAdapter(getActivity(), mFragments);
        mRecyclerView.setAdapter(mAdapter);
    }
}
