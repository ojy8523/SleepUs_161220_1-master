package com.example.seungkyu.dreamtodream;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class Signup extends AppCompatActivity {
    InputMethodManager imm;
    private String TAG = MainActivity.class.getSimpleName();
    Button emailCheckBtn, dateBtn, signupBtn, backBtn2;
    EditText emailET, passwordET, passwordET2, nameET, phoneNumberET;
    RadioGroup genderRG;
    RadioButton genderRadioBtn; // 현재 선택되어있는 라디오 버튼을 담을 변수
    TextView emailCheckTV, birthdayTV;
    String flagMsg; // emailCheckTV에 사용가능한지 불가능한지 나타내는 메세지 저장.
    int flagMsgNum; // -1이면 사용가능한 이메일, 1이면 이미 존재하는 이메일
    String inputEmail; // 처음 중복체크했을 때 적혀있던 이메일을 저장. 가입버튼을 누르기 직전 이메일을 바꾸는 것에 대한 대비.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailCheckBtn = (Button)findViewById(R.id.emailCheckBtn);
        dateBtn = (Button)findViewById(R.id.dateBtn);
        signupBtn = (Button)findViewById(R.id.signupBtn);
        backBtn2 = (Button)findViewById(R.id.backBtn2);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        emailET = (EditText)findViewById(R.id.emailET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        passwordET2 = (EditText)findViewById(R.id.passwordET2);
        nameET = (EditText)findViewById(R.id.nameET);
        genderRG = (RadioGroup)findViewById(R.id.genderRG);
        phoneNumberET = (EditText)findViewById(R.id.phoneNumberET);

        emailCheckTV = (TextView)findViewById(R.id.emailCheckTV);
        birthdayTV = (TextView)findViewById(R.id.birthdayTV);

        // 이메일 중복체크 버튼 클릭시.
        emailCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                if(email!=null && email.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    inputEmail = email;
                    new SendPost().execute();
                }
            }
        });

        // 날짜선택 버튼 클릭시.
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        // 회원가입 버튼 클릭시.
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderRadioBtn = (RadioButton)findViewById(genderRG.getCheckedRadioButtonId());
                if(!emailET.getText().toString().isEmpty() && !passwordET.getText().toString().isEmpty() && !passwordET2.getText().toString().isEmpty()
                        && !nameET.getText().toString().isEmpty() && !phoneNumberET.getText().toString().isEmpty()
                        && !birthdayTV.getText().toString().isEmpty() && genderRadioBtn!=null){
                    //Toast.makeText(getApplicationContext(), "다입력했당", Toast.LENGTH_SHORT).show();
                    if(passwordET.getText().toString().equals(passwordET2.getText().toString())){
                        /*MemberVO mvo = new MemberVO(emailET.getText().toString(), passwordET.getText().toString(), genderRadioBtn.getText().toString(),
                                birthdayTV.getText().toString().replaceAll("월","-"), phoneNumberET.getText().toString(), nameET.getText().toString());

                        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
                        post.add(new BasicNameValuePair("m_email",mvo.getM_email()));
                        post.add(new BasicNameValuePair("m_password",mvo.getM_password()));
                        post.add(new BasicNameValuePair("m_name",mvo.getM_name()));
                        post.add(new BasicNameValuePair("m_gender",mvo.getM_gender()));
                        post.add(new BasicNameValuePair("m_phoneNumber",mvo.getM_phoneNumber()));
                        post.add(new BasicNameValuePair("m_birthday",mvo.getM_birthday()));

                        try {
                            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/signup");
                            httpPost.setEntity(entity);

                            HttpClient client = new DefaultHttpClient();
                            client.execute(httpPost); // thread로 해야하나???????????????
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        if(flagMsgNum==-1 && emailET.getText().toString().equals(inputEmail))
                            new InsertMember().execute();
                        else
                            Toast.makeText(getApplicationContext(), "이메일 중복체크를 해주십숑", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "뭐 빠트렸어요...", Toast.LENGTH_SHORT).show();
                    //if(sexRadioBtn!=null)
                    //Toast.makeText(getApplicationContext(), genderRadioBtn.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    } // onCreate method

    protected Dialog onCreateDialog(int id) {
        DatePickerDialog dpd = new DatePickerDialog(Signup.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                //Toast.makeText(getApplicationContext(), year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일 을 선택했습니다", Toast.LENGTH_SHORT).show();
                birthdayTV.setText(year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일");
            }
        }, 1989, 12, 31);
        return dpd;
    }

    // 중복체크 버튼을 누르면 적은 이메일을 서버로 보내서 중복된 값이 있는지 체크하는 쓰레드.
    private class SendPost extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {
            String content = executeClient();
            return content;
        }

        protected void onPostExecute(String result) {
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
        }

        public String executeClient() {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            //post.add(new BasicNameValuePair("userId", "sibal"));
            //post.add(new BasicNameValuePair("password", "한글왜안되냐"));
            post.add(new BasicNameValuePair("m_email",emailET.getText().toString()));

            // 연결 HttpClient 객체 생성
            HttpClient client = new DefaultHttpClient();

            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            // Post객체 생성
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/emailCheck");

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

                if(flag==false){
                    Log.e(TAG, "이미 존재하는 이메일입니다.");
                    //emailCheckTV.setText("이미 존재하는 이메일입니다."); // error 발생, 하나의 스레드에서 이중적으로 불가능?
                    new EmailCheckTVChanging().execute();
                    flagMsg = "이미 존재하는 이메일입니다.";
                    flagMsgNum = 1;
                }
                else{
                    Log.e(TAG, "사용가능한 이메일입니다.");
                    new EmailCheckTVChanging().execute();
                    flagMsg = "사용가능한 이메일입니다.";
                    flagMsgNum = -1;
                }

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
    } // SendPost class

    // 이메일 중복여부메세지를 변경해주는 쓰레드.
    private class EmailCheckTVChanging extends AsyncTask<String, String, String>{
        @Override
        protected void onPostExecute(String aa) {
            super.onPostExecute(aa);
            String strColor;
            if(flagMsgNum==1) strColor = "#FF0000";
            else strColor = "#0000FF";

            emailCheckTV.setText(flagMsg);
            emailCheckTV.setTextColor(Color.parseColor(strColor));
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    // 회원정보를 서버로 보내서 DB에 insert하는 쓰레드.
    private class InsertMember extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "잠시만 기다려주세요...\n가입완료 후 로그인화면으로 이동합니다.", Toast.LENGTH_LONG).show();
        }

        protected String doInBackground(Void... unused) {
            String content = executeClient();
            return content;
        }

        protected void onPostExecute(String result) {
            finish();
        }

        public String executeClient() {
            String birthday = birthdayTV.getText().toString().replaceAll("년 ","-");
            birthday = birthday.replaceAll("월 ","-");
            birthday = birthday.replaceAll("일","");
            MemberVO mvo = new MemberVO(emailET.getText().toString(), passwordET.getText().toString(), nameET.getText().toString(),
                    genderRadioBtn.getText().toString(), phoneNumberET.getText().toString(), birthday);
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("m_email",mvo.getM_email()));
            post.add(new BasicNameValuePair("m_password",mvo.getM_password()));
            post.add(new BasicNameValuePair("m_name",mvo.getM_name()));
            post.add(new BasicNameValuePair("m_gender",mvo.getM_gender()));
            post.add(new BasicNameValuePair("m_phoneNumber",mvo.getM_phoneNumber()));
            post.add(new BasicNameValuePair("m_birthday",mvo.getM_birthday()));
            //Log.e(TAG, mvo.toString());

            HttpClient client = new DefaultHttpClient();

            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            // node.js 에서도 try, catch는 꼭 해보도록 하자.!!!!!!!!!!!!!
            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/signup");

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                client.execute(httpPost);

                Log.e(TAG, mvo.toString());
                //return EntityUtils.getContentCharSet(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    } // InsertMember class

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(emailET.getWindowToken(), 0);
    }

} // Signup class
