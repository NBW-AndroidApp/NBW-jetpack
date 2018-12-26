package org.nieghborhoodbikeworks.nbw.ui.queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nieghborhoodbikeworks.nbw.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueHolder> {
    private LayoutInflater inflater;
    private List<String> mUsers;

    public static class QueueHolder extends RecyclerView.ViewHolder {
        public TextView user;
        public QueueHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
        }
    }

    public QueueAdapter(Context context, List<String> mUsers) {
        inflater = LayoutInflater.from(context);
        this.mUsers = mUsers;
    }

    @Override
    public QueueAdapter.QueueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.queued_user, parent, false);
        QueueHolder vh = new QueueHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QueueHolder holder, int position) {
        String currentUser = mUsers.get(position);
        holder.user.setText(currentUser);
        //holder.profilePic.setImageResource();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

}
