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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsContentActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_content);

        final WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        dataBaseHelper = new DataBaseHelper(this, "NewsDB", null, 1);

        Intent intent = getIntent();
        String url = intent.getStringExtra("newsUrl");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
//                String[] s = new String[]{"head", "news-class", "grade", "down-app-box", "bdsharebuttonbox", "relevant-news", "comment", "news-footer", "adsbygoogle", "hot-app", "ruanmei-footer", "footer"};
                String[] ss = new String[]{"head", "news-class","news-footer", "hot-app", "ruanmei-footer", "footer", "grade", "down-app-box", "bdsharebuttonbox", "relevant-news", "lapin", "comment"};

                for (int i=0; i < ss.length; i++) {
                    webView.loadUrl("javascript:(function() { document.getElementsByClassName('"+ ss[i] +"')[0].style.display='none'; })()");
                }
                webView.loadUrl("javascript:(function() { document.getElementsByTagName('iframe')[document.getElementsByTagName('iframe').length -1].style.display='none'; })()");

            }


        });

        webView.loadUrl(url);
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

            case R.id.favo_btn:

                Intent intent = getIntent();
                String url = intent.getStringExtra("newsUrl");
                String title = intent.getStringExtra("newsTitle");
                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                    new AlertDialog.Builder(NewsContentActivity.this)
                            .setTitle("提示")
                            .setMessage("您还未登录，请登录")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return false;
                }
                String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                cursor.close();

                cursor = db.query("favorites", null, "news_title=? and user_name=?", new String[]{title, user_name}, null, null, null);
                cursor.moveToFirst();

                db = dataBaseHelper.getWritableDatabase();
                if (cursor.getCount() != 0) {
                    db.delete("favorites", "news_title=?", new String[]{title});
                    item.setIcon(R.drawable.bookmark);
                    Toast.makeText(NewsContentActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();

                } else {

                    ContentValues values = new ContentValues();
                    values.put("news_title", title);
                    values.put("news_url", url);
                    values.put("user_name", user_name);
                    db.insert("favorites", null, values);
                    item.setIcon(R.drawable.favorites);
                    Toast.makeText(NewsContentActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conent_title, menu);
        Intent intent = getIntent();
        String url = intent.getStringExtra("newsUrl");
        String title = intent.getStringExtra("newsTitle");

        db = dataBaseHelper.getReadableDatabase();
        cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
        cursor.moveToFirst();


        if (cursor.getCount() != 0) {
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            cursor = db.query("favorites", null, "news_title=? and user_name=?", new String[]{title, user_name}, null, null, null);
            if (cursor.getCount() != 0) {
                menu.findItem(R.id.favo_btn).setIcon(R.drawable.favorites);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

}
