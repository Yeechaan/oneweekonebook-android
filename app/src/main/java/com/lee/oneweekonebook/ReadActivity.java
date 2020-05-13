package com.lee.oneweekonebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadActivity extends AppCompatActivity {
//    RecyclerView recyclerView;
//    AdapterReview adapter;
    SQLiteDatabase database;

    EditText editText3, editText4;

    int idSelected;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        Intent passedIntent = this.getIntent();
        idSelected = passedIntent.getIntExtra("id", 1);

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);

        initRead();

//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new AdapterReview(getApplicationContext());
//
//        adapter.setOnItemClickListener(new AdapterReview.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterReview.ViewHolder holder, View view, int position) {
//                ItemReview item = adapter.getItem(position);
//                int id = item.getId();
//
//                Toast.makeText(getApplicationContext(), "item : " + id, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        initReviewList();

        //책 임시 저장하기
        Button btn_read_save = (Button) findViewById(R.id.btn_read_save);
        btn_read_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        //책 삭제하기
        Button btn_read_delete = (Button) findViewById(R.id.btn_read_delete);
        btn_read_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReadActivity.this);
                builder.setTitle("읽고 있는 책을 지우시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //삭제 이벤트 처리
                        deleteData();
                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.create().show();
            }
        });

        //책 추가하기
        Button btn_read_add = (Button) findViewById(R.id.btn_read_add);
        btn_read_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReadActivity.this);
                builder.setTitle("독서를 마무리 하시겠습니까?");
                builder.setMessage("독서 완료 목록으로 이동합니다");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DB에 저장 후 "독서-done" 으로 이동
                        doneData();
                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.create().show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //main 으로 이동할까? 왜나면 중간 read & readAdd 에서 flag 를 clear_top 으로 스택에서 지웠으니까
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteData(){
        if(database != null){
            String sqlDelete = "delete from read where _id=" + idSelected;
            database.execSQL(sqlDelete);

            Toast.makeText(getApplicationContext(), "독서 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void saveData(){
        if(database != null){
            String sqlUpdate = "update read set fi_memo=?, fi_review=?, save=? where _id=" + idSelected;
            Object[] params = {editText3.getText().toString(), editText4.getText().toString(), 1};
            database.execSQL(sqlUpdate, params);

            Toast.makeText(getApplicationContext(), "내용이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void doneData(){
        if(database != null){
            //페이지가 공백이면 0으로 초기화
//            if(TextUtils.isEmpty(editText3.getText().toString())){
//                Object[] params = {editText3.getText().toString(), 0, editText3.getText().toString(), editText4.getText().toString(), idSelected};
//                database.execSQL(sqlInsert, params);
//            }
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String sqlUpdate = "update read set fi_memo=?, fi_review=?, fi_date=?, done=? where _id=" + idSelected;
            Object[] params = {editText3.getText().toString(), editText4.getText().toString(), timeStamp, 1};
            database.execSQL(sqlUpdate, params);

            Intent intent_done = new Intent(getApplicationContext(), DoneActivity.class);
            //중간 엑티비티 지우고 기존 엑티비티 재사용
            intent_done.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_done);

            //Toast.makeText(getApplicationContext(), "책 읽기 완료", Toast.LENGTH_SHORT).show();
            //initReviewList();
        }
    }

    public void initRead(){
        String sql = "select title, save, fi_memo, fi_review from " + "read" + " where _id=" + idSelected;

        if(database != null) {
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToNext();
            String title = cursor.getString(0);
            int save = cursor.getInt(1);

            //제목이 공백이면, (문자열 비교 equals() 사용)
            if(title.equals("")){
                mTitle.setText("마음의 양식");
            }else{
                mTitle.setText(title);
            }

            //저장된 내용이면, (save flag 비교 0/1)
            if(save == 1){
                String fi_memo = cursor.getString(2);
                String fi_review = cursor.getString(3);

                //빈 내용이 아니면, 뷰에 표시
                if(!fi_memo.equals("")){
                    editText3.setText(fi_memo);
                }
                if(!fi_memo.equals("")){
                    editText4.setText(fi_review);
                }
            }
        }
        else{
            mTitle.setText("마음의 양식");
        }
    }

    /*
    //읽는 중인 책 리싸이클러뷰에 초기화
    public void initReviewList(){
        String sql = "select _id, sub_title, page, contents, review from " + "book where read_id=" + idSelected;

        if(database != null){
            Cursor cursor = database.rawQuery(sql, null);

            if(adapter.getItemCount() == 0){
                for(int i=0 ; i<cursor.getCount() ; i++){
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    String sub_title = cursor.getString(1);
                    String page = cursor.getString(2);
                    String contents = cursor.getString(3);
                    String review = cursor.getString(4);

                    adapter.addItem(new ItemReview(id, sub_title, page, contents, review));
                }
            } else{
                //즐겨찾기 디비에 정류장 추가되었을때 어뎁터 하나 추가
                if(adapter.getItemCount() < cursor.getCount()){
                    adapter.addItem(new ItemReview(0, "", "", "", ""));
                }
                //즐겨찾기 디비에 정류장 삭제되었을때 어뎁터 하나 제거
                else if(adapter.getItemCount() > cursor.getCount()){
                    adapter.subItem(adapter.getItemCount()-1);
                }
                for(int i=0 ; i<cursor.getCount() ; i++){
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    String sub_title = cursor.getString(1);
                    String page = cursor.getString(2);
                    String contents = cursor.getString(3);
                    String review = cursor.getString(4);

                    adapter.getItem(i).setId(id);
                    adapter.getItem(i).setSub_title(sub_title);
                    adapter.getItem(i).setPage(page);
                    adapter.getItem(i).setContents(contents);
                    adapter.getItem(i).setReview(review);
                }
            }
        }
        recyclerView.setAdapter(adapter);
    }
    */
}
