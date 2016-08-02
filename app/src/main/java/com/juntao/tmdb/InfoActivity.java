package com.juntao.tmdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;


public class InfoActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final static String key = "&api_key=16f1d4452bf58e6649c066f3a9bc7b63";
    private JSONObject jsonObject;
    private Map<String, Object> mDatas;
    private ImageView imageView;
    private TextView titleInfo;
    private TextView yearInfo;
    private TextView overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        imageView = (ImageView) findViewById(R.id.poster);
        titleInfo = (TextView) findViewById(R.id.titleInfo);
        yearInfo = (TextView) findViewById(R.id.yearInfo);
        overview = (TextView) findViewById(R.id.overview);

        try {
            JSONTokener jsonTokener = new JSONTokener(result);
            jsonObject = (JSONObject) jsonTokener.nextValue();

            initDatas(jsonObject);
        } catch (JSONException jex) {
            jex.printStackTrace();
        }


        titleInfo.setText((CharSequence) mDatas.get("title").toString());
        yearInfo.setText((CharSequence) mDatas.get("year").toString());
        overview.setText((CharSequence) mDatas.get("overview").toString());
        Picasso.with(this).load((String) mDatas.get("image")).into(imageView);


    }

    private void initDatas(JSONObject jsonObject) {
        if (MainActivity.getMode() == true) {
            initDatasForMovie(jsonObject);
        } else {
            initDatasForTV(jsonObject);
        }
        return;
    }

    private void initDatasForMovie(JSONObject jsonObject) {
        mDatas = new HashMap<String, Object>();
        try {
            mDatas.put("overview", jsonObject.get("overview"));
            mDatas.put("title", jsonObject.get("title"));
            mDatas.put("year", jsonObject.get("release_date"));
            StringBuilder baseUrlBuilder = new StringBuilder(IMAGE_BASE_URL);
            baseUrlBuilder.append(jsonObject.get("poster_path").toString());
            baseUrlBuilder.append(key);
            String imageUrl = baseUrlBuilder.toString();

            mDatas.put("image", imageUrl);
        } catch (JSONException jex) {
            jex.printStackTrace();
        }
    }

    private void initDatasForTV(JSONObject jsonObject) {
        mDatas = new HashMap<String, Object>();
        try {
            mDatas.put("overview", jsonObject.get("overview"));
            mDatas.put("title", jsonObject.get("name"));
            mDatas.put("year", jsonObject.get("first_air_date"));
            StringBuilder baseUrlBuilder = new StringBuilder(IMAGE_BASE_URL);
            baseUrlBuilder.append(jsonObject.get("poster_path").toString());
            baseUrlBuilder.append(key);
            String imageUrl = baseUrlBuilder.toString();

            mDatas.put("image", imageUrl);
        } catch (JSONException jex) {
            jex.printStackTrace();
        }
    }
}
