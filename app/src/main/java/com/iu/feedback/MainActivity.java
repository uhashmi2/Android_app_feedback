package com.iu.feedback;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iu.feedback.database.MyDataSource;
import com.iu.feedback.model.FieldType;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static List<FieldType> typeList = new ArrayList<>();
    final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hasPermission(REQUEST_STORAGE_PERMISSION);
        typeList.add(new FieldType(FieldType.Type.CHECKBOX.toString(), FieldType.Type.CHECKBOX.toInteger()));
        typeList.add(new FieldType(FieldType.Type.DATE.toString(), FieldType.Type.DATE.toInteger()));
        typeList.add(new FieldType(FieldType.Type.RADIO.toString(), FieldType.Type.RADIO.toInteger()));
        typeList.add(new FieldType(FieldType.Type.RATING.toString(), FieldType.Type.RATING.toInteger()));
        typeList.add(new FieldType(FieldType.Type.SELECT.toString(), FieldType.Type.SELECT.toInteger()));
        typeList.add(new FieldType(FieldType.Type.TEXTFIELD.toString(), FieldType.Type.TEXTFIELD.toInteger()));
        typeList.add(new FieldType(FieldType.Type.TEXTAREA.toString(), FieldType.Type.TEXTAREA.toInteger()));
        MyDataSource dataSource = new MyDataSource(getApplicationContext());
        dataSource.InsertFields(typeList);

    }

    public void CreateForm(View view) {
        Intent i = new Intent(getApplicationContext(), AddForm.class);
        startActivity(i);
    }

    public void FormList(View view) {
        Intent i = new Intent(getApplicationContext(), FormList.class);
        startActivity(i);
    }

    //For permission
    public boolean hasPermission(int permissionType) {
        if (permissionType == REQUEST_STORAGE_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }
}


