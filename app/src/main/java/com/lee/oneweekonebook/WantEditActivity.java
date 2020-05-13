package com.lee.oneweekonebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WantEditActivity extends AppCompatActivity {
    ImageView img_cover;
    EditText etxt_title, etxt_writer, etxt_publisher;
    String mCurrentPhotoPath = "0";
    SQLiteDatabase database;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_edit);

        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        Intent passedIntent = this.getIntent();
        id = passedIntent.getIntExtra("id", 1);

        etxt_title = (EditText) findViewById(R.id.etxt_title);
        etxt_writer = (EditText) findViewById(R.id.etxt_writer);
        etxt_publisher = (EditText) findViewById(R.id.etxt_publisher);

        //책 커버 선택하기
        img_cover = (ImageView) findViewById(R.id.img_cover);
        img_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //팝업으로 선택한다
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupImageSelected(popupMenu);
            }
        });

        //툴바 관련
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("수정하기 PAGE");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        setInit();

        //책 수정하기
        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //want 로 이동
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setInit(){
        if(database != null){
            String sql = "select _id, title, writer, pub, pic_cover from " + "want" + " where _id=" + id;

            if(database != null) {
                Cursor cursor = database.rawQuery(sql, null);

                cursor.moveToNext();
                etxt_title.setText(cursor.getString(1));
                etxt_writer.setText(cursor.getString(2));
                etxt_publisher.setText(cursor.getString(3));
                mCurrentPhotoPath = cursor.getString(4);

                File imgFile = new  File(mCurrentPhotoPath);
                if(imgFile.exists()){
                    Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
                    setTitleImage(uri, mCurrentPhotoPath);                }
                //저장된 표지 사진이 없으면 일반사진으로 대체
                else{
                    img_cover.setImageResource(R.drawable.ic_book);
                }
            }

            //Toast.makeText(getApplicationContext(), "이야기가 수정되었습니다.", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    //책 정보 데이터베이스에 추가
    public void editData(){
        if(database != null){
            String sqlInsert = "update want set title=?, writer=?, pub=?, pic_cover=? where _id=" + id;
            Object[] params = {etxt_title.getText().toString(), etxt_writer.getText().toString(), etxt_publisher.getText().toString(), mCurrentPhotoPath};

            database.execSQL(sqlInsert, params);

            Toast.makeText(getApplicationContext(), "보고싶은 책 수정!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //이미지 팝업으로 선택
    public void popupImageSelected(PopupMenu popupMenu){
        getMenuInflater().inflate(R.menu.option_image, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.m1:
                        takePicture();
                        Toast.makeText(getApplicationContext(), "직접 찍기", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.m2:
                        pickUpPicture();
                        Toast.makeText(getApplicationContext(), "갤러리에서 선택", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    //카메라 앱을 열어 사진을 찍는다
    void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = createImageFile();
            //본인 패키지 이름 적어
            Uri photoUri = FileProvider.getUriForFile(this,"com.lee.oneweekonebook.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //직접 찍은 사진
        if(requestCode == 10){
            Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
            //사진을 뷰에 띄운다 (회전 후)
            setTitleImage(uri, mCurrentPhotoPath);
            //사진을 저장한다
            galleryAddPic();
        }
        //갤러리에서 불러온 사진
        if(requestCode == 11 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            //갤러리에서 가져온 사진 경로
            mCurrentPhotoPath = getRealPathFromURI(getApplicationContext(), uri);
            //사진을 뷰에 띄운다 (회전 후)
            setTitleImage(uri, mCurrentPhotoPath);
        }
    }

    //사진 찍고 경로 만들기
    private File createImageFile() throws IOException {
        // Create an image file namΩe
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    //갤러리에 사진 저장
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);

        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //갤러리를 열어 사진을 불러온다
    void pickUpPicture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent,11);
    }

    //uri to real file path
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("error ", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
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
        options.inSampleSize = setSimpleSize(options, 512, 512);
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
