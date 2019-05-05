package com.iu.feedback.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iu.feedback.R;
import com.iu.feedback.model.Feedback;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    Activity activity;
    private List<Feedback> feedbackList;
    private FeedbackAdapter.FeedbackClickListener mClickListener;

    public FeedbackAdapter(Activity act, List<Feedback> contacts, FeedbackAdapter.FeedbackClickListener clickListener) {

        this.feedbackList = contacts;
        this.mClickListener = clickListener;
        activity = act;
    }

    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_form, parent, false);
        // Set the view to the ViewHolder
        FeedbackAdapter.ViewHolder holder = new FeedbackAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FeedbackAdapter.ViewHolder holder, final int i) {
        // Log.d(TAG, "onBindViewHolder(" + holder.getAdapterPosition() + ", " + i + ")");
        Feedback form = feedbackList.get(i);
        holder.name.setText(form.getName().length() > 0 ? form.getName() : "Anonymous");

        if (form.getContact().length() > 0) {
            holder.circleOne.setVisibility(View.VISIBLE);
            holder.contact.setVisibility(View.VISIBLE);
            holder.contact.setText(form.getContact());
        }

        if (form.getEmail().length() > 0) {
            holder.circleTwo.setVisibility(View.VISIBLE);
            holder.email.setVisibility(View.VISIBLE);
            holder.email.setText(form.getEmail());
        }

        holder.description.setText(form.getDate());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onFeedbackClick(feedbackList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public interface FeedbackClickListener {
        void onFeedbackClick(Feedback feedback);
    }

    // Create the ViewHolder class to keep references to your views
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        View circleOne, circleTwo;
        private TextView name, description, contact, email;

        private ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            contact = view.findViewById(R.id.contact);
            email = view.findViewById(R.id.email);
            mainLayout = view.findViewById(R.id.main_layout);
            description = view.findViewById(R.id.description);

            circleOne = view.findViewById(R.id.circle_one);
            circleTwo = view.findViewById(R.id.circle_two);
        }
    }
}


