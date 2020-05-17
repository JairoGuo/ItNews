package com.example.itnews.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.itnews.NewsContentActivity;
import com.example.itnews.NewsData;
import com.example.itnews.R;
import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {



    ListView news_list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        news_list =  root.findViewById(R.id.news_list);
        ArrayList<String> news_titles  = null;
        try {
            news_titles = NewsData.getNewsTitles();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, news_titles);

        news_list.setAdapter(arrayAdapter);

        final ArrayList<String> finalNews_titles = news_titles;

        news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                HashMap<String,String> news_data = NewsData.getNewsData();
                intent.putExtra("newsUrl", news_data.get(finalNews_titles.get(i)));
                intent.putExtra("newsTitle", finalNews_titles.get(i));
                startActivity(intent);

            }
        });


        return root;
    }

}