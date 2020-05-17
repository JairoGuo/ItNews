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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    EditText userName;
    EditText userPass;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userName = findViewById(R.id.login_user_name);
        userPass = findViewById(R.id.login_user_pass);
        dataBaseHelper = new DataBaseHelper(this, "NewsDB", null, 1);

        textView = findViewById(R.id.protocol_in);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, ProtocolActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.logup_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, LogUpActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userName.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(LogInActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入用户名")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                if (userPass.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(LogInActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入密码")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_name=?"
                        , new String[]{userName.getText().toString()}
                        , null, null, null);
                if (cursor.getCount() == 0) {
                    new AlertDialog.Builder(LogInActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入正确的用户名或注册用户")
                            .setPositiveButton("确定", null)
                            .create().show();
                    return;
                } else {
                    Cursor cursor1 = db.query("users", null,
                            "user_name=? and user_pass=?",
                            new String[]{userName.getText().toString(),
                                    userPass.getText().toString()},
                            null, null, null);

                    if (cursor1.getCount() > 0) {
                        Toast.makeText(LogInActivity.this, "登录成功",
                                Toast.LENGTH_SHORT).show();
                        SQLiteDatabase db1 = dataBaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("user_state","LOGIN");
                        db1.update("users", values, "user_name=?",
                                new String[]{userName.getText().toString()});
                        db1.close();
                        db.close();

                        Intent intent = new Intent();
                        intent.putExtra("login_state", "LOGIN");
                        intent.putExtra("user_name", userName.getText().toString());
                        setResult(1, intent);
                        finish();

                    }

                }




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
