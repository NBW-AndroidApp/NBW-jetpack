package org.nieghborhoodbikeworks.nbw.ui.queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nieghborhoodbikeworks.nbw.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> mUsers;

    /**
     * The adapter populates the data into the RecyclerView by converting an object at a position
     * into a list row item to be inserted. The adapter requires the existence of a "ViewHolder"
     * object which describes and provides access to all the views within each item row. In our case,
     * each item row is composed of CardViews.
     *
     * @param context
     * @param mUsers
     */
    public QueueAdapter(Context context, ArrayList<String> mUsers) {
        inflater = LayoutInflater.from(context);
        this.mUsers = mUsers;
    }

    public static class QueueHolder extends RecyclerView.ViewHolder {
        public TextView user;

        public QueueHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
        }

    }

    @Override
    public QueueAdapter.QueueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates the XML layout file that will be used for each row within the list
        View view = inflater.inflate(R.layout.queued_user, parent, false);
        return new QueueHolder(view);
    }

    /**
     * Sets the view attributes based on the data.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull QueueHolder holder, final int position) {
        String currentUser = mUsers.get(position);
        holder.user.setText(currentUser);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

}
