package org.nieghborhoodbikeworks.nbw.ui.postsignin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class UserChoiceAdapter extends RecyclerView.Adapter<UserChoiceAdapter.UserChoiceHolder>{

    private LayoutInflater inflater;
    private ArrayList<String> mFragments;

    /**
     * There are five ViewHolders, the first being the title of the UserChoiceFragment, and the
     * subsequent ones for each CardView in the RecyclerView, all instances of the UserChoiceHolder
     * abstract class.
     */
    public static abstract class UserChoiceHolder extends RecyclerView.ViewHolder {
        public UserChoiceHolder(View itemView) {
            super(itemView);
            }
    }

        // Making the title of the fragment a ViewHolder item instead of a TextView allows for
        // continuous scrolling of the CardViews and the title; using a TextView would result in a
        // "sticky" header
        public static class TitleViewHolder extends UserChoiceHolder {
            public TitleViewHolder(View view) {
                super(view);
            }
        }

        public static class QueueFragmentViewHolder extends UserChoiceHolder {
            private Button enqueueButton, dequeueButton, viewQueueButton;
            private View mView;
            private Context mContext;
            private SharedViewModel mViewModel;
            private User mUser;

            public QueueFragmentViewHolder(View view) {
                super(view);
                mView = view;
                mContext = mView.getContext();
                mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(SharedViewModel.class);
                mUser = mViewModel.getUser();
                enqueueButton = view.findViewById(R.id.card_view_enqueue_button);
                dequeueButton = view.findViewById(R.id.card_view_dequeue_button);
                viewQueueButton = view.findViewById(R.id.card_view_queue_button);
            }

            public void bindData() {
                enqueueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mViewModel.enqueueUser();
                            if (mUser.isAdmin()) {
                                Navigation.findNavController(mView).navigate(R.id.queueAdminFragment);
                            } else {
                                Navigation.findNavController(mView).navigate(R.id.queueFragment);
                            }
                        } catch (NullPointerException e) {
                            Bundle bundle = new Bundle();
                            bundle.putCharSequence("externalFragmentMessage", "enqueue");
                            Navigation.findNavController(mView).navigate(R.id.loginFragment, bundle);
                        }
                    }
                });
                dequeueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mViewModel.dequeueUser();
                            if (mUser.isAdmin()) {
                                Navigation.findNavController(mView).navigate(R.id.queueAdminFragment);
                            } else {
                                Navigation.findNavController(mView).navigate(R.id.queueFragment);
                            }
                        } catch (NullPointerException e) {
                            Bundle bundle = new Bundle();
                            bundle.putCharSequence("externalFragmentMessage", "dequeue");
                            Navigation.findNavController(mView).navigate(R.id.loginFragment, bundle);
                        }
                    }
                });
                viewQueueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (mUser.isAdmin()) {
                                Navigation.findNavController(mView).navigate(R.id.queueAdminFragment);
                            } else {
                                Navigation.findNavController(mView).navigate(R.id.queueFragment);
                            }
                        } catch (NullPointerException e) {
                            Bundle bundle = new Bundle();
                            bundle.putCharSequence("externalFragmentMessage", "seeQueue");
                            Navigation.findNavController(mView).navigate(R.id.loginFragment, bundle);
                        }
                    }
                });
            }
        }

        public static class WaiverFragmentViewHolder extends UserChoiceHolder {
            private Button viewWaiverButton;
            private View mView;
            private Context mContext;
            private SharedViewModel mViewModel;

            public WaiverFragmentViewHolder(View view) {
                super(view);
                mView = view;
                mContext = mView.getContext();
                mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(SharedViewModel.class);
                viewWaiverButton = view.findViewById(R.id.card_view_waiver_button);
            }

            public void bindData() {
                viewWaiverButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewModel.getUser() == null) {
                            Navigation.findNavController(mView).navigate(R.id.loginFragment);
                        } else {
                            Navigation.findNavController(mView).navigate(R.id.waiverFragment);
                        }
                    }
                });
            }
        }

        public static class OrientationFragmentViewHolder extends UserChoiceHolder {
            private Button viewOrientationButton;
            private View mView;

            public OrientationFragmentViewHolder(View view) {
                super(view);
                mView = view;
                viewOrientationButton = view.findViewById(R.id.card_view_orientation_button);
            }

            public void bindData() {
                viewOrientationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         Navigation.findNavController(mView).navigate(R.id.orientationFragment);
                    }
                });
            }
        }

        public static class MapFragmentViewHolder extends UserChoiceHolder {
            private Button mapButton, navigateButton;
            private View mView;

            public MapFragmentViewHolder(View view) {
                super(view);
                mView = view;
                mapButton = view.findViewById(R.id.card_view_map_button);
                navigateButton = view.findViewById(R.id.card_view_navigate_button);
            }

            public void bindData() {
                mapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.findNavController(mView).navigate(R.id.mapFragment);
                    }
                });
                navigateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q=3939 Lancaster Avenue, Philadelphia, PA"));
                        mapsIntent.setPackage("com.google.android.apps.maps");
                        mView.getContext().startActivity(mapsIntent);
                    }
                });
            }
        }

    /**
     * The adapter populates the data into the RecyclerView by converting an object at a position
     * into a list row item to be inserted. The adapter requires the existence of a "ViewHolder"
     * object which describes and provides access to all the views within each item row. In our case,
     * each item row is composed of CardViews.
     *
     * @param context
     * @param mFragments
     */
    public UserChoiceAdapter(Context context, ArrayList<String> mFragments) {
        inflater = LayoutInflater.from(context);
        this.mFragments = mFragments;
    }

    @NonNull
    @Override
    public UserChoiceAdapter.UserChoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the XML layout file that will be used for each row within the list
        View view = null;
        UserChoiceAdapter.UserChoiceHolder vh = null;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.user_choice_fragment_title, parent, false);
                vh = new TitleViewHolder(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.queue_fragment_choice, parent, false);
                vh = new QueueFragmentViewHolder(view);
                break;
            case 2:
                view = inflater.inflate(R.layout.waiver_fragment_choice, parent, false);
                vh = new WaiverFragmentViewHolder(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.orientation_fragment_choice, parent, false);
                vh = new OrientationFragmentViewHolder(view);
                break;
            case 4:
                view = inflater.inflate(R.layout.map_fragment_choice, parent, false);
                vh = new MapFragmentViewHolder(view);
                break;
        }
        return vh;
    }

    /**
     * Sets the view attributes based on the data.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull UserChoiceAdapter.UserChoiceHolder holder, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                ((QueueFragmentViewHolder)holder).bindData();
                break;
            case 2:
                ((WaiverFragmentViewHolder)holder).bindData();
                break;
            case 3:
                ((OrientationFragmentViewHolder)holder).bindData();
                break;
            case 4:
                ((MapFragmentViewHolder)holder).bindData();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int result = 0;
        switch(mFragments.get(position)) {
            case "Title":
                result = 0;
                break;
            case "Queue":
                result = 1;
                break;
            case "Waiver":
                result = 2;
                break;
            case "Orientation":
                result = 3;
                break;
            case "Map":
                result = 4;
                break;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

}
