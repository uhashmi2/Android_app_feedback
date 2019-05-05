package com.iu.feedback.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iu.feedback.R;
import com.iu.feedback.model.Form;

import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.ViewHolder> {

    Activity activity;
    private List<Form> formList;
    private FormAdapter.FormClickListener mClickListener;

    public FormAdapter(Activity act, List<Form> contacts, FormAdapter.FormClickListener clickListener) {

        this.formList = contacts;
        this.mClickListener = clickListener;
        activity = act;
    }

    @Override
    public FormAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_form, parent, false);
        // Set the view to the ViewHolder
        FormAdapter.ViewHolder holder = new FormAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FormAdapter.ViewHolder holder, final int i) {
        // Log.d(TAG, "onBindViewHolder(" + holder.getAdapterPosition() + ", " + i + ")");
        Form form = formList.get(i);
        holder.name.setText(form.getTitle());
        holder.description.setText(form.getDescription());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onFormClick(formList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }

    public interface FormClickListener {
        void onFormClick(Form form);
    }

    // Create the ViewHolder class to keep references to your views
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        private TextView name, description;

        private ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            mainLayout = view.findViewById(R.id.main_layout);
            description = view.findViewById(R.id.description);
        }
    }
}


