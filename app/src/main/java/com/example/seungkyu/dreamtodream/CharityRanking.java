package com.example.seungkyu.dreamtodream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by junyepoh on 2016. 12. 12..
 */

public class CharityRanking extends FragmentActivity {
    private String TAG = CharityRanking.class.getSimpleName();
    TextView todaySleepTime, totalCharityDay, myRankTV;
    Button nextPage;
    int m_totalSleepTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FF48D1CC"));
        }
        setContentView(R.layout.activity_charity_ranking_page);

        new CountSleepDay().execute();

        new GetMyRank().execute();

        Intent intent = getIntent();
        todaySleepTime = (TextView)findViewById(R.id.today_sleep_time);
        todaySleepTime.setText(intent.getExtras().getString("curSleepTime"));
        m_totalSleepTime = intent.getExtras().getInt("m_totalSleepTime");

        totalCharityDay = (TextView)findViewById(R.id.total_charity_text);
        //totalCharityDay.setText("17");

        myRankTV = (TextView)findViewById(R.id.my_ranking_text);
        //myRank.setText("3");

        nextPage = (Button) findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent intent =  new Intent(getApplicationContext(),CompanyList.class);
                finish();
                startActivity(intent);
            }
        });

    }

    private class CountSleepDay extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        int count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CharityRanking.this);
            pDialog.setMessage("잠시만 기다려주세요...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("m_email", Info.USER_EMAIL));

            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/getSleepDay");
            HttpResponse response = null;
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                response = client.execute(httpPost);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);

                Log.e(TAG, "Response : "+result);
                JSONArray jsonArray = new JSONArray(result);
                count = jsonArray.length();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            totalCharityDay.setText(Integer.toString(count));
            try {
                Thread.sleep(1500);
                pDialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetMyRank extends AsyncTask<Void, Void, Void>{
        int myRank;

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("m_totalSleepTime", Integer.toString(m_totalSleepTime)));

            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/getMyRank");
            HttpResponse response = null;
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                response = client.execute(httpPost);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);

                Log.e(TAG, "Response : "+result);
                JSONArray jsonArray = new JSONArray(result);
                //count = jsonArray.length();
                Log.e(TAG, "count : "+jsonArray.length());
                myRank = jsonArray.length()+1;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            myRankTV.setText(Integer.toString(myRank));
        }
    }
}
