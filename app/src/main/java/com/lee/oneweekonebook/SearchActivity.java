package com.lee.oneweekonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lee.oneweekonebook.Data.BookList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    EditText etxt_search;
    TextView textView;

    RecyclerView recyclerView;
    AdapterSearch adapter;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView = (TextView) findViewById(R.id.textView);
        etxt_search = (EditText) findViewById(R.id.etxt_search);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterSearch(getApplicationContext());

        adapter.setOnItemClickListener(new AdapterSearch.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterSearch.ViewHolder holder, View view, int position) {
                //when listView item clicked
            }
        });

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("도서 검색");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etxt_search.getText().toString();
                sendRequestOffice(title);
            }
        });

        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //done 으로 이동
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendRequestOffice(String title){
        String url = "http://15.164.219.19:3000/book?title=" + title;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processBookResponse(response);
                        //println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("잠시 후에 다시 시도해주세요.");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("title", String.valueOf(title));

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processBookResponse(String response){
        Gson gson = new Gson();

        if(response.equals("400")){
            Toast.makeText(getApplicationContext(), "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            BookList items = gson.fromJson(response, BookList.class);
            int size = items.documents.size();

            if(adapter.getItemCount() == 0){
                for(int i=0 ; i<items.documents.size() ; i++){
                    //int id = cursor.getInt(0);
                    String title = items.documents.get(i).title;
                    String pub = items.documents.get(i).publisher;
                    String pic_cover = items.documents.get(i).thumbnail;
                    String st_date = items.documents.get(i).datetime;
                    st_date = st_date.substring(0, 10);
                    String writer = "";

                    for(int j=0 ; j<items.documents.get(i).authors.size() ; j++){
                        writer += items.documents.get(i).authors.get(j);
                        writer += "  ";
                    }

                    adapter.addItem(new ItemSearch(i, title, writer, pub, st_date, pic_cover, i+1));
                }
            }
            else{
                adapter = new AdapterSearch(getApplicationContext());
                for(int i=0 ; i<items.documents.size() ; i++){
                    //int id = cursor.getInt(0);
                    String title = items.documents.get(i).title;
                    String pub = items.documents.get(i).publisher;
                    String pic_cover = items.documents.get(i).thumbnail;
                    String st_date = items.documents.get(i).datetime;
                    st_date = st_date.substring(0, 10);
                    String writer = "";

                    for(int j=0 ; j<items.documents.get(i).authors.size() ; j++){
                        writer += items.documents.get(i).authors.get(j);
                        writer += "  ";
                    }

                    adapter.addItem(new ItemSearch(i, title, writer, pub, st_date, pic_cover, i+1));
                }
            }


            recyclerView.setAdapter(adapter);
            //println(items.documents.get(0).title);
        }

    }

    public void println(String data){
        textView.append(data + "\n");
    }
}
