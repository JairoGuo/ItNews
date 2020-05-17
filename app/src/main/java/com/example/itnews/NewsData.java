package com.example.itnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NewsData {
    private static ArrayList<String> news_title_s = new ArrayList<>();
    private static HashMap<String, String> news_data = new HashMap<>();
    private static HashMap<String, String> news_data1 = new HashMap<>();

    private static void init() throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                news_title_s.clear();
                news_data.clear();
                String url = "https://it.ithome.com";
                String css_query_l = "#con > div.wrap_left > div.wrap_left_top > div.mf > div.rt > div > div > div.block.new-list-1 > ul > li:nth-child( ";
                String css_query_r = ") > span.title > a";
                String news_title = null;
                String news_content_url = null;
                try {
                    Document document = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36")
                            .get();
                    for (int i = 0; i < 25; i++) {

                        news_title = document.select(css_query_l + i + css_query_r).text();
                        if (news_title.trim() == "") {
                            continue;
                        }
                        news_content_url = document.select(css_query_l + i + css_query_r).attr("href");
                        news_title_s.add(news_title);
                        news_data.put(news_title, news_content_url);
                        String[] url1 = news_content_url.split("/");
                        news_data1.put(news_title, "https://m.ithome.com/html/" + url1[4] + url1[5]);


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread.join();

    }

    public static ArrayList<String> getNewsTitles() throws InterruptedException {
        init();
        return  news_title_s;
    }

    public static HashMap<String, String> getNewsData() {

        return news_data1;
    }
}
