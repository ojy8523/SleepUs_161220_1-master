package com.example.seungkyu.dreamtodream;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;

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

import me.relex.circleindicator.CircleIndicator;

public class CompanyList extends AppCompatActivity {
    private String TAG = CompanyList.class.getSimpleName();
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    ListViewAdapter adapter;
    ListView listview;
    JSONArray jsonArray;

    ArrayList<CompanyVO> companyVOs = new ArrayList<CompanyVO>();

    //뷰페이져 부분
    CompanyList_MyPageAdapter myPageAdapter;
    ViewPager pager;
    Context context = this;
    CompanyList_Sqlite companyListSqlite = new CompanyList_Sqlite(context);
    //뷰페이져 부분

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FF48D1CC"));
        }
        setContentView(R.layout.activity_company_list);

        new CreateCompanyList().execute();

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        /*listview.setAdapter(adapter);*/

        //이미지 뷰페이저 넣는 부분///////////

        companyListSqlite.getWritableDatabase();
        companyListSqlite.InsertData();

        String[] arrData = companyListSqlite.SelectData("1");
        if (arrData[0].length() > 0) {
            String Flag = arrData[1];
        }

        myPageAdapter = new CompanyList_MyPageAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(myPageAdapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        pager.setOffscreenPageLimit(5);


        //이미지 뷰페이저 넣는 부분///////////

        // 서버 off일 때 주냡 test 용 버튼.
        //adapter.addItem(-1, null, "Server Off", "이므로 이걸 클릭.");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                CompanyVO item = (CompanyVO)parent.getItemAtPosition(position);

                String titleStr = item.getC_name();
                String descStr = item.getC_giveTo();
                String imgPath = item.getC_imagePath();

                Intent intent = new Intent(getApplicationContext(), CompanyViewPage.class);
                intent.putExtra("c_id", item.getC_id());
                startActivity(intent);

                String toastMessage = item.getC_name()+" is selected.";
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addDrawerItems(){
        String[] osArray = {"Log out","Market","My info","Board"};
        mAdapter = new ArrayAdapter<String>(CompanyList.this, android.R.layout.simple_list_item_1,osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Board.class);
                startActivity(intent);
                Toast.makeText(CompanyList.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Etc");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    /*int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
        return true;
    }

    // Activate the navigation drawer toggle
    if (mDrawerToggle.onOptionsItemSelected(item)) {
        return true;
    }

    return super.onOptionsItemSelected(item);*/

    private class CreateCompanyList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpGet httpGet = new HttpGet("http://52.78.129.84:3000/getCompanyList");
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
                    companyVOs.add(new CompanyVO(jobj.getInt("c_id"), jobj.getString("c_name"),
                            jobj.getString("c_giveTo"), jobj.getString("c_imagePath")));
                }
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
            for(int i=0; i<companyVOs.size(); i++){
                adapter.addItem(companyVOs.get(i).getC_id(), companyVOs.get(i).getC_imagePath(),
                        companyVOs.get(i).getC_name(), companyVOs.get(i).getC_giveTo());
            }

            listview.setAdapter(adapter);
        }
    } // CreateCompanyList Class


}