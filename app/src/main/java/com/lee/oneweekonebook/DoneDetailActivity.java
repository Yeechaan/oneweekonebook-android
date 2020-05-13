package com.lee.oneweekonebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class DoneDetailActivity extends AppCompatActivity {
    ImageView img_cover;
    TextView txt_title, txt_writer, txt_pub, txt_st_date, txt_fi_date, txt_fi_memo, txt_fi_review;
    SQLiteDatabase database;

    int idSelected;
    String mCurrentPhotoPath;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_detail);

        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_writer = (TextView) findViewById(R.id.txt_writer);
        txt_pub = (TextView) findViewById(R.id.txt_pub);
        txt_st_date = (TextView) findViewById(R.id.txt_st_date);
        txt_fi_date = (TextView) findViewById(R.id.txt_fi_date);
        txt_fi_memo = (TextView) findViewById(R.id.txt_fi_memo);
        txt_fi_review = (TextView) findViewById(R.id.txt_fi_review);
        img_cover = (ImageView) findViewById(R.id.img_cover);

        Intent passedIntent = this.getIntent();
        idSelected = passedIntent.getIntExtra("id", 1);

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("다 읽은 책");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //책 수정하기
        Button btn_read_save = (Button) findViewById(R.id.btn_read_save);
        btn_read_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editData();
            }
        });

        //책 삭제하기
        Button btn_read_delete = (Button) findViewById(R.id.btn_read_delete);
        btn_read_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        setInit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            setInit();
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

    public void deleteData(){
        if(database != null){
            String sqlDelete = "delete from read where _id=" + idSelected;
            database.execSQL(sqlDelete);

            Toast.makeText(getApplicationContext(), "독서 완료 목록에서 삭제", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void editData(){
        if(database != null){
            Intent intent_done_detail_edit = new Intent(getApplicationContext(), DoneDetailEditActivity.class);
            intent_done_detail_edit.putExtra("id", idSelected);

            startActivityForResult(intent_done_detail_edit, 101);
        }
    }

    public void setInit(){
        if(database != null){
            String sql = "select title, writer, pub, pic_cover, st_date, fi_date, fi_memo, fi_review from " + "read" + " where _id=" + idSelected;

            if(database != null) {
                Cursor cursor = database.rawQuery(sql, null);

                cursor.moveToNext();
                txt_title.setText(cursor.getString(0));
                txt_writer.setText(cursor.getString(1));
                txt_pub.setText(cursor.getString(2));
                mCurrentPhotoPath = cursor.getString(3);
                txt_st_date.setText(cursor.getString(4));
                txt_fi_date.setText(cursor.getString(5));
                txt_fi_memo.setText(cursor.getString(6));
                txt_fi_review.setText(cursor.getString(7));

                File imgFile = new  File(mCurrentPhotoPath);
                if(imgFile.exists()){
                    Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
                    setTitleImage(uri, mCurrentPhotoPath);                }
                //저장된 표지 사진이 없으면 일반사진으로 대체
                else{
                    img_cover.setImageResource(R.drawable.ic_book);
                }
                //title bar 설정하기
                //mTitle.setText(cursor.getString(0));
            }
        }
    }

    // 이미지 Resize 함수
    private int setSimpleSize(BitmapFactory.Options options, int requestWidth, int requestHeight){
        // 이미지 사이즈를 체크할 원본 이미지 가로/세로 사이즈를 임시 변수에 대입.
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        // 원본 이미지 비율인 1로 초기화
        int size = 1;

        // 해상도가 깨지지 않을만한 요구되는 사이즈까지 2의 배수의 값으로 원본 이미지를 나눈다.
        while(requestWidth < originalWidth || requestHeight < originalHeight){
            originalWidth = originalWidth / 2;
            originalHeight = originalHeight / 2;

            size = size * 2;
        }
        return size;
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

        File imgFile = new  File(path);
        //사진 용량 줄이기
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
        options.inSampleSize = setSimpleSize(options, 256, 256);
        options.inJustDecodeBounds = false;

        Bitmap bitmap;
        Bitmap bmRotated;
        try {
            //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
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
