package com.iu.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iu.feedback.model.Form;


public class FeedbackOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_overview);


        TextView continueText = findViewById(R.id.continu);
        TextView dismiss = findViewById(R.id.dismiss);
        final Form form = getIntent().getParcelableExtra("form");
        continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackOverview.this, TakeFeedbackStepByStep.class);
                intent.putExtra("form", form);
                startActivity(intent);
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
