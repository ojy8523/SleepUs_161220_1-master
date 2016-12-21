package com.example.seungkyu.dreamtodream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.betomaluje.miband.ActionCallback;
import com.betomaluje.miband.DateUtils;
import com.betomaluje.miband.MiBand;
import com.betomaluje.miband.model.VibrationMode;
import com.betomaluje.miband.models.ActivityAmount;
import com.betomaluje.miband.models.ActivityData;
import com.betomaluje.miband.models.ActivityKind;
import com.betomaluje.miband.sqlite.ActivitySQLite;

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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class StartCharity extends FragmentActivity {
    MiBand miBand;
    private String TAG = StartCharity.class.getSimpleName();
    //Button backBtn;
    Button startCharity;
    TextView curSleepTimeTV, totalEarnedTimeTV, totalEarnedMoneyTV;
    private int tmpSleepTime;
    private int deepSleepTime;
    int c_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FF48D1CC"));
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_charity_page);

        miBand = MiBand.getInstance(StartCharity.this);

        startCharity = (Button) findViewById(R.id.startCharity);
       // backBtn = (Button)findViewById(R.id.backBtn);
        curSleepTimeTV = (TextView)findViewById(R.id.curSleepTimeTV);
        totalEarnedTimeTV = (TextView)findViewById(R.id.totalEarnedTimeTV);
        totalEarnedMoneyTV = (TextView)findViewById(R.id.totalEarnedMoneyTV);

        new ConnectBand().execute();

        new LoadTotalEarnedMoney().execute();

        Drawable alpha = ((ImageView)findViewById(R.id.give_page)).getDrawable();
        alpha.setAlpha(50);


        //*************************************



        //*************************************
        Intent intent = getIntent();
        c_id = intent.getExtras().getInt("c_id");
        deepSleepTime = intent.getExtras().getInt("deepSleepTime");
        startCharity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                /*Intent intent =  new Intent(getApplicationContext(),CharityRanking.class);
                finish();
                startActivity(intent);*/
                //miBand.startVibration(VibrationMode.VIBRATION_WITH_LED);
                /*miBand.customVibration(3, 800, 500);
                miBand.setLedColor(3, 120, 500);*/
                new Donate().execute();
                try {
                    Thread.sleep(2500);
                    Intent intent =  new Intent(getApplicationContext(),CharityRanking.class);
                    intent.putExtra("curSleepTime", curSleepTimeTV.getText().toString());
                    intent.putExtra("m_totalSleepTime",tmpSleepTime+deepSleepTime);
                    finish();
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    /*    backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

    } // onCreate

    private class ConnectBand extends AsyncTask<Void, Void, Void>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StartCharity.this);
            pDialog.setMessage("밴드 연결중입니다...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /*if (!miBand.isConnected()) {
                miBand.connect(new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.d(TAG, "Connected with Mi Band!");
                        //show SnackBar/Toast or something
                        Calendar before = Calendar.getInstance();
                        before.add(Calendar.DAY_OF_WEEK, -14);

                        Calendar today = Calendar.getInstance();
                        today.setTimeInMillis(System.currentTimeMillis());

                        Log.i(TAG, "data from " + DateUtils.convertString(before) + " to " + DateUtils.convertString(today));

                        ActivityAmount deepSleep = new ActivityAmount(ActivityKind.TYPE_DEEP_SLEEP);
                        Log.i(TAG, "*****"+deepSleep.getTotalSeconds());

                        ArrayList<ActivityData> allActivities = ActivitySQLite.getInstance(getApplicationContext())
                                .getSleepSamples(before.getTimeInMillis(), today.getTimeInMillis());

                        //refreshSleepAmounts(allActivities);

                        ArrayList<String> xVals = new ArrayList<String>();

                        int i = 0;

                        float value;

                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        Date date;
                        String dateStringFrom = "";
                        String dateStringTo = "";

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        SimpleDateFormat annotationDateFormat = new SimpleDateFormat("HH:mm");

                        float movementDivisor = 180.0f;

                        for (ActivityData ad : allActivities) {

                            // determine start and end dates
                            if (i == 0) {
                                cal.setTimeInMillis(ad.getTimestamp() * 1000L); // make sure it's converted to long
                                date = cal.getTime();
                                dateStringFrom = dateFormat.format(date);
                            } else if (i == allActivities.size() - 1) {
                                cal.setTimeInMillis(ad.getTimestamp() * 1000L); // same here
                                date = cal.getTime();
                                dateStringTo = dateFormat.format(date);
                            }

                            String xLabel = "";
                            cal.setTimeInMillis(ad.getTimestamp() * 1000L);
                            date = cal.getTime();
                            String dateString = annotationDateFormat.format(date);
                            xLabel = dateString;

                            xVals.add(xLabel);

                            Log.i(TAG, "date " + dateString);
                            Log.i(TAG, "steps " + ad.getSteps());

                            value = ((float) ad.getIntensity() / movementDivisor);

                            switch (ad.getType()) {
                                case ActivityData.TYPE_DEEP_SLEEP:
                                    value += ActivityData.Y_VALUE_DEEP_SLEEP;
                                    //deep.add(new BarEntry(value, i));
                                    Log.d(TAG, "Connection failed: " + value);
                                    break;
                                case ActivityData.TYPE_LIGHT_SLEEP:
                                    //light.add(new BarEntry(value, i));
                                    Log.d(TAG, "Connection failed: " + value);
                                    break;
                                default:
                                    //unknown.add(new BarEntry(value, i));
                                    Log.d(TAG, "Connection failed: " + value);
                                    break;
                            }

                            i++;
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        Log.d(TAG, "Connection failed: " + msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "???", Toast.LENGTH_SHORT).show();
                miBand.disconnect();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Thread.sleep(2000);
                pDialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    } // ConnectBand class

    private class LoadTotalEarnedMoney extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("m_email",Info.USER_EMAIL));

            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/getTotalSleepTime");
            HttpResponse response = null;
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                response = client.execute(httpPost);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);

                Log.e(TAG, "Response : "+result);
                JSONArray jobj = new JSONArray(result);
                tmpSleepTime = Integer.parseInt(jobj.getJSONObject(0).getString("m_totalSleepTime"));
                Log.e(TAG, "tmpSleepTime : "+tmpSleepTime);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpPost = new HttpPost("http://52.78.129.84:3000/getSleepDay");
            response = null;

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                response = client.execute(httpPost);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);

                Log.e(TAG, "Response : "+result);
                JSONArray jsonArray = new JSONArray(result);
                int sum = 0;
                for(int i=0; i<jsonArray.length(); i++){
                    sum += jsonArray.getJSONObject(i).getInt("s_sleepTime");
                }
                if(deepSleepTime==0){
                    deepSleepTime = sum/jsonArray.length();
                }
                Log.e(TAG, "deepSleepTime : "+deepSleepTime);
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
            curSleepTimeTV.setText(Integer.toString(deepSleepTime));
            totalEarnedTimeTV.setText(Integer.toString(tmpSleepTime));
            totalEarnedMoneyTV.setText(Integer.toString(tmpSleepTime*5));
        }
    } // LoadTotalEarnedMoney class

    private class Donate extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            miBand.startVibration(VibrationMode.VIBRATION_NEW_FIRMWARE);
            //miBand.setLedColor(3, 5, 500);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //miBand.startVibration(VibrationMode.VIBRATION_WITH_LED);
            //miBand.customVibration(3, 800, 500);
            // member table에 추가.
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("m_email", Info.USER_EMAIL));
            post.add(new BasicNameValuePair("m_totalSleepTime", Integer.toString(tmpSleepTime+deepSleepTime)));
            Log.e(TAG, "totalSleepTime : "+Integer.toString(tmpSleepTime+deepSleepTime));

            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/updateTotalSleepTime");
            HttpResponse response = null;
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                response = client.execute(httpPost);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);

                Log.e(TAG, "Response : "+result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // sleep table에 추가.
            post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("m_email", Info.USER_EMAIL));
            post.add(new BasicNameValuePair("c_id", Integer.toString(c_id)));
            post.add(new BasicNameValuePair("s_sleepTime", Integer.toString(deepSleepTime)));
            String curDate = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                android.icu.text.SimpleDateFormat simpleDateFormat = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
                curDate = simpleDateFormat.format(new Date());
            }else{
                curDate = Info.CUR_DATE;
            }
            post.add(new BasicNameValuePair("s_date", curDate));

            httpPost = new HttpPost("http://52.78.129.84:3000/insertSleep");
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                response = client.execute(httpPost);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);

                Log.e(TAG, "Response : "+result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            curSleepTimeTV.setText("0");
            totalEarnedTimeTV.setText(Integer.toString(tmpSleepTime+deepSleepTime));
            totalEarnedMoneyTV.setText(Integer.toString((tmpSleepTime+deepSleepTime)*5));
            miBand.setLedColor(3, 5, 500);
            miBand.disconnect();
        }
    } // Donate class

}
