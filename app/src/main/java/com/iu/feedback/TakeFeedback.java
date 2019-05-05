package com.iu.feedback;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TakeFeedback extends AppCompatActivity {
    TextView title, description;
    LinearLayout parentLayout;
    MyDataSource myDataSource;
    Form formObject;
    Calendar myCalendar;
    String checkBoxValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
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
            int count = j + 1;
            fieldName.setText(count + ") " + field.getFieldHeading());

            parentLayout.addView(fieldName);
            if (field.getFieldId() == FieldType.Type.SELECT.toInteger() || field.getFieldId() == FieldType.Type.RADIO.toInteger() ||
                    field.getFieldId() == FieldType.Type.CHECKBOX.toInteger()) {
                ArrayList<String> listSpinner = new ArrayList<>();
                for (int i = 0; i < field.getFieldOptions().size(); i++) {
                    String option = field.getFieldOptions().get(i);
                    if (field.getFieldId() == FieldType.Type.SELECT.toInteger()) {
                        listSpinner.add(option);
                        if (i == field.getFieldOptions().size() - 1) {
                            Spinner spinner = new Spinner(getApplicationContext());
                            spinner.setPadding(50, 10, 20, 0);
                            ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(TakeFeedback.this, android.R.layout.simple_spinner_dropdown_item, listSpinner);
                            spinner.setAdapter(selectAdapter);
                            parentLayout.addView(spinner);
                        }

                    } else if (field.getFieldId() == FieldType.Type.RADIO.toInteger()) {
                        final RadioButton radiobutton = new RadioButton(getApplicationContext());
                        radiobutton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        radiobutton.setText(option);
                        radiobutton.setTag(field.getFieldHeading());
                        radiobutton.setLayoutParams(setMargin(false, 50, 10, 20, 10));
                        parentLayout.addView(radiobutton);
                        radiobutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                                    if (parentLayout.getChildAt(i) instanceof RadioButton) {
                                        RadioButton innerRadioButton = (RadioButton) parentLayout.getChildAt(i);
                                        if (field.getFieldHeading().equalsIgnoreCase(innerRadioButton.getTag().toString())) {
                                            if (radiobutton.getText().toString().equalsIgnoreCase(innerRadioButton.getText().toString())) {
                                            } else {
                                                innerRadioButton.setChecked(false);
                                            }
                                        }

                                    }
                                }
                            }
                        });
                    } else if (field.getFieldId() == FieldType.Type.CHECKBOX.toInteger()) {
                        final CheckBox checkbox = new CheckBox(getApplicationContext());
                        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        checkbox.setText(option);
                        checkbox.setTag(field.getFieldHeading());
                        checkbox.setLayoutParams(setMargin(false, 50, 10, 20, 10));
                        checkbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkBoxValue = "";
                                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                                    if (parentLayout.getChildAt(i) instanceof CheckBox) {
                                        CheckBox innerCheckBox = (CheckBox) parentLayout.getChildAt(i);
                                        if (field.getFieldHeading().equalsIgnoreCase(innerCheckBox.getTag().toString())) {
                                            if (innerCheckBox.isChecked()) {
                                                if (checkBoxValue.length() > 0) {
                                                    checkBoxValue = checkBoxValue + "," + innerCheckBox.getText().toString();
                                                } else {
                                                    checkBoxValue = innerCheckBox.getText().toString();
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        });
                        parentLayout.addView(checkbox);
                    }

                }
            } else {
                if (field.getFieldId() == FieldType.Type.TEXTAREA.toInteger()) {
                    EditText textArea = new EditText(getApplicationContext());
                    textArea.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textArea.setHint("Please enter the text area detail.");
                    textArea.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    textArea.setLayoutParams(setMargin(true, 50, 10, 40, 10));
                    parentLayout.addView(textArea);

                } else if (field.getFieldId() == FieldType.Type.TEXTFIELD.toInteger()) {

                    EditText textField = new EditText(getApplicationContext());
                    textField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textField.setHint("Please enter the text field detail.");
                    textField.setSingleLine(true);
                    textField.setLayoutParams(setMargin(true, 50, 10, 40, 10));
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
                            horizontally.setTag("neutral");
                            icon.setSelected(true);
                        } else if (i == 3) {
                            icon.setImageResource(R.drawable.selector_rate4);
                            icon.setTag("good");
                        } else {
                            icon.setImageResource(R.drawable.selector_rate5);
                            icon.setTag("awesome");
                        }
                        icon.setLayoutParams(imageParams);
                        horizontally.addView(icon);
                        icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ///For change the icon selection
                                for (int i = 0; i < horizontally.getChildCount(); i++) {
                                    ImageView imageView = (ImageView) horizontally.getChildAt(i);
                                    if (imageView.getTag().toString().equalsIgnoreCase(icon.getTag().toString())) {
                                        icon.setSelected(true);
                                    } else {
                                        imageView.setSelected(false);
                                    }
                                }
                                //////
                                horizontally.setTag(icon.getTag().toString());
                            }
                        });
                    }
                    parentLayout.addView(horizontally);


                } else if (field.getFieldId() == FieldType.Type.DATE.toInteger()) {

                    final Button dateButton = new Button(getApplicationContext());
                    dateButton.setText("Set the date");
                    dateButton.setPadding(50, 10, 10, 10);
                    dateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatePickerDialog dialog = new DatePickerDialog(TakeFeedback.this);
                            dialog.show();
                            dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    myCalendar.set(Calendar.YEAR, year);
                                    myCalendar.set(Calendar.MONTH, month);
                                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    Date date = new Date(myCalendar.getTimeInMillis());
                                    DateFormat df = new SimpleDateFormat("E, MMM d");
                                    dateButton.setText(df.format(date));
                                }
                            });
                        }
                    });
                    parentLayout.addView(dateButton);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                myDataSource.AddFeedback(formObject.getFormId(), false, "", "", "", "", "");
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View view = parentLayout.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) parentLayout.getChildAt(i);
                        Log.i("check box", "--->" + checkBox.getText().toString());
                    } else if (view instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) parentLayout.getChildAt(i);
                        if (radioButton.isChecked())
                            myDataSource.AddFeedbackResponseValue(radioButton.getText().toString());
                    } else if (view instanceof EditText) {
                        EditText editText = (EditText) parentLayout.getChildAt(i);
                        if (editText.toString().length() > 0)
                            myDataSource.AddFeedbackResponseValue(editText.getText().toString());
                    } else if (view instanceof Button) {
                        Button button = (Button) parentLayout.getChildAt(i);
                        myDataSource.AddFeedbackResponseValue(button.getText().toString());
                    } else if (view instanceof Spinner) {
                        Spinner spinner = (Spinner) parentLayout.getChildAt(i);
                        myDataSource.AddFeedbackResponseValue(spinner.getSelectedItem().toString());
                    } else if (view instanceof LinearLayout) {
                        LinearLayout linearLayout = (LinearLayout) parentLayout.getChildAt(i);
                        myDataSource.AddFeedbackResponseValue(linearLayout.getTag().toString());
                    } else if (view instanceof TextView) {

                        String tag = (String) parentLayout.getChildAt(i).getTag();

                        String[] split = tag.split(",");
                        int fieldId = Integer.parseInt(split[0]);
                        int formFieldId = Integer.parseInt(split[1]);

                        if (fieldId == FieldType.Type.CHECKBOX.toInteger()) {
                            myDataSource.AddFeedbackResponse(FieldType.Type.CHECKBOX.toInteger(), formFieldId);
                            myDataSource.AddFeedbackResponseValue(checkBoxValue);
                        } else if (fieldId == FieldType.Type.DATE.toInteger()) {
                            myDataSource.AddFeedbackResponse(FieldType.Type.DATE.toInteger(), formFieldId);
                        } else if (fieldId == FieldType.Type.RADIO.toInteger()) {
                            myDataSource.AddFeedbackResponse(FieldType.Type.RADIO.toInteger(), formFieldId);
                        } else if (fieldId == FieldType.Type.RATING.toInteger()) {
                            myDataSource.AddFeedbackResponse(FieldType.Type.RATING.toInteger(), formFieldId);
                        } else if (fieldId == FieldType.Type.SELECT.toInteger()) {
                            myDataSource.AddFeedbackResponse(FieldType.Type.SELECT.toInteger(), formFieldId);
                        } else if (fieldId == FieldType.Type.TEXTFIELD.toInteger()) {
                            myDataSource.AddFeedbackResponse(FieldType.Type.TEXTFIELD.toInteger(), formFieldId);
                        } else if (fieldId == FieldType.Type.TEXTAREA.toInteger()) {
                            myDataSource.AddFeedbackResponse(FieldType.Type.TEXTAREA.toInteger(), formFieldId);
                        }
                    }

                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
