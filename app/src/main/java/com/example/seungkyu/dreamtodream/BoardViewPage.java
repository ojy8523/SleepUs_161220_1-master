package com.example.seungkyu.dreamtodream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

public class BoardViewPage extends AppCompatActivity {
    private String TAG = BoardViewPage.class.getSimpleName();
    int boardId;
    TextView nameTV, dateTV, titleTV, contentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view_page);

        new GetBoardContent().execute();

        Intent intent = getIntent();
        nameTV = (TextView)findViewById(R.id.nameTextView);
        dateTV = (TextView)findViewById(R.id.dateTextView);
        titleTV = (TextView)findViewById(R.id.titleTextView);
        contentTV = (TextView)findViewById(R.id.contentTextView);

        boardId = intent.getExtras().getInt("id");
        nameTV.setText(intent.getExtras().getString("name"));
        dateTV.setText(intent.getExtras().getString("date"));
        titleTV.setText(intent.getExtras().getString("title"));

        Log.e(TAG, "Res : "+boardId);
    }

    private class GetBoardContent extends AsyncTask<Void, Void, Void> {
        String content;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("mb_id", Integer.toString(boardId)));

            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/getBoardContent");
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
                content = jobj.getJSONObject(0).getString("mb_content");
                Log.e(TAG, "content : "+content);
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
            contentTV.setText(content);
        }
    }
}
