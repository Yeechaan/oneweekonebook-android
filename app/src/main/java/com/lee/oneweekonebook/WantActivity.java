package com.lee.oneweekonebook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WantActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterWant adapter;
    SQLiteDatabase database;

    int idSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want);

        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("읽고 싶은 책");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterWant(getApplicationContext());

        initWantList();

        //뷰에 아이템 클릭 이벤트 처리
        adapter.setOnItemClickListener(new AdapterWant.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterWant.ViewHolder holder, View view, int position) {
                ItemWant item = adapter.getItem(position);
                idSelected = item.getId();

                //읽고 있는 책으로 이동여부 팝업으로 선택
                PopupMenu popupMenuType = new PopupMenu(getApplicationContext(), view);
                popupReadSelected(popupMenuType);

                //Toast.makeText(getApplicationContext(), "item : " + idSelected, Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_want_add = new Intent(getApplicationContext(), WantAddActivity.class);
                startActivityForResult(intent_want_add, 101);
            }
        });
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

        if(requestCode == 101 || requestCode == 102){
            initWantList();
        }
    }

    public void initWantList(){

        String sql = "select _id, title, writer, pub, pic_cover from " + "want";

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

                    adapter.addItem(new ItemWant(id, title, writer, pub, pic_cover));
                }
            } else{
                //즐겨찾기 디비에 정류장 추가되었을때 어뎁터 하나 추가
                if(adapter.getItemCount() < cursor.getCount()){
                    adapter.addItem(new ItemWant(0, "", "", "", ""));
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

                    adapter.getItem(i).setId(id);
                    adapter.getItem(i).setTitle(title);
                    adapter.getItem(i).setWriter(writer);
                    adapter.getItem(i).setPub(pub);
                    adapter.getItem(i).setPic_cover(pic_cover);
                }
            }
        }
        recyclerView.setAdapter(adapter);
    }

    //읽고 있는 책으로 이동여부 팝업으로 선택
    public void popupReadSelected(PopupMenu popupMenu){
        getMenuInflater().inflate(R.menu.option_type, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.m1:
                        startRead();
                        break;
                    case R.id.m2:
                        //수정하기
                        editWant();
                        break;
                    case R.id.m3:
                        //삭제하기
                        deleteWant();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    //읽고 싶은 책 -> 읽는 중인 책으로 이동
    public void startRead() {
        String sql = "select _id, title, writer, pub, pic_cover from " + "want where _id=" + idSelected;

        if (database != null) {
            Cursor cursor = database.rawQuery(sql, null);

            cursor.moveToNext();
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String writer = cursor.getString(2);
            String pub = cursor.getString(3);
            String pic_cover = cursor.getString(4);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            //테이블 변경 want->read
            String sqlInsert = "insert into read(title, writer, pub, pic_cover, st_date) values(?, ?, ?, ?, ?)";
            Object[] params = {title, writer, pub, pic_cover, timeStamp};
            database.execSQL(sqlInsert, params);

            //read 테이블에서 삭제
            String sqlDelete = "delete from want where _id=" + id;
            database.execSQL(sqlDelete);
        }
        Toast.makeText(getApplicationContext(), "읽고 있는 책 list 추가", Toast.LENGTH_SHORT).show();
        //main 이동
        finish();
    }

    //읽고 싶은 책 수정
    public void editWant(){
        Intent intent_want_edit = new Intent(getApplicationContext(), WantEditActivity.class);
        intent_want_edit.putExtra("id", idSelected);

        startActivityForResult(intent_want_edit, 102);
    }

    //읽고 싶은 책 삭제
    public void deleteWant(){
        if(database != null){
            String sqlDelete = "delete from want where _id=" + idSelected;
            database.execSQL(sqlDelete);

            initWantList();

            Toast.makeText(getApplicationContext(), "읽고 싶은 목록에서 삭제되었습니다~", Toast.LENGTH_SHORT).show();
        }
    }
}
