package com.example.itnews.ui.my;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.itnews.AboutActivity;
import com.example.itnews.DataBaseHelper;
import com.example.itnews.FavoritesActivity;
import com.example.itnews.LogInActivity;
import com.example.itnews.R;
import com.example.itnews.SettingActivity;


public class MyFragment extends Fragment {


    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;
    Button button;
    TextView textView;
    String login_state = "null";
    String user_name;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my, container, false);
        dataBaseHelper = new DataBaseHelper(getActivity(), "NewsDB", null, 1);
        db = dataBaseHelper.getReadableDatabase();

        textView = root.findViewById(R.id.textView);
        button = root.findViewById(R.id.my_btn);

        Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            button.setVisibility(View.INVISIBLE);
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            textView.setText(user_name);
        }


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView = getActivity().findViewById(R.id.textView);
        button = getActivity().findViewById(R.id.my_btn);
        button.setVisibility(View.VISIBLE);
        textView.setText(null);

        getActivity().findViewById(R.id.my_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        getActivity().findViewById(R.id.my_setting_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                startActivity(intent);
            }
        });

        getActivity().findViewById(R.id.my_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);

            }
        });

        getActivity().findViewById(R.id.my_about_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        getActivity().findViewById(R.id.my_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor1 = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
                if (cursor1.getCount() == 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("您还未登录，请登录")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);

            }
        });
        getActivity().findViewById(R.id.my_favorite_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = dataBaseHelper.getReadableDatabase();
                Cursor cursor1 = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);

                if (cursor1.getCount() == 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("您还未登录，请登录")
                            .setPositiveButton("确定",null)
                            .create().show();
                    return;
                }

                Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);
            }
        });


        db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            button.setVisibility(View.INVISIBLE);
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            textView.setText(user_name);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        textView = getActivity().findViewById(R.id.textView);
        button = getActivity().findViewById(R.id.my_btn);
        button.setVisibility(View.VISIBLE);
        textView.setText(null);
        db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "user_state=?", new String[]{"LOGIN"}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            button.setVisibility(View.INVISIBLE);
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            textView.setText(user_name);
        }

    }

}