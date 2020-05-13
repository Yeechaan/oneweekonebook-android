package com.lee.oneweekonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class DoneDetailEditActivity extends AppCompatActivity {
    SQLiteDatabase database;
    ImageView img_cover;
    EditText editText3, editText4;
    TextView txt_title;
//    TextView txt_writer, txt_pub, txt_st_date, txt_fi_date;
//    String mCurrentPhotoPath;

    int idSelected;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_detail_edit);

        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        Intent passedIntent = this.getIntent();
        idSelected = passedIntent.getIntExtra("id", 1);

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("수정하기 Page");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        txt_title = (TextView) findViewById(R.id.txt_title);
//        txt_writer = (TextView) findViewById(R.id.txt_writer);
//        txt_pub = (TextView) findViewById(R.id.txt_pub);
//        txt_st_date = (TextView) findViewById(R.id.txt_st_date);
//        txt_fi_date = (TextView) findViewById(R.id.txt_fi_date);
//        img_cover = (ImageView) findViewById(R.id.img_cover);

        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);

        //책 임시 저장하기
        Button btn_read_save = (Button) findViewById(R.id.btn_read_save);
        btn_read_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        setInit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //doneDetail 으로 이동
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveData(){
        if(database != null){
            String sqlUpdate = "update read set fi_memo=?, fi_review=? where _id=" + idSelected;
            Object[] params = {editText3.getText().toString(), editText4.getText().toString()};
            database.execSQL(sqlUpdate, params);

            Toast.makeText(getApplicationContext(), "수정된 내용 저장", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void setInit(){
        if(database != null){
            String sql = "select title, writer, pub, pic_cover, st_date, fi_date, fi_memo, fi_review from " + "read" + " where _id=" + idSelected;

            if(database != null) {
                Cursor cursor = database.rawQuery(sql, null);

                cursor.moveToNext();
                txt_title.setText(cursor.getString(0));
//                txt_writer.setText(cursor.getString(1));
//                txt_pub.setText(cursor.getString(2));
//                mCurrentPhotoPath = cursor.getString(3);
//                txt_st_date.setText(cursor.getString(4));
//                txt_fi_date.setText(cursor.getString(5));
                editText3.setText(cursor.getString(6));
                editText4.setText(cursor.getString(7));
//
//                File imgFile = new  File(mCurrentPhotoPath);
//                if(imgFile.exists()){
//                    Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
//                    setTitleImage(uri, mCurrentPhotoPath);                }
//                //저장된 표지 사진이 없으면 일반사진으로 대체
//                else{
//                    img_cover.setImageResource(R.drawable.ic_book);
//                }
            }
        }
    }

    //사진 뷰에 띄운다 (회전 후)
    public void setTitleImage(Uri uri, String path){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap bitmap;
        Bitmap bmRotated;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            //회전된 사진을 보정한다
            bmRotated = rotateBitmap(bitmap, orientation);
            img_cover.setImageBitmap(bmRotated);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //회전된 사진을 보정한다
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
