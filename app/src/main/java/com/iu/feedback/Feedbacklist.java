package com.iu.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iu.feedback.adapter.FeedbackAdapter;
import com.iu.feedback.database.MyDataSource;
import com.iu.feedback.model.Feedback;
import com.iu.feedback.model.Form;


public class Feedbacklist extends AppCompatActivity {
    LinearLayout errorLayout;
    TextView errorText;
    RecyclerView recyclerView;
    FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.from_list);


        errorLayout = findViewById(R.id.error_layout);
        errorText = findViewById(R.id.error_text);
        recyclerView = findViewById(R.id.RecyclerView);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Form form = getIntent().getParcelableExtra("form");
        final MyDataSource myDataSource = new MyDataSource(getApplicationContext());


        feedbackAdapter = new FeedbackAdapter(this, myDataSource.GetFeedback(form.getFormId()), new FeedbackAdapter.FeedbackClickListener() {
            @Override
            public void onFeedbackClick(Feedback feedback) {
                feedback = myDataSource.GetFeedbackDetail(feedback);
                Intent intent = new Intent(Feedbacklist.this, ViewFeedback.class);
                intent.putExtra("form", form);
                intent.putExtra("feedback", feedback);
                startActivity(intent);

            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Feedbacklist.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(feedbackAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
