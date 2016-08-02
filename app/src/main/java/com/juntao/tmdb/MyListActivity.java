package com.juntao.tmdb;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ApiUrl;
import utils.LazyAdapter;
import utils.RunThreadForInfo;

public class MyListActivity extends ListActivity {
    final static String movieMode = "movie/";
    final static String tvMode = "tv/";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final static String key = "?api_key=16f1d4452bf58e6649c066f3a9bc7b63";
    private ListView myListView;
    private List<HashMap<String, Object>> mDatas;
    private JSONObject jsonObject;
    private LazyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myListView = getListView();

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        try {
            JSONTokener jsonTokener = new JSONTokener(result);
            jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");
            if (MainActivity.getMode() == true) {
                initDatasForMovie(jsonArray);
            } else {
                initDatasForTV(jsonArray);
            }
        } catch (JSONException jex) {
            jex.printStackTrace();
        }


        mAdapter = new LazyAdapter(this, mDatas);
        myListView.setAdapter(mAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieId = Integer.toString((int) mDatas.get(position).get("id"));
                ApiUrl apiUrl = MainActivity.getMode() == true ? new ApiUrl(movieMode + movieId) : new ApiUrl(tvMode + movieId);
                String url = apiUrl.getBaseUrl() + key;
                RunThreadForInfo rt = new RunThreadForInfo(url);
                rt.start();
                String result = rt.getRunLog();
                while ((result == null || result.length() == 0) && rt.isAlive()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result = rt.getRunLog();
                }
                if (result.length() > 0)//success
                {
                    Intent intent = new Intent();
                    intent.putExtra("result", result);

                    intent.setClass(MyListActivity.this, InfoActivity.class);
                    MyListActivity.this.startActivity(intent);
                    return;
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "no result", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
            }
        });


    }


    private void initDatasForMovie(JSONArray jsonArray) {
        mDatas = new ArrayList<HashMap<String, Object>>();
        int i = 0;
        try {
            while (i < jsonArray.length()) {
                HashMap<String, Object> map = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("vote_average", jsonObject.get("vote_average"));
                map.put("id", jsonObject.get("id"));
                map.put("title", jsonObject.get("title"));
                map.put("year", jsonObject.get("release_date"));
                StringBuilder baseUrlBuilder = new StringBuilder(IMAGE_BASE_URL);
                baseUrlBuilder.append(jsonObject.get("poster_path").toString());
                baseUrlBuilder.append(key);
                String imageUrl = baseUrlBuilder.toString();


                map.put("image", imageUrl);

                mDatas.add(map);
                i++;
            }
        } catch (JSONException jex) {
            jex.printStackTrace();
        }
    }

    private void initDatasForTV(JSONArray jsonArray) {
        mDatas = new ArrayList<HashMap<String, Object>>();
        int i = 0;
        try {
            while (i < jsonArray.length()) {
                HashMap<String, Object> map = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("vote_average", jsonObject.get("vote_average"));
                map.put("id", jsonObject.get("id"));
                map.put("title", jsonObject.get("name"));
                map.put("year", jsonObject.get("first_air_date"));
                StringBuilder baseUrlBuilder = new StringBuilder(IMAGE_BASE_URL);
                baseUrlBuilder.append(jsonObject.get("poster_path").toString());
                baseUrlBuilder.append(key);
                String imageUrl = baseUrlBuilder.toString();


                map.put("image", imageUrl);

                mDatas.add(map);
                i++;
            }
        } catch (JSONException jex) {
            jex.printStackTrace();
        }
    }


}
