package com.example.seungkyu.dreamtodream;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    InputMethodManager imm;
    private String TAG = MainActivity.class.getSimpleName();
    EditText emailLoginET, passwordLoginET;
    Button loginBtn, signupBtn;
    boolean loginFlag = false; // 로그인 성공시 true.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        startActivity(new Intent(this,SplashPage.class));


        emailLoginET = (EditText)findViewById(R.id.emailLoginET);
        passwordLoginET = (EditText)findViewById(R.id.passwordLoginET);

        // 키보드 내리기위한 부분.
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        signupBtn = (Button)findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailLoginET.getText().toString().equals("11")&&passwordLoginET.getText().toString().equals("11")){
                    Intent intent = new Intent(MainActivity.this, CompanyList.class);
                    startActivity(intent);
                }
                if(emailLoginET.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                }else if(passwordLoginET.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }else {
                    new Login().execute();
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CleanTextView().execute();
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

    } // onCreate method

    private class Login extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {
            String content = executeClient();
            return content;
        }

        protected void onPostExecute(String result) {
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
        }

        public String executeClient() {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("m_email",emailLoginET.getText().toString()));
            post.add(new BasicNameValuePair("m_password",passwordLoginET.getText().toString()));

            HttpClient client = new DefaultHttpClient();

            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/login");

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                HttpResponse response = client.execute(httpPost);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);
                Log.e(TAG, "Response : "+result);

                JSONArray jsonArray = new JSONArray(result);
                boolean flag = jsonArray.isNull(0);
                Log.e(TAG, String.valueOf(flag));

                if(flag==false){ // 로그인 성공
                    loginFlag = true;
                    Info.NAME = jsonArray.getJSONObject(0).getString("m_name");
                }
                else{ // 로그인 실패
                    loginFlag = false;
                }
                new LoginResultMsg().execute();

                return EntityUtils.getContentCharSet(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    } // Login class

    private class LoginResultMsg extends AsyncTask<String, String, String>{
        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);
            if(loginFlag){
                Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                new CleanTextView().execute();
                Intent intent = new Intent(MainActivity.this, CompanyList.class);
                Info.USER_EMAIL = emailLoginET.getText().toString();
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    private class CleanTextView extends AsyncTask<String, String, String>{
        @Override
        protected void onPostExecute(String a) {
            passwordLoginET.setText("");
            super.onPostExecute(a);
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(emailLoginET.getWindowToken(), 0);
    }


}
