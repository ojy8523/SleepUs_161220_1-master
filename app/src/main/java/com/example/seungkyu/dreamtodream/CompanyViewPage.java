package com.example.seungkyu.dreamtodream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.betomaluje.miband.ActionCallback;
import com.betomaluje.miband.DateUtils;
import com.betomaluje.miband.MiBand;
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


public class CompanyViewPage extends FragmentActivity {
    private String TAG = CompanyViewPage.class.getSimpleName();
    int c_id;
    ImageButton startSleep;
    ImageView mImgTrans;
    Bitmap mBitmap;
    Button backToCompanylist;
    TextView charityExplainTV;
    MiBand miBand;
    private int deepSleepTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FF48D1CC"));
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_company_start_view);
        Intent intent = getIntent();
        c_id = intent.getExtras().getInt("c_id");

        miBand = MiBand.getInstance(CompanyViewPage.this);




        startSleep = (ImageButton) findViewById(R.id.startSleep);
        mImgTrans = (ImageView) findViewById(R.id.imgTranslate);
        backToCompanylist = (Button) findViewById(R.id.backToCompanyList);
        charityExplainTV = (TextView)findViewById(R.id.charityExplain);

        backToCompanylist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent intent =  new Intent(getApplicationContext(),CompanyList.class);
                startActivity(intent);

            }
        });



        startSleep.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                new ConnectBand().execute();
                Intent intent =  new Intent(getApplicationContext(),StartCharity.class);
                intent.putExtra("c_id",c_id);
                intent.putExtra("deepSleepTime",deepSleepTime);
                finish();
                startActivity(intent);

            }
        });

        new LoadImage().execute("http://52.78.129.84:3000/images/test2.png");
    }




    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        ProgressDialog pDialog;
        String description;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CompanyViewPage.this);
            pDialog.setMessage("이미지 로딩중입니다...");
            pDialog.show();
        }

        protected Bitmap doInBackground(String... args) {
            String imgPath = "";
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("c_id", Integer.toString(c_id)));

            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/getCharityCompany");
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
                imgPath = jsonArray.getJSONObject(0).getString("c_imagePath2");
                description = jsonArray.getJSONObject(0).getString("c_description");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                mBitmap = BitmapFactory.decodeStream((InputStream) new URL(imgPath).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap image) {
            charityExplainTV.setText(description);
            if (image != null) {
                mImgTrans.setImageBitmap(image);
                pDialog.dismiss();

            } else {
                pDialog.dismiss();
                Toast.makeText(CompanyViewPage.this, "이미지가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ConnectBand extends AsyncTask<Void, Void, Void>{
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CompanyViewPage.this);
            pDialog.setMessage("밴드 연결중입니다...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);

                //***************************
                if (!miBand.isConnected()) {
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
                            deepSleepTime = (int)(deepSleep.getTotalSeconds()/60);

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
                    miBand.disconnect();
                }
                miBand.setLedColor(3, 5, 500);
                //***************************
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
        }
    }
}
