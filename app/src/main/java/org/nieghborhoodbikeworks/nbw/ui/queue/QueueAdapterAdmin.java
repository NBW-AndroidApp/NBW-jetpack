package org.nieghborhoodbikeworks.nbw.ui.queue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nieghborhoodbikeworks.nbw.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

public class QueueAdapterAdmin extends QueueSelectableAdapter<QueueAdapterAdmin.QueueHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> mUsers;
    private LongClickListener longClickListener;

    /**
     * The adapter populates the data into the RecyclerView by converting an object at a position
     * into a list row item to be inserted. The adapter requires the existence of a "ViewHolder"
     * object which describes and provides access to all the views within each item row. In our case,
     * each item row is composed of CardViews.
     *
     * @param context
     * @param mUsers
     */
    public QueueAdapterAdmin(Context context, ArrayList<String> mUsers,
                             LongClickListener clickListener) {
        inflater = LayoutInflater.from(context);

        this.mUsers = mUsers;
        this.longClickListener = clickListener;
    }

    public static class QueueHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView user;
        public int primaryColor;
        private LongClickListener longClickListener;

        public QueueHolder(View itemView, LongClickListener longClickListener) {
            super(itemView);

            this.longClickListener = longClickListener;

            user = itemView.findViewById(R.id.user);
            primaryColor = itemView.getResources().getColor(R.color.colorPrimary);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
//            if (longClickListener != null) {
                Log.d(TAG, "Item long-clicked at position " + getPosition());
                return longClickListener.onItemLongClicked(getPosition());
//            }

//            return false;
        }
    }

    @Override
    public QueueAdapterAdmin.QueueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates the XML layout file that will be used for each row within the list
        View view = inflater.inflate(R.layout.queued_user, parent, false);
        return new QueueHolder(view, this.longClickListener);
    }

    /**
     * Sets the view attributes based on the data
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull QueueHolder holder, int position) {
        String currentUser = mUsers.get(position);
        holder.user.setText(currentUser);
        if (isSelected(position)) {
            holder.user.setTextColor(holder.primaryColor);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    interface LongClickListener {
        boolean onItemLongClicked(int position);
    }

}