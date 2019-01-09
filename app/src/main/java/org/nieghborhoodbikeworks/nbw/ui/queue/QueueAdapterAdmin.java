package org.nieghborhoodbikeworks.nbw.ui.queue;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nieghborhoodbikeworks.nbw.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

public class QueueAdapterAdmin extends QueueSelectableAdapter<QueueAdapterAdmin.QueueHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> mUsers;
    private ClickListener clickListener;

    /**
     * The adapter populates the data into the RecyclerView by converting an object at a position
     * into a list row item to be inserted. The adapter requires the existence of a "ViewHolder"
     * object which describes and provides access to all the views within each item row. In our case,
     * each item row is composed of CardViews.
     *
     * @param context
     * @param mUsers
     */
    public QueueAdapterAdmin(Context context, ArrayList<String> mUsers, ClickListener clickListener) {
        inflater = LayoutInflater.from(context);

        this.mUsers = mUsers;
        this.clickListener = clickListener;
    }

    public static class QueueHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        private TextView user;
        public int defaultColor;
        public int primaryColor;
        private ClickListener clickListener;

        public QueueHolder(View itemView, ClickListener clickListener) {
            super(itemView);

            this.clickListener = clickListener;

            user = itemView.findViewById(R.id.user);
            primaryColor = itemView.getResources().getColor(R.color.colorPrimary);
            defaultColor = itemView.getResources().getColor(R.color.colorDefault);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                Log.d(TAG, "Item clicked at position " + getPosition());
                clickListener.onItemClicked(getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (clickListener != null) {
                Log.d(TAG, "Item long-clicked at position " + getPosition());
                return clickListener.onItemLongClicked(getPosition());
            }
            return false;
        }
    }

    @Override
    public QueueAdapterAdmin.QueueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates the XML layout file that will be used for each row within the list
        View view = inflater.inflate(R.layout.queued_user, parent, false);
        return new QueueHolder(view, this.clickListener);
    }

    /**
     * Sets the view attributes based on the data.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull QueueHolder holder, int position) {
        String currentUser = mUsers.get(position);
        holder.user.setText(currentUser);
        // If the user is selected, set their name to a different color.
        holder.user.setTextColor(isSelected(position) ? holder.primaryColor : holder.defaultColor);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * Remove the user at position 'position'.
     *
     * @param position
     */
    public void removeItem(int position) {
        mUsers.remove(position);
        notifyItemRemoved(position);
    }
    
    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals
                        (positions.get(count - 1) - 1)) {
                    count++;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            mUsers.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }


    /**
     * Interface to listen for admin clicks.
     */
    interface ClickListener {
        void onItemClicked(int position);
        boolean onItemLongClicked(int position);
    }

}