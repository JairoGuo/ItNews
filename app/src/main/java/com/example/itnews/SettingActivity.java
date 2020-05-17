package com.example.itnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class SettingActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;

    TextView userName;
    TextView userId;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userName = findViewById(R.id.setting_user_name);
        userId = findViewById(R.id.setting_user_id);
        dataBaseHelper = new DataBaseHelper(this, "NewsDB", null, 1);
        db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "未登录...", Toast.LENGTH_SHORT).show();
        } else {
            user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            userName.setText(user_name);
            userId.setText("ID:NULL");
        }


        findViewById(R.id.repass_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
                if (cursor.getCount() == 0) {
                    new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("提示")
                            .setMessage("您还未登录，请登录")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                Intent intent = new Intent(SettingActivity.this, RePassActivity.class);
                startActivity(intent);
                Cursor cursor1 = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
                if (cursor1.getCount() == 0) {
                    finish();
                }

            }
        });

        findViewById(R.id.switch_account_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);

                if (cursor.getCount() == 0) {
                    new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("提示")
                            .setMessage("您还未登录，请登录")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                Intent intent = new Intent(SettingActivity.this, LogInActivity.class);
                startActivity(intent);
                ContentValues values = new ContentValues();
                values.put("user_state", "LOGOUT");
                db = dataBaseHelper.getWritableDatabase();
                db.update("users", values, "user_name=?", new String[]{user_name});
                finish();

            }
        });


        findViewById(R.id.delect_user_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);

                if (cursor.getCount() == 0) {
                    new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("提示")
                            .setMessage("您还未登录，请登录")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("警告")
                        .setMessage("请确认是否要注销当前账户")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final EditText et = new EditText(SettingActivity.this);
                                new AlertDialog.Builder(SettingActivity.this)
                                        .setTitle("提示")
                                        .setView(et)
                                        .setMessage("请输入您的账户密码来操作")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
                                                cursor.moveToFirst();
                                                String user_pass = cursor.getString(cursor.getColumnIndex("user_pass"));
                                                if (et.getText().toString().equals(user_pass)) {

                                                    db = dataBaseHelper.getWritableDatabase();
                                                    db.delete("users", "user_name=?", new String[]{user_name});
                                                    Toast.makeText(SettingActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                                                    finish();

                                                }

                                            }
                                        })
                                        .create().show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });

        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);

                if (cursor.getCount() == 0) {
                    new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("提示")
                            .setMessage("您还未登录，请登录")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                ContentValues values = new ContentValues();

                values.put("user_state", "LOGOUT");
                db = dataBaseHelper.getWritableDatabase();
                db.update("users", values, "user_name=?", new String[]{user_name});

                Toast.makeText(SettingActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
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
