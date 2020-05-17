package com.example.itnews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RePassActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;
    EditText oldPass ;
    EditText newPass;
    EditText newPass2;
    TextView userName;
    String old_pass;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_pass);




        dataBaseHelper = new DataBaseHelper(this, "NewsDB", null, 1);


        findViewById(R.id.repass_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPass = findViewById(R.id.edit_old_pass);
                newPass = findViewById(R.id.edit_new_pass);
                newPass2 = findViewById(R.id.edit_new_pass2);
                userName = findViewById(R.id.of_re_user_name);
                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
                cursor.moveToFirst();
                user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                old_pass = cursor.getString(cursor.getColumnIndex("user_pass"));
                userName.setText(user_name);

                if (oldPass.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(RePassActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入原密码")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                if (newPass.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(RePassActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入新密码")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                if (newPass2.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(RePassActivity.this)
                            .setTitle("提示")
                            .setMessage("请确认新密码")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }
                if (oldPass.getText().toString().equals(old_pass) == false) {
                    new AlertDialog.Builder(RePassActivity.this)
                            .setTitle("提示")
                            .setMessage("原密码不正确")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                if (newPass.getText().toString().equals(newPass2.getText().toString())) {
                    ContentValues values = new ContentValues();
                    values.put("user_pass", newPass.getText().toString());
                    values.put("user_state", "LOGOUT");
                    db = dataBaseHelper.getWritableDatabase();
                    db.update("users", values, "user_name=?", new String[]{user_name});

                    Toast.makeText(RePassActivity.this, "修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }
}
