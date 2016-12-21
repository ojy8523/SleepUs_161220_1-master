package com.example.seungkyu.dreamtodream;

/**
 * Created by junyepoh on 2016. 12. 14..
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Board extends FragmentActivity {
    private String TAG = Board.class.getSimpleName();
    BoardListViewAdapter adapter;
    ListView listview;
    JSONArray jsonArray;
    ArrayList<BoardVO> boardVOs = new ArrayList<BoardVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#46BBEC"));}
        setContentView(R.layout.activity_board);

        new CreateBoardList().execute();

        adapter = new BoardListViewAdapter();
        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.boardLV);

        ImageButton leftButton = (ImageButton) findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompanyList.class);
                startActivity(intent);
                Toast.makeText(Board.this, "메인 페이지로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton writeButton = (ImageButton) findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteBoardPage.class);
                startActivity(intent);
                //Toast.makeText(Board.this, "글쓰기로 완료", Toast.LENGTH_SHORT).show();
            }
        });


        /*adapter.addItem("게시판 ㅅㅂ 되냐?", "seungkyu","2016-12-03");
        adapter.addItem("미밴드 거의 쌔삥 살사람 있냐?", "junyep","2016-12-03");
        adapter.addItem("이거 앱 좋음?", "seungkyu","2016-12-03");
        adapter.addItem("와 3시간 깊으잠 개이득", "gildong","2016-12-03");
        adapter.addItem("미밴드 공동구매 하실분 구해요^^", "닉넴멀로하지","2016-12-03");
        adapter.addItem("그냥 심심해서 써봄", "jooyoung","2016-12-03");
        adapter.addItem("아 ㅡㅡ 님들 질문있음", "solder76","2016-12-03");
        adapter.addItem("이거 어캐함?", "루시우","2016-12-03");
        adapter.addItem("오버워치 같이 하실분 구함", "파라","2016-12-03");
        adapter.addItem("롤 캐리해드림", "메르띠","2016-12-03");
        adapter.addItem("안녕하세요ㅎㅎ", "라인하르트","2016-12-03");
        adapter.addItem("어디로 가야 하오", "리신","2016-12-03");
        adapter.addItem("내 이름은 맥크리", "맥크리","2016-12-03");*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                BoardVO item = (BoardVO) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), BoardViewPage.class);
                intent.putExtra("id",item.getId());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("name",item.getName());
                intent.putExtra("date",item.getDate());
                //intent.putExtra("content","여기부터 본문입니다.\n댓글은 또 언제 구현하나...");
                startActivity(intent);
            }
        });
    }
    private class CreateBoardList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpGet httpGet = new HttpGet("http://52.78.129.84:3000/getBoardList");
            HttpResponse response = null;
            try {
                response = client.execute(httpGet);

                HttpHandler handler = new HttpHandler();
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                String result = handler.convertStreamToString(in);
                Log.e(TAG, "Response : "+result);

                jsonArray = new JSONArray(result);

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jobj = jsonArray.getJSONObject(i);
                    boardVOs.add(new BoardVO(jobj.getInt("mb_id"), jobj.getString("mb_title"),
                            jobj.getString("m_name"), jobj.getString("mb_writeDate")));
                }
                Log.e(TAG, "Response : "+boardVOs.size());
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
            for(int i=0; i<boardVOs.size(); i++){
                adapter.addItem(boardVOs.get(i).getId(), boardVOs.get(i).getTitle(),
                        boardVOs.get(i).getName(), boardVOs.get(i).getDate().substring(0,10));
            }
            listview.setAdapter(adapter);
        }
    } // CreateBoardList

}
