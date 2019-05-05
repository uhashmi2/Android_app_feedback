package com.iu.feedback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.iu.feedback.database.MyDataSource;
import com.iu.feedback.model.FieldType;
import com.iu.feedback.model.Fields;
import com.iu.feedback.model.Form;

import java.util.ArrayList;
import java.util.Calendar;

public class FormEdit extends AppCompatActivity {
    TextView title, description;
    LinearLayout parentLayout;
    MyDataSource myDataSource;
    Form formObject;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myCalendar = Calendar.getInstance();
        formObject = getIntent().getParcelableExtra("form");
        title = findViewById(R.id.form_title);
        description = findViewById(R.id.description);
        parentLayout = findViewById(R.id.parent_layout);
        myDataSource = new MyDataSource(getApplicationContext());
        formObject = myDataSource.getFormDetail(formObject);
        title.setText(formObject.getTitle());
        description.setText(formObject.getDescription());


        for (int j = 0; j < formObject.getFieldsList().size(); j++) {

            final Fields field = formObject.getFieldsList().get(j);
            TextView fieldName = new TextView(getApplicationContext());
            fieldName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            fieldName.setPadding(30, 10, 20, 0);
            fieldName.setTag(field.getFieldId() + "," + field.getFormFieldId());
            fieldName.setText(field.getFieldHeading());
            parentLayout.addView(fieldName);
            if (field.getFieldId() == FieldType.Type.SELECT.toInteger() || field.getFieldId() == FieldType.Type.RADIO.toInteger() ||
                    field.getFieldId() == FieldType.Type.CHECKBOX.toInteger()) {
                ArrayList<String> listSpinner = new ArrayList<>();
                for (int i = 0; i < field.getFieldOptions().size(); i++) {
                    String option = field.getFieldOptions().get(i);
                    if (field.getFieldId() == FieldType.Type.SELECT.toInteger()) {
                        listSpinner.add(option);
                        if (i == field.getFieldOptions().size() - 1) {
                            ///////////////
                            Spinner spinner = new Spinner(getApplicationContext());
                            spinner.setPadding(50, 10, 20, 0);
                            ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(FormEdit.this, android.R.layout.simple_spinner_dropdown_item, listSpinner);
                            spinner.setAdapter(selectAdapter);
                            parentLayout.addView(spinner);
                        }

                    } else if (field.getFieldId() == FieldType.Type.RADIO.toInteger()) {
                        final RadioButton radiobutton = new RadioButton(getApplicationContext());
                        radiobutton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        radiobutton.setText(option);
                        radiobutton.setTag(field.getFieldHeading());
                        radiobutton.setClickable(false);
                        radiobutton.setLayoutParams(setMargin(false, 50, 10, 20, 10));
                        parentLayout.addView(radiobutton);
                    } else if (field.getFieldId() == FieldType.Type.CHECKBOX.toInteger()) {
                        CheckBox checkbox = new CheckBox(getApplicationContext());
                        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        checkbox.setText(option);
                        checkbox.setClickable(false);

                        checkbox.setLayoutParams(setMargin(false, 50, 10, 20, 10));
                        parentLayout.addView(checkbox);
                    }
                }
            } else {
                if (field.getFieldId() == FieldType.Type.TEXTAREA.toInteger()) {
                    EditText textArea = new EditText(getApplicationContext());
                    textArea.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textArea.setHint("Please enter the text area detail.");
                    textArea.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    textArea.setKeyListener(null);
                    textArea.setLayoutParams(setMargin(true, 50, 10, 40, 10));


                    parentLayout.addView(textArea);

                } else if (field.getFieldId() == FieldType.Type.TEXTFIELD.toInteger()) {

                    EditText textField = new EditText(getApplicationContext());
                    textField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textField.setHint("Please enter the text field detail.");
                    textField.setSingleLine(true);
                    textField.setLayoutParams(setMargin(true, 50, 10, 40, 10));
                    textField.setInputType(InputType.TYPE_NULL);
                    textField.setKeyListener(null);

                    parentLayout.addView(textField);
                } else if (field.getFieldId() == FieldType.Type.RATING.toInteger()) {

                    final LinearLayout horizontally = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    params.setMargins(0, 10, 0, 10);
                    horizontally.setLayoutParams(params);
                    horizontally.setOrientation(LinearLayout.HORIZONTAL);
                    for (int i = 0; i < 5; i++) {
                        final ImageView icon = new ImageView(getApplicationContext());
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        imageParams.weight = 0.2f;

                        if (i == 0) {
                            icon.setImageResource(R.drawable.selector_rate1);
                            icon.setTag("angry");
                        } else if (i == 1) {
                            icon.setImageResource(R.drawable.selector_rate2);
                            icon.setTag("not good");
                        } else if (i == 2) {
                            icon.setImageResource(R.drawable.selector_rate3);
                            icon.setTag("neutral");
                        } else if (i == 3) {
                            icon.setImageResource(R.drawable.selector_rate4);
                            icon.setTag("good");
                        } else {
                            icon.setImageResource(R.drawable.selector_rate5);
                            icon.setTag("awesome");
                        }
                        icon.setLayoutParams(imageParams);

                        horizontally.addView(icon);
                    }
                    parentLayout.addView(horizontally);


                } else if (field.getFieldId() == FieldType.Type.DATE.toInteger()) {

                    final Button dateButton = new Button(getApplicationContext());
                    dateButton.setText("Set the date");
                    dateButton.setClickable(false);
                    dateButton.setPadding(50, 10, 10, 10);
                    parentLayout.addView(dateButton);


                    dateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("onclick ", "--->");
                        }
                    });
                }
            }
        }

    }


    private LinearLayout.LayoutParams setMargin(boolean matchParent, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                matchParent ? LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, right, bottom);
        return params;
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
