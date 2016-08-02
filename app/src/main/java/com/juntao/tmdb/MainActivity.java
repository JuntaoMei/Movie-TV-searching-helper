package com.juntao.tmdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import utils.ApiUrl;
import utils.RunThread;

public class MainActivity extends AppCompatActivity {

    final static String movieMode = "search/movie?query=";
    final static String tvMode = "search/tv?query=";
    private static boolean mode;
    private EditText textForTitle;
    private EditText textForYear;
    private Button buttonForMovie;
    private Button buttonForTV;

    public static boolean getMode() {
        return mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textForTitle = (EditText) findViewById(R.id.textForTitle);
        textForYear = (EditText) findViewById(R.id.textForYear);
        buttonForMovie = (Button) findViewById(R.id.buttonForMovie);
        buttonForTV = (Button) findViewById(R.id.buttonForTV);
        buttonForMovie.setOnClickListener(new SearchMovieListener());
        buttonForTV.setOnClickListener(new SearchTVListener());


    }

    class SearchMovieListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            mode = true;
            String result = null;
            String title = textForTitle.getText().toString();
            String year = textForYear.getText().toString();
            title = StringUtils.trimToEmpty(title);

            if (title == null || title.length() <= 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "please enter a title", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }


            ApiUrl apiUrl = new ApiUrl(movieMode);
            apiUrl.addParam("title", title);
            if (year != null && year.length() != 0) {
                apiUrl.addParam("primary_release_year", year);
            }


            String url = apiUrl.buildUrl().toString();
            RunThread rt = new RunThread(url);
            rt.start();
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

                intent.setClass(MainActivity.this, MyListActivity.class);
                MainActivity.this.startActivity(intent);
                return;
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "no result", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
        }
    }

    class SearchTVListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            mode = false;
            String result = null;
            String title = textForTitle.getText().toString();
            String year = textForYear.getText().toString();
            if (title == null || title.length() <= 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "please enter a title", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

            ApiUrl apiUrl = new ApiUrl(tvMode);
            apiUrl.addParam("title", title);
            if (year != null && year.length() != 0) {
                apiUrl.addParam("year", year);
            }

            String url = apiUrl.buildUrl().toString();
            Log.d("URL------", url);
            RunThread rt = new RunThread(url);
            rt.start();
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

                intent.setClass(MainActivity.this, MyListActivity.class);
                MainActivity.this.startActivity(intent);
                return;
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "no result", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
        }
    }
}
