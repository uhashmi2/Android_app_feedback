package com.iu.feedback;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.iu.feedback.database.MyDataSource;
import com.iu.feedback.model.FieldType;
import com.iu.feedback.model.Fields;
import com.iu.feedback.model.Form;

import java.util.ArrayList;
import java.util.Date;


public class AddForm extends AppCompatActivity {
    MyDataSource myDataSource;
    LinearLayout parentLayout;
    ArrayList<View> fieldList = new ArrayList<>();
    EditText formTitle, formDescription;
    Button addFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form);
        getSupportActionBar().setTitle("New Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        formTitle = findViewById(R.id.form_title);
        addFrom = findViewById(R.id.add_form);
        formDescription = findViewById(R.id.description);
        parentLayout = findViewById(R.id.parent_layout);
        myDataSource = new MyDataSource(getApplicationContext());


    }

    public void AddField(View view) {
        addFrom.setVisibility(View.VISIBLE);
        addNewField();
    }

    public void AddForm(View view) {
        Form form = new Form();
        form.setTitle(formTitle.getText().toString());
        form.setDescription(formDescription.getText().toString());
        form.setDate(new Date().getTime());
        ArrayList<Fields> fieldsArrayList = new ArrayList<>();

        for (int i = 0; i < fieldList.size(); i++) {
            EditText fieldName = fieldList.get(i).findViewById(R.id.field_name);
            AppCompatSpinner type = fieldList.get(i).findViewById(R.id.type);
            CheckBox fieldStatus = fieldList.get(i).findViewById(R.id.field_status);
            Button addOption = fieldList.get(i).findViewById(R.id.add_option);


            ArrayList<String> fieldOptions = new ArrayList<>();
            if (addOption.getTag().toString().length() > 0) {
                LinearLayout extraOptionLayout = fieldList.get(i).findViewById(R.id.extra_options);
                final int childCount = extraOptionLayout.getChildCount();

                if (type.getSelectedItem().toString().equals(FieldType.Type.SELECT.toString())) {
                    View v = extraOptionLayout.getChildAt(0);
                    Spinner selectValues = (Spinner) v;
                    for (int k = 0; k < selectValues.getAdapter().getCount(); k++) {
                        fieldOptions.add(selectValues.getAdapter().getItem(k).toString());
                        Log.i("selectValues", "-->" + selectValues.getAdapter().getItem(k).toString());
                    }

                } else {
                    for (int j = 0; j < childCount; j++) {
                        View v = extraOptionLayout.getChildAt(j);
                        if (type.getSelectedItem().toString().equals(FieldType.Type.CHECKBOX.toString())) {
                            CheckBox checkBox = (CheckBox) v;
                            fieldOptions.add(checkBox.getText().toString());
                            Log.i("checkBox", "-->" + checkBox.getText().toString());
                        } else if (type.getSelectedItem().toString().equals(FieldType.Type.RADIO.toString())) {
                            RadioButton radioButton = (RadioButton) v;
                            fieldOptions.add(radioButton.getText().toString());
                            Log.i("radioButton", "-->" + radioButton.getText().toString());
                        }
                    }
                }

            }
            ///
            int fieldId = 0;
            for (FieldType field : MainActivity.typeList) {
                if (field.getType().equalsIgnoreCase(type.getSelectedItem().toString())) {
                    fieldId = field.getId();
                    break;
                }
            }
            ///
            fieldsArrayList.add(new Fields(fieldName.getText().toString(), fieldId, fieldStatus.isChecked() ? 1 : 0, fieldOptions));//Add fields
            //Add field options
        }
        form.setFieldsList(fieldsArrayList);
        myDataSource.InsertFromData(form);
        Toast.makeText(getApplicationContext(), "Form Added successfully", Toast.LENGTH_LONG).show();
        this.finish();

    }

    private void addNewField() {
        final View fieldView = getLayoutInflater().inflate(R.layout.add_field, parentLayout, false);
        ////
        ImageView close = fieldView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLayout.removeView(fieldView);
                fieldList.remove(fieldView);
            }
        });
        ///
        final LinearLayout extraOptions = fieldView.findViewById(R.id.extra_options);
        fieldView.setTag(fieldList.size());
        parentLayout.addView(fieldView);
        final AppCompatSpinner type = fieldView.findViewById(R.id.type);

        /////Spinner item
        ArrayAdapter<FieldType> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MainActivity.typeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(dataAdapter);

        /////
        final ArrayList<String> listSpinner = new ArrayList<>();
        final Button addOption = fieldView.findViewById(R.id.add_option);
        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AddForm.this, R.style.AlertDialogInput);
                final EditText edittext = new EditText(AddForm.this);
//               if(addOption.getText().toString().equalsIgnoreCase("Add checkbox"))
//                   alert.setTitle("Checkbox option");
                edittext.setHint("Enter the name");
                edittext.setTextColor(Color.WHITE);
                edittext.setSingleLine();
                edittext.setHintTextColor(Color.GRAY);
                FrameLayout container = new FrameLayout(getApplicationContext());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 15;
                params.rightMargin = 15;
                edittext.setLayoutParams(params);
                container.addView(edittext);
                alert.setView(container);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String input = edittext.getText().toString();
                        if (input.length() > 0) {
                            String spinnerValue = type.getSelectedItem().toString();
                            if (spinnerValue.equalsIgnoreCase(FieldType.Type.CHECKBOX.toString())) {
                                CheckBox cb = new CheckBox(getApplicationContext());
                                cb.setPadding(10, 10, 10, 10);
                                cb.setText(input);
                                extraOptions.addView(cb);
                            } else if (spinnerValue.equalsIgnoreCase(FieldType.Type.RADIO.toString())) {
                                RadioButton radioButton = new RadioButton(getApplicationContext());
                                radioButton.setPadding(10, 10, 10, 10);
                                radioButton.setText(input);
                                extraOptions.addView(radioButton);
                            } else if (spinnerValue.equalsIgnoreCase(FieldType.Type.SELECT.toString())) {
                                Spinner spinner = new Spinner(getApplicationContext());
                                spinner.setPadding(10, 0, 10, 0);
                                listSpinner.add(input);
                                ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(AddForm.this, android.R.layout.simple_spinner_dropdown_item, listSpinner);
                                spinner.setAdapter(selectAdapter);
                                if (listSpinner.size() == 1)
                                    extraOptions.addView(spinner);

                            }
                        }


                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();


            }
        });
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String typeValue = type.getSelectedItem().toString();
                if (typeValue.equalsIgnoreCase(FieldType.Type.CHECKBOX.toString()) || typeValue.equalsIgnoreCase(FieldType.Type.RADIO.toString()) ||
                        typeValue.equalsIgnoreCase(FieldType.Type.SELECT.toString())) {
                    addOption.setVisibility(View.VISIBLE);
                    addOption.setText("Add " + typeValue);
                    addOption.setTag(typeValue);
                } else {
                    addOption.setVisibility(View.GONE);
                    addOption.setTag("");
                }
                extraOptions.removeAllViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fieldList.add(fieldView);
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
