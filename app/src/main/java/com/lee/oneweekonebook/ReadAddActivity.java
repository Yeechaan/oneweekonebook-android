package com.lee.oneweekonebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReadAddActivity extends AppCompatActivity {
    ImageView img_cover;
    EditText etxt_title, etxt_writer, etxt_publisher;
    TextView txt_st_date;
    String st_date;
    Calendar cal;
    int y=0, m=0, d=0;

    String mCurrentPhotoPath = "0";
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_add);

        //사진 권한 받아오기
        requirePermission();

        etxt_title = (EditText) findViewById(R.id.etxt_title);
        etxt_writer = (EditText) findViewById(R.id.etxt_writer);
        etxt_publisher = (EditText) findViewById(R.id.etxt_publisher);
        txt_st_date = (TextView) findViewById(R.id.txt_st_date);

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
        mTitle.setText("책 읽기 시작");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        st_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //달력에서 날짜 선택하기
        Button btn_st_date = (Button) findViewById(R.id.btn_st_date);
        btn_st_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        //책 추가하기
        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //read 으로 이동
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //책 정보 데이터베이스에 추가
    public void insertData(){
        database = openOrCreateDatabase("book.db", MODE_PRIVATE, null);

        if(database != null){
            String sqlInsert = "insert into read(title, writer, pub, pic_cover, st_date) values(?, ?, ?, ?, ?)";
            Object[] params = {etxt_title.getText().toString(), etxt_writer.getText().toString(), etxt_publisher.getText().toString(), mCurrentPhotoPath, st_date};

            database.execSQL(sqlInsert, params);

            //read 이동
            Toast.makeText(getApplicationContext(), "새로운 책이 등록되었습니다", Toast.LENGTH_SHORT).show();

            //메인으로
            finish();
//            Intent intent_read = new Intent(getApplicationContext(), ReadActivity.class);
//            //중간 엑티비티 지우고 기존 엑티비티 재사용
//            intent_read.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent_read);
        }
    }

    void showDate() {
        //달력 뷰 띄워
        cal = Calendar.getInstance();
        //달력안에서 날짜 선택하면
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

                String day = Integer.toString(d);
                if(d<10){
                    day = "0" + day;
                }

                st_date = Integer.toString(y) + "-" + Integer.toString(m) + "-" + day;
                txt_st_date.setText(st_date);
                Toast.makeText(getApplicationContext(), "selected data : " + st_date, Toast.LENGTH_SHORT).show();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        datePickerDialog.setMessage("독서시작 날짜 선택하기");
        //오늘 날짜 이후 클릭 안되게 하기
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
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
