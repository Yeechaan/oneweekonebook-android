package com.lee.oneweekonebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DoneActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterDone adapter;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("다 읽은 책 목록");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterDone(getApplicationContext());

        adapter.setOnItemClickListener(new AdapterDone.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterDone.ViewHolder holder, View view, int position) {
                ItemDone item = adapter.getItem(position);
                int id = item.getId();

                Intent intent_done_detail = new Intent(getApplicationContext(), DoneDetailActivity.class);
                intent_done_detail.putExtra("id", id);

                startActivityForResult(intent_done_detail, 103);
            }
        });

        initDoneList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //main 으로 이동
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 103){
            initDoneList();
        }
    }

    public void initDoneList(){
        String sql = "select _id, title, writer, pub, pic_cover, st_date, fi_date from " + "read where done=1";

        if(database != null){
            Cursor cursor = database.rawQuery(sql, null);

            if(adapter.getItemCount() == 0){
                for(int i=0 ; i<cursor.getCount() ; i++){
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String writer = cursor.getString(2);
                    String pub = cursor.getString(3);
                    String pic_cover = cursor.getString(4);
                    String st_date = cursor.getString(5);
                    String fi_date = cursor.getString(6);

                    adapter.addItem(new ItemDone(id, title, writer, pub, pic_cover, st_date, fi_date, i+1));
                }
            } else{
                //즐겨찾기 디비에 정류장 추가되었을때 어뎁터 하나 추가
                if(adapter.getItemCount() < cursor.getCount()){
                    adapter.addItem(new ItemDone(0, "", "", "", "", "", "", 1));
                }
                //즐겨찾기 디비에 정류장 삭제되었을때 어뎁터 하나 제거
                else if(adapter.getItemCount() > cursor.getCount()){
                    adapter.subItem(adapter.getItemCount()-1);
                }
                for(int i=0 ; i<cursor.getCount() ; i++){
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String writer = cursor.getString(2);
                    String pub = cursor.getString(3);
                    String pic_cover = cursor.getString(4);
                    String st_date = cursor.getString(5);
                    String fi_date = cursor.getString(6);

                    adapter.getItem(i).setId(id);
                    adapter.getItem(i).setTitle(title);
                    adapter.getItem(i).setWriter(writer);
                    adapter.getItem(i).setPub(pub);
                    adapter.getItem(i).setPic_cover(pic_cover);
                    adapter.getItem(i).setSt_date(st_date);
                    adapter.getItem(i).setFi_date(fi_date);
                    adapter.getItem(i).setIndex(i+1);
                }
            }
        }
        recyclerView.setAdapter(adapter);
    }

}
