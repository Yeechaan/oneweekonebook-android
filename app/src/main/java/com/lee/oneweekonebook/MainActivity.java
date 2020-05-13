package com.lee.oneweekonebook;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterRead adapter;
    SQLiteDatabase database;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requirePermission();

        //광고 admob
        //MobileAds.initialize(this, getString(R.string.admob_app_id));

        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("일주일책 2020");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterRead(getApplicationContext());

        initReadList();

        //읽는 중인 책이 선택되었을때 이벤트 처리
        adapter.setOnItemClickListener(new AdapterRead.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterRead.ViewHolder holder, View view, int position) {
                ItemRead item = adapter.getItem(position);
                int id = item.getId();

                Intent intent_read = new Intent(getApplicationContext(), ReadActivity.class);
                intent_read.putExtra("id", id);
                //중간 엑티비티 지우고 기존 엑티비티 재사용
                intent_read.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intent_read, 103);
            }
        });

        //readAdd 읽는 중인 책 추가
        Button btn_read_add = (Button) findViewById(R.id.btn_read_add);
        btn_read_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_read_add = new Intent(getApplicationContext(), ReadAddActivity.class);
                //중간 엑티비티 지우고 기존 엑티비티 재사용
                intent_read_add.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent_read_add, 102);
            }
        });

        //책 검색하기
        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //검색 테스트
                Intent intent_book_search = new Intent(getApplicationContext(), SearchActivity.class);
                //중간 엑티비티 지우고 기존 엑티비티 재사용
                intent_book_search.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent_book_search, 102);
            }
        });

        //want 읽고싶은 책 목록으로 이동
        Button btn_want = (Button) findViewById(R.id.btn_want);
        btn_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_want = new Intent(getApplicationContext(), WantActivity.class);
                //기존 엑티비티 재사용
                intent_want.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent_want, 101);
            }
        });

        //done 독서완료 목록으로 이동
        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_done = new Intent(getApplicationContext(), DoneActivity.class);
                startActivityForResult(intent_done, 104);
            }
        });

    }

//    //액션바 업데이트
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_title, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int curId = item.getItemId();
//        switch (curId){
//            case R.id.menu_refresh:
//                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menu_search:
//                Toast.makeText(getApplicationContext(), "검색", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menu_settings:
//                Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //어플종료
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("종료 하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DB에 저장 후 "독서-done" 으로 이동
                        finish();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        return;
                    }
                });
                builder.show();

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DB에 저장 후 "독서-done" 으로 이동
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                return;
            }
        });

        builder.show();
    }

    //읽는 중인 책 업데이트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 || requestCode == 102 || requestCode == 103){
            initReadList();
        }
    }

    //읽는 중인 책 리싸이클러뷰에 초기화
    public void initReadList(){
        String sql = "select _id, title, writer, pub, pic_cover, st_date from " + "read where done=0";

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

                    adapter.addItem(new ItemRead(id, title, writer, pub, pic_cover, st_date));
                }
            } else{
                //즐겨찾기 디비에 정류장 추가되었을때 어뎁터 하나 추가
                if(adapter.getItemCount() < cursor.getCount()){
                    adapter.addItem(new ItemRead(0, "", "", "", "", ""));
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

                    adapter.getItem(i).setId(id);
                    adapter.getItem(i).setTitle(title);
                    adapter.getItem(i).setWriter(writer);
                    adapter.getItem(i).setPub(pub);
                    adapter.getItem(i).setPic_cover(pic_cover);
                    adapter.getItem(i).setSt_date(st_date);
                }
            }
        }
        recyclerView.setAdapter(adapter);
    }

    //권한 확인 모듈
    void requirePermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> listPermissionsNeeded = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                //권한이 허가가 안됬을 경우 요청할 권한을 모집하는 부분
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            //권한 요청 하는 부분
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }
}
