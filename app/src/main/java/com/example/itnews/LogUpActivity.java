package com.example.itnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogUpActivity extends AppCompatActivity {

    EditText userName;
    EditText userPass;
    CheckBox checkBox;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;
    TextView textView;

    int ID = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_up);

        userName = findViewById(R.id.logup_user_name);
        userPass = findViewById(R.id.logup_user_pass);
        checkBox = findViewById(R.id.checkBox);
        textView = findViewById(R.id.protocol_up);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogUpActivity.this, ProtocolActivity.class);
                startActivity(intent);
            }
        });

        dataBaseHelper = new DataBaseHelper(this, "NewsDB", null, 1);

        findViewById(R.id.logup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(LogUpActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入用户名")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                if (userPass.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(LogUpActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入密码")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                if (checkBox.isChecked() == false) {
                    new AlertDialog.Builder(LogUpActivity.this)
                            .setTitle("提示")
                            .setMessage("请同意使用协议")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_name=?",
                        new String[]{userName.getText().toString()},
                        null, null, null);
                cursor.moveToFirst();

                if (cursor.getCount() != 0) {
                    new AlertDialog.Builder(LogUpActivity.this)
                            .setTitle("提示")
                            .setMessage("已存在该用户")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("user_name", userName.getText().toString());
                values.put("user_pass", userPass.getText().toString());

                db = dataBaseHelper.getWritableDatabase();
                db.insert("users", null, values);

                Toast.makeText(LogUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
