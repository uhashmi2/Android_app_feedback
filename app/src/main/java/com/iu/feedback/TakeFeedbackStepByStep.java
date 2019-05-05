package com.iu.feedback;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import android.widget.ViewFlipper;

import com.iu.feedback.database.MyDataSource;
import com.iu.feedback.model.FieldType;
import com.iu.feedback.model.Fields;
import com.iu.feedback.model.Form;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TakeFeedbackStepByStep extends AppCompatActivity {
    ImageView next, back;
    ViewFlipper viewSwitcher;
    Form formObject;
    MyDataSource myDataSource;
    Calendar myCalendar;
    String checkBoxValue = "";
    DisplayMetrics displayMetrics;
    ImageView middleIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_step_by_step);
        myCalendar = Calendar.getInstance();
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        middleIcon = findViewById(R.id.middle_icon);
        viewSwitcher = findViewById(R.id.viewswitcher);
        final Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        final Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        viewSwitcher.setInAnimation(in);
        viewSwitcher.setOutAnimation(out);

        myDataSource = new MyDataSource(getApplicationContext());
        formObject = getIntent().getParcelableExtra("form");
        formObject = myDataSource.getFormDetail(formObject);

        for (int j = 0; j < formObject.getFieldsList().size(); j++) {
            final LinearLayout mainLayout = new LinearLayout(getApplicationContext());
            mainLayout.setOrientation(LinearLayout.VERTICAL);

            final Fields field = formObject.getFieldsList().get(j);
            TextView fieldName = new TextView(getApplicationContext());
            fieldName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
            fieldName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
            fieldName.setPadding(50, 0, 50, 0);
            fieldName.setGravity(Gravity.CENTER_HORIZONTAL);
            fieldName.setTag(field.getFieldId() + "," + field.getFormFieldId());
            fieldName.setTypeface(getFont());
            fieldName.setText(field.getFieldHeading());
            mainLayout.addView(fieldName);


            if (field.getFieldId() == FieldType.Type.SELECT.toInteger() || field.getFieldId() == FieldType.Type.RADIO.toInteger() ||
                    field.getFieldId() == FieldType.Type.CHECKBOX.toInteger()) {
                ArrayList<String> listSpinner = new ArrayList<>();
                for (int i = 0; i < field.getFieldOptions().size(); i++) {
                    String option = field.getFieldOptions().get(i);
                    if (field.getFieldId() == FieldType.Type.SELECT.toInteger()) {
                        listSpinner.add(option);
                        if (i == field.getFieldOptions().size() - 1) {
                            Spinner spinner = new Spinner(getApplicationContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 100, 0, 0);
                            params.gravity = Gravity.CENTER_HORIZONTAL;
                            spinner.setLayoutParams(params);
                            spinner.setPadding(80, 0, 80, 0);
                            ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(TakeFeedbackStepByStep.this, R.layout.spinner_item, listSpinner);
                            spinner.setAdapter(selectAdapter);
                            mainLayout.addView(spinner);
                        }

                    } else if (field.getFieldId() == FieldType.Type.RADIO.toInteger()) {
                        final RadioButton radiobutton = new RadioButton(getApplicationContext());
                        radiobutton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.LEFT;

                        params.setMargins((displayMetrics.widthPixels / 2) - 100, 0, 0, 0);
                        radiobutton.setLayoutParams(params);
                        radiobutton.setPadding(10, 20, 10, 20);
                        radiobutton.setText(option);
                        radiobutton.setTag(field.getFieldHeading());
                        radiobutton.setTypeface(getFont());
                        mainLayout.addView(radiobutton);
                        radiobutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                                    if (mainLayout.getChildAt(i) instanceof RadioButton) {
                                        RadioButton innerRadioButton = (RadioButton) mainLayout.getChildAt(i);
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
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.LEFT;
                        params.setMargins((displayMetrics.widthPixels / 2) - 100, 0, 0, 0);
                        checkbox.setLayoutParams(params);
                        checkbox.setPadding(10, 20, 10, 20);
                        checkbox.setText(option);
                        checkbox.setTypeface(getFont());
                        checkbox.setTag(field.getFieldHeading());
                        checkbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkBoxValue = "";
                                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                                    if (mainLayout.getChildAt(i) instanceof CheckBox) {
                                        CheckBox innerCheckBox = (CheckBox) mainLayout.getChildAt(i);
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
                        mainLayout.addView(checkbox);
                    }

                }
            } else {
                if (field.getFieldId() == FieldType.Type.TEXTAREA.toInteger()) {
                    int width = displayMetrics.widthPixels;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width - 250, 350);
                    params.setMargins(200, 80, 200, 0);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    EditText textArea = new EditText(getApplicationContext());
                    textArea.setBackgroundResource(R.drawable.edittext_background);
                    textArea.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textArea.setGravity(Gravity.TOP);
                    textArea.setHint("Please enter the text area detail.");
                    textArea.setTextColor(Color.WHITE);
                    textArea.setHintTextColor(Color.WHITE);
                    textArea.setTypeface(getFont());
                    textArea.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    textArea.setLayoutParams(params);
                    mainLayout.addView(textArea);

                } else if (field.getFieldId() == FieldType.Type.TEXTFIELD.toInteger()) {
                    int width = displayMetrics.widthPixels;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width - 250, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(200, 80, 200, 0);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    EditText textField = new EditText(getApplicationContext());
                    textField.setBackgroundResource(R.drawable.edittext_background);
                    textField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textField.setHint("Please enter the text field detail.");
                    textField.setTextColor(Color.WHITE);
                    textField.setHintTextColor(Color.WHITE);
                    textField.setSingleLine(true);
                    textField.setLayoutParams(params);
                    textField.setTypeface(getFont());
                    mainLayout.addView(textField);

                } else if (field.getFieldId() == FieldType.Type.RATING.toInteger()) {

                    final LinearLayout horizontally = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    params.setMargins(0, 100, 0, 10);
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
                    mainLayout.addView(horizontally);


                } else if (field.getFieldId() == FieldType.Type.DATE.toInteger()) {

                    final Button dateButton = new Button(getApplicationContext());
                    dateButton.setText("Select the date");
                    dateButton.setTextColor(Color.WHITE);
                    dateButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
                    dateButton.setPadding(0, 15, 0, 15);
                    dateButton.setLayoutParams(setMargin(true, 150, 100, 150, 0));
                    dateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatePickerDialog dialog = new DatePickerDialog(TakeFeedbackStepByStep.this);
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
                    dateButton.setTypeface(getFont());
                    mainLayout.addView(dateButton);
                }
            }


            viewSwitcher.addView(mainLayout);

        }

//Add suggestion layout
        viewSwitcher.addView(addSuggestionLayout());
        viewSwitcher.addView(addInfoLayout());


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int displayCount = viewSwitcher.getDisplayedChild();
                int childCount = viewSwitcher.getChildCount() - 1;
                if (displayCount < childCount) {
//                    LinearLayout parentView = (LinearLayout) viewSwitcher.getChildAt(displayCount);
//                    for (int j = 0; j < parentView.getChildCount(); j++) {
//                        View innerView = parentView.getChildAt(j);
//                        Log.i("innerView", innerView.toString());
//                    }
                    viewSwitcher.showNext();
                    back.setColorFilter(null);
                    back.setEnabled(true);
                } else {
                    next.setColorFilter(Color.parseColor("#BBFFFFFF"));
                    next.setEnabled(false);
                    Submit();
                    Intent intent = new Intent(TakeFeedbackStepByStep.this, Thankyou.class);
                    intent.putExtra("form", formObject);
                    startActivity(intent);
                    finish();
                }


                if (displayCount == childCount - 2)
                    middleIcon.setImageResource(R.drawable.ic_survey_comment);
                else if (displayCount == childCount - 1)
                    middleIcon.setImageResource(R.drawable.ic_survey_info);
                else
                    middleIcon.setImageResource(R.drawable.ic_survey_rate);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int displayCount = viewSwitcher.getDisplayedChild();
                int childCount = viewSwitcher.getChildCount() - 1;
                if (displayCount > 0) {
                    viewSwitcher.showPrevious();
                    next.setColorFilter(null);
                    next.setEnabled(true);
                } else {
                    back.setColorFilter(Color.parseColor("#BBFFFFFF"));
                    back.setEnabled(false);
                    finish();
                }

                if (displayCount == childCount)
                    middleIcon.setImageResource(R.drawable.ic_survey_comment);
                else
                    middleIcon.setImageResource(R.drawable.ic_survey_rate);

            }
        });
    }

    private LinearLayout.LayoutParams setMargin(boolean matchParent, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                matchParent ? LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, right, bottom);
        return params;
    }

    public void Submit() {
        String suggestion = "", name = "", contact = "", email = "", dob = "";
        //insert the feedback
        myDataSource.AddFeedback(formObject.getFormId(), false, suggestion, name, contact, email, dob);
        for (int i = 0; i < viewSwitcher.getChildCount(); i++) {
            LinearLayout parentView = (LinearLayout) viewSwitcher.getChildAt(i);
            if (parentView.getTag() != null) {
                if (parentView.getTag().toString().equalsIgnoreCase("suggestion") || parentView.getTag().toString().equalsIgnoreCase("info")) {

                    for (int j = 0; j < parentView.getChildCount(); j++) {
                        View innerView;
                        innerView = parentView.getChildAt(j);
                        if (innerView instanceof EditText) {
                            EditText editText = (EditText) innerView;
                            if (editText.toString().length() > 0) {
                                suggestion = editText.getText().toString();
                            }
                        } else if (innerView instanceof LinearLayout) {
                            LinearLayout linearLayout = (LinearLayout) innerView;
                            TextView title = (TextView) linearLayout.getChildAt(0);
                            EditText editText = (EditText) linearLayout.getChildAt(1);

                            if (title.getText().toString().equalsIgnoreCase(getResources().getString(R.string.name))) {
                                if (editText.toString().length() > 0)
                                    name = editText.getText().toString();
                            } else if (title.getText().toString().equalsIgnoreCase(getResources().getString(R.string.contact))) {
                                if (editText.toString().length() > 0)
                                    contact = editText.getText().toString();
                            } else if (title.getText().toString().equalsIgnoreCase(getResources().getString(R.string.email))) {
                                if (editText.toString().length() > 0)
                                    email = editText.getText().toString();
                            } else if (title.getText().toString().equalsIgnoreCase(getResources().getString(R.string.dob))) {
                                if (editText.toString().length() > 0)
                                    dob = editText.getText().toString();
                            }

                        }
                    }

                }
            } else {
                for (int j = 0; j < parentView.getChildCount(); j++) {
                    View innerView = parentView.getChildAt(j);

                    if (innerView instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) innerView;
                        Log.i("check box", "--->" + checkBox.getText().toString());
                    } else if (innerView instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) innerView;
                        if (radioButton.isChecked())
                            myDataSource.AddFeedbackResponseValue(radioButton.getText().toString());
                    } else if (innerView instanceof EditText) {
                        EditText editText = (EditText) innerView;
                        if (editText.toString().length() > 0)
                            myDataSource.AddFeedbackResponseValue(editText.getText().toString());
                    } else if (innerView instanceof Button) {
                        Button button = (Button) innerView;
                        myDataSource.AddFeedbackResponseValue(button.getText().toString());
                    } else if (innerView instanceof Spinner) {
                        Spinner spinner = (Spinner) innerView;
                        myDataSource.AddFeedbackResponseValue(spinner.getSelectedItem().toString());
                    } else if (innerView instanceof LinearLayout) {
                        LinearLayout linearLayout = (LinearLayout) innerView;
                        myDataSource.AddFeedbackResponseValue(linearLayout.getTag().toString());
                    } else if (innerView instanceof TextView) {
                        String tag = (String) innerView.getTag();
                        if (tag != null) {
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
                }

            }
        }


        myDataSource.AddFeedback(formObject.getFormId(), true, suggestion, name, contact, email, dob);

    }

    private LinearLayout addSuggestionLayout() {
        LinearLayout mainLayout = new LinearLayout(getApplicationContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setTag("suggestion");
        TextView title = new TextView(getApplicationContext());
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
        title.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        title.setPadding(30, 10, 30, 30);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setText("Suggestions please");
        title.setTypeface(getFont());
        mainLayout.addView(title);

        TextView description = new TextView(getApplicationContext());
        description.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        description.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        description.setPadding(30, 10, 30, 30);
        description.setGravity(Gravity.CENTER_HORIZONTAL);
        description.setText("Would you like to give any suggestions?");
        description.setTypeface(getFont());
        mainLayout.addView(description);

        int width = displayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width - 300, 350);
        params.setMargins(0, 20, 0, 0);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        EditText textArea = new EditText(getApplicationContext());
        textArea.setBackgroundResource(R.drawable.edittext_background);
        textArea.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textArea.setGravity(Gravity.TOP);
        textArea.setHint("Please enter the suggestions.");
        textArea.setTextColor(Color.WHITE);
        textArea.setHintTextColor(Color.WHITE);
        textArea.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
        textArea.setLayoutParams(params);
        textArea.setTypeface(getFont());
        mainLayout.addView(textArea);
        return mainLayout;
    }

    private LinearLayout addInfoLayout() {
        LinearLayout mainLayout = new LinearLayout(getApplicationContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setTag("info");
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 0);


        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout nameLayout = new LinearLayout(getApplicationContext());
        nameLayout.setOrientation(LinearLayout.HORIZONTAL);
        nameLayout.setLayoutParams(layoutParams);
        nameLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView name = new TextView(getApplicationContext());
        name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        name.setLayoutParams(textViewParams);
        name.setGravity(Gravity.CENTER);
        name.setText(getResources().getString(R.string.name));
        name.setTypeface(getFont());
        nameLayout.addView(name);


        EditText nameEdittext = new EditText(getApplicationContext());
        nameEdittext.setBackgroundResource(R.drawable.edittext_background);
        nameEdittext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        nameEdittext.setHint(getResources().getString(R.string.name));
        nameEdittext.setTextColor(Color.WHITE);
        nameEdittext.setHintTextColor(Color.WHITE);
        nameEdittext.setLayoutParams(editTextParams);
        nameEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        nameEdittext.setTypeface(getFont());
        nameLayout.addView(nameEdittext);

        mainLayout.addView(nameLayout);

        /////
        LinearLayout contactLayout = new LinearLayout(getApplicationContext());
        contactLayout.setOrientation(LinearLayout.HORIZONTAL);
        contactLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        contactLayout.setLayoutParams(layoutParams);
        TextView contact = new TextView(getApplicationContext());
        contact.setGravity(Gravity.CENTER);
        contact.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 21);
        contact.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        contact.setText(getResources().getString(R.string.contact));
        contact.setLayoutParams(textViewParams);
        contact.setTypeface(getFont());
        contactLayout.addView(contact);

        final EditText contactEdittext = new EditText(getApplicationContext());
        contactEdittext.setBackgroundResource(R.drawable.edittext_background);
        contactEdittext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 21);
        contactEdittext.setHint("401-###-####");
        contactEdittext.setTextColor(Color.WHITE);
        contactEdittext.setHintTextColor(Color.WHITE);
        contactEdittext.setLayoutParams(editTextParams);
        contactEdittext.setTypeface(getFont());
        contactEdittext.setInputType(InputType.TYPE_CLASS_PHONE);
        contactEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                contactEdittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                String originalString = s.toString();
                if (originalString.length() > 1) {
                    int secondChar = Integer.parseInt(Character.toString(originalString.charAt(1)));
                    if (secondChar > 3) {
                        contactEdittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                    }
//                    if (originalString.length() == 4)
//                        s.append("-");
                }
            }
        });
        contactLayout.addView(contactEdittext);

        mainLayout.addView(contactLayout);

        /////
        LinearLayout emailLayout = new LinearLayout(getApplicationContext());
        emailLayout.setOrientation(LinearLayout.HORIZONTAL);
        emailLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        emailLayout.setLayoutParams(layoutParams);
        TextView email = new TextView(getApplicationContext());
        email.setGravity(Gravity.CENTER);
        email.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        email.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        email.setLayoutParams(textViewParams);
        email.setText(getResources().getString(R.string.email));
        email.setTypeface(getFont());
        emailLayout.addView(email);

        EditText emailEdittext = new EditText(getApplicationContext());
        emailEdittext.setBackgroundResource(R.drawable.edittext_background);
        emailEdittext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        emailEdittext.setHint("johndoe@example.com");
        emailEdittext.setTextColor(Color.WHITE);
        emailEdittext.setHintTextColor(Color.WHITE);
        emailEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEdittext.setTypeface(getFont());
        emailEdittext.setLayoutParams(editTextParams);
        emailLayout.addView(emailEdittext);

        mainLayout.addView(emailLayout);

        ////
        LinearLayout dobLayout = new LinearLayout(getApplicationContext());
        dobLayout.setOrientation(LinearLayout.HORIZONTAL);
        dobLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        dobLayout.setLayoutParams(layoutParams);
        TextView dob = new TextView(getApplicationContext());
        dob.setGravity(Gravity.CENTER);
        dob.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        dob.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        dob.setLayoutParams(textViewParams);
        dob.setText(getResources().getString(R.string.dob));
        dob.setTypeface(getFont());
        dobLayout.addView(dob);

        final EditText dobEdittext = new EditText(getApplicationContext());
        dobEdittext.setBackgroundResource(R.drawable.edittext_background);
        dobEdittext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        dobEdittext.setHint("04 Dec, 1989");
        dobEdittext.setTextColor(Color.WHITE);
        dobEdittext.setHintTextColor(Color.WHITE);
        dobEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        dobEdittext.setLayoutParams(editTextParams);
        dobEdittext.setInputType(InputType.TYPE_NULL);
        dobEdittext.setTypeface(getFont());
        dobEdittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    DatePickerDialog dialog = new DatePickerDialog(TakeFeedbackStepByStep.this);
                    dialog.show();
                    dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, month);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date = new Date(myCalendar.getTimeInMillis());
                            DateFormat df = new SimpleDateFormat("dd MMM, yyyy");
                            dobEdittext.setText(df.format(date));
                        }
                    });
                }
                return false;
            }
        });
        dobLayout.addView(dobEdittext);
        mainLayout.addView(dobLayout);
        return mainLayout;
    }

    public Typeface getFont() {
        return Typeface.createFromAsset(this.getAssets(), "fonts/Quicksand-Medium.ttf");
    }
}
