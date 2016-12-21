package com.example.seungkyu.dreamtodream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by junyepoh on 2016. 12. 14..
 */

public class WriteBoardPage extends AppCompatActivity {
    private String TAG = WriteBoardPage.class.getSimpleName();
    EditText titleET, contentET;
    LinearLayout contentLinearLayout;
    InputMethodManager mInputMethodManager;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board_page);

        titleET = (EditText)findViewById(R.id.titleET);
        contentET = (EditText)findViewById(R.id.contentET);
        contentLinearLayout = (LinearLayout)findViewById(R.id.contentLinearLayout);

        mContext = getApplicationContext();

        contentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentET.setFocusable(true);
                contentET.setFocusableInTouchMode(true);
                contentET.requestFocus();
                contentET.setCursorVisible(true);
                showKeyBoard();
            }
        });


        ImageButton leftButton = (ImageButton) findViewById(R.id.stopWrite);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Board.class);
                finish();
                startActivity(intent);
                Toast.makeText(WriteBoardPage.this, "게시판으로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        //글쓰기 넣어야 하는 부분

        ImageButton writeButton = (ImageButton) findViewById(R.id.writeFinishButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleET.getText().toString().isEmpty()){
                    Toast.makeText(WriteBoardPage.this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(contentET.getText().toString().isEmpty()){
                    Toast.makeText(WriteBoardPage.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteBoardPage.this);
                    alert_confirm.setMessage("글을 등록하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new WriteBoard().execute();
                                /*Intent intent = new Intent(getApplicationContext(), Board.class);
                                finish();
                                startActivity(intent);
                                Toast.makeText(WriteBoardPage.this, "글쓰기 완료", Toast.LENGTH_SHORT).show();*/
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }
            }
        });


        /*View myButtonLayout = getLayoutInflater().inflate(R.layout.board_test_page,null);
        ActionBar ab = getSupportActionBar();

        ab.setCustomView(myButtonLayout);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|
        ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM);*/
    }


    /*public void onBtnClicked(View view){
        LinearLayout ll = (LinearLayout) findViewById(R.id.body);
        switch (view.getId()){
            case R.id.white:
                ll.setBackgroundColor(Color.WHITE);
                break;
            case R.id.black:
                ll.setBackgroundColor(Color.BLACK);
        }
    }*/

    public void showKeyBoard(){
        if(mInputMethodManager == null)
            mInputMethodManager = (InputMethodManager) mContext.getSystemService(this.getApplicationContext().INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void disAppearKeyBoard(){
        if(mInputMethodManager == null)
            mInputMethodManager = (InputMethodManager) mContext.getSystemService(WriteBoardPage.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private class WriteBoard extends AsyncTask<Void, Void, String>{
        ProgressDialog pDialog;
        BoardVO boardVO;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String curDate = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                curDate = simpleDateFormat.format(new Date());
            }else{
                curDate = Info.CUR_DATE;
            }
            boardVO = new BoardVO(titleET.getText().toString(),contentET.getText().toString(),curDate,0,Info.NAME,Info.USER_EMAIL);
            pDialog = new ProgressDialog(WriteBoardPage.this);
            pDialog.setMessage("글 등록중...");
            pDialog.show();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(getApplicationContext(), Board.class);
            finish();
            startActivity(intent);
            Toast.makeText(WriteBoardPage.this, "글쓰기 완료", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("mb_title",boardVO.getTitle()));
            post.add(new BasicNameValuePair("mb_content",boardVO.getContent()));
            post.add(new BasicNameValuePair("mb_writeDate",boardVO.getDate()));
            post.add(new BasicNameValuePair("m_email",boardVO.getEmail()));
            post.add(new BasicNameValuePair("m_name",boardVO.getName()));

            HttpClient client = new DefaultHttpClient();

            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpPost httpPost = new HttpPost("http://52.78.129.84:3000/writeBoard");
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                client.execute(httpPost);

                Log.e(TAG, boardVO.toString());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
