package com.iu.feedback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iu.feedback.adapter.FormAdapter;
import com.iu.feedback.database.MyDataSource;
import com.iu.feedback.model.Form;


public class FormList extends AppCompatActivity {
    LinearLayout errorLayout;
    TextView errorText;
    RecyclerView recyclerView;
    FormAdapter formAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.from_list);

        errorLayout = findViewById(R.id.error_layout);
        errorText = findViewById(R.id.error_text);
        recyclerView = findViewById(R.id.RecyclerView);
        getSupportActionBar().setTitle("Forms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MyDataSource myDataSource = new MyDataSource(getApplicationContext());
        formAdapter = new FormAdapter(this, myDataSource.getForms(), new FormAdapter.FormClickListener() {
            @Override
            public void onFormClick(Form form) {
                Dialog(form);

            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FormList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(formAdapter);
    }

    private void Dialog(final Form form) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(FormList.this, R.style.AlertDialogCustom));
        builder.setTitle("Please chose an action");
        String[] animals = {"Edit", "Take Feedback", "View Feedbacks"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent edit = new Intent(FormList.this, FormEdit.class);
                        edit.putExtra("form", form);
                        startActivity(edit);
                        break;
                    case 1:
                        Intent intent = new Intent(FormList.this, FeedbackOverview.class);
                        intent.putExtra("form", form);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent list = new Intent(FormList.this, Feedbacklist.class);
                        list.putExtra("form", form);
                        startActivity(list);
                        break;
                    default:
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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

