package com.example.itnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoritesActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ListView listView;
    SimpleCursorAdapter simpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        dataBaseHelper = new DataBaseHelper(this, "NewsDB", null, 1);

        listView = findViewById(R.id.favo_listview);
        db = dataBaseHelper.getReadableDatabase();
        cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            new AlertDialog.Builder(FavoritesActivity.this)
                    .setTitle("提示")
                    .setMessage("您还未登录，请登录")
                    .setPositiveButton("确定",null)
                    .create().show();
            return;
        }

        final String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
        cursor.close();

        cursor = db.query("favorites", null, "user_name=?", new String[]{user_name}, null, null, null);
        cursor.moveToFirst();
        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.favo_listview_item, cursor, new String[]{"news_title"},
                new int[]{R.id.favo_list_news_title},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(null);
        listView.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor1;
                TextView textView = view.findViewById(R.id.favo_list_news_title);
                cursor1 = cursor = db.query("favorites", null,
                        "user_name=? and news_title=?",
                        new String[]{user_name, textView.getText().toString() },
                        null, null, null);
                cursor1.moveToFirst();

                final String news_title = cursor1.getString(cursor1.getColumnIndex("news_title"));
                String news_url = cursor1.getString(cursor1.getColumnIndex("news_url"));

                Intent intent = new Intent(FavoritesActivity.this, NewsContentActivity.class);
                intent.putExtra("newsUrl", news_url);
                intent.putExtra("newsTitle", news_title);
                startActivity(intent);

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



    @Override
    protected void onStart() {
        super.onStart();


        db = dataBaseHelper.getReadableDatabase();
        cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
        cursor.moveToFirst();
        final String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
        cursor.close();
        cursor = db.query("favorites", null, "user_name=?", new String[]{user_name}, null, null, null);
        cursor.moveToFirst();
        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.favo_listview_item, cursor, new String[]{"news_title"},
                new int[]{R.id.favo_list_news_title},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(null);
        listView.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.notifyDataSetChanged();


    }
}
