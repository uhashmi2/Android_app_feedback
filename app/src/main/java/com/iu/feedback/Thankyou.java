package com.iu.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iu.feedback.model.Form;

public class Thankyou extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyou);
        TextView continueText = findViewById(R.id.continu);
        continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentActivity();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentActivity();
            }
        }, 5000);
    }

    void IntentActivity() {
        Form form = getIntent().getParcelableExtra("form");
        Intent i = new Intent();
        i.putExtra("form", form);
        finish();
    }
}
