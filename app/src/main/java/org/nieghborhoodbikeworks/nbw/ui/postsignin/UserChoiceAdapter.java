package org.nieghborhoodbikeworks.nbw.ui.postsignin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class UserChoiceAdapter extends RecyclerView.Adapter<UserChoiceAdapter.UserChoiceHolder>{

    private SharedViewModel mViewModel;
    private LayoutInflater inflater;
    private ArrayList<String> mFragments;
    private UserChoiceAdapter.OnItemClicked onClick;

    interface OnItemClicked {
        void onItemClicked(int position);
    }

    public static abstract class UserChoiceHolder extends RecyclerView.ViewHolder {
        public UserChoiceHolder(View itemView) {
            super(itemView);
        }
    }

        public static class QueueFragmentViewHolder extends UserChoiceHolder {
            private Button enqueueButton;
            private Button dequeueButton;
            private Button viewQueueButton;

            public QueueFragmentViewHolder(View view) {
                super(view);
                enqueueButton = view.findViewById(R.id.card_view_enqueue_button);
                dequeueButton = view.findViewById(R.id.card_view_dequeue_button);
                viewQueueButton = view.findViewById(R.id.card_view_queue_button);
            }

            public void bindData() {
                enqueueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Next line
                        // this.mViewModel.enqueue();
                    }
                });
            }

        }

        public static class WaiverFragmentViewHolder extends UserChoiceHolder {
            private Button viewWaiverButton;

            public WaiverFragmentViewHolder(View view) {
                super(view);
                viewWaiverButton = view.findViewById(R.id.card_view_waiver_button);
            }

            public void bindData() {
                viewWaiverButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Next line
                        // Navigation.findNavController(view).navigate(R.id.waiverFragment);
                    }
                });
            }

        }

        public static class OrientationFragmentViewHolder extends UserChoiceHolder {
            private Button viewOrientationButton;

            public OrientationFragmentViewHolder(View view) {
                super(view);
                viewOrientationButton = view.findViewById(R.id.card_view_orientation_button);
            }

            public void bindData() {
                viewOrientationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Next line
                        // Navigation.findNavController(view).navigate(R.id.orientationFragment);
                    }
                });
            }

        }

        public static class MapFragmentViewHolder extends UserChoiceHolder {
            private Button navigateButton;

            public MapFragmentViewHolder(View view) {
                super(view);
                navigateButton = view.findViewById(R.id.card_view_navigate_button);
            }

            public void bindData() {
                navigateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Next line
                        // Navigation.findNavController(view).navigate(R.id.mapFragment);
                    }
                });
            }

        }

    /**
     * The adapter populates the data into the RecyclerView by converting an object at a position
     * into a list row item to be inserted. The adapter requires the existence of a "ViewHolder"
     * object which describes and provides access to all the views within each item row. In our case,
     * each item row is composed of TextViews.
     *
     * @param context
     * @param mFragments
     */
    public UserChoiceAdapter(Context context, ArrayList<String> mFragments, SharedViewModel mViewModel) {
        this.mFragments = mFragments;
        this.mViewModel = mViewModel;
    }

    @Override
    public UserChoiceAdapter.UserChoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates the XML layout file that will be used for each row within the list
        View view = null;
        UserChoiceAdapter.UserChoiceHolder vh = null;
        switch (viewType) {
            case 0:
                view = View.inflate(parent.getContext(), R.layout.queue_fragment_choice, null);
                vh = new QueueFragmentViewHolder(view);
                break;
            case 1:
                view = View.inflate(parent.getContext(), R.layout.waiver_fragment_choice, null);
                vh = new WaiverFragmentViewHolder(view);
                break;
            case 2:
                view = View.inflate(parent.getContext(), R.layout.orientation_fragment_choice, null);
                vh = new OrientationFragmentViewHolder(view);
                break;
            case 3:
                view = View.inflate(parent.getContext(), R.layout.map_fragment_choice, null);
                vh = new MapFragmentViewHolder(view);
                break;
        }
        return vh;
    }

    /**
     * Sets the view attributes based on the data
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull UserChoiceAdapter.UserChoiceHolder holder, final int position) {
        switch (position) {
            case 0:
                ((QueueFragmentViewHolder)holder).bindData();
                break;
            case 1:
                ((WaiverFragmentViewHolder)holder).bindData();
                break;
            case 2:
                ((OrientationFragmentViewHolder)holder).bindData();
                break;
            case 3:
                ((MapFragmentViewHolder)holder).bindData();
                break;
        }
//        holder.fragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClick.onItemClicked(position);
//            }
//        });
    }

    @Override
    public int getItemViewType(int position) {
        int result = 0;
        if (mFragments.get(position).equals("Queue")) {
            result = 0;
        } else if (mFragments.get(position).equals("Waiver")) {
            result = 1;
        } else if (mFragments.get(position).equals("Orientation")) {
            result = 2;
        } else if (mFragments.get(position).equals("Map")) {
            result = 3;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

    public void setOnClick(UserChoiceAdapter.OnItemClicked onClick) {
        this.onClick = onClick;
    }

}
