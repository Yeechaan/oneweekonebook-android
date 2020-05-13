package com.lee.oneweekonebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDone extends RecyclerView.Adapter<AdapterDone.ViewHolder>{
    Context context;
    ArrayList<ItemDone> items = new ArrayList<ItemDone>();
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public AdapterDone(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDone.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_done, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDone.ViewHolder holder, int position) {
        ItemDone item = items.get(position);
        holder.setItem(item);

        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //하나 추가
    public void addItem(ItemDone item){
        items.add(item);
    }
    //하나 제거
    public void subItem(int index){
        items.remove(index);
    }

    public ItemDone getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_title;
        TextView txt_writer;
        TextView txt_pub;
        ImageView img_picture;
        TextView txt_st_date;
        TextView txt_fi_date;
        TextView txt_index;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_writer = (TextView) itemView.findViewById(R.id.txt_writer);
            txt_pub = (TextView) itemView.findViewById(R.id.txt_pub);
            img_picture = (ImageView) itemView.findViewById(R.id.img_picture);
            txt_st_date = (TextView) itemView.findViewById(R.id.txt_st_date);
            txt_fi_date = (TextView) itemView.findViewById(R.id.txt_fi_date);
            txt_index = (TextView) itemView.findViewById(R.id.txt_index);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(ItemDone item){

            File imgFile = new  File(item.getPic_cover());

            if(imgFile.exists()){
                setTitleImage(item.getPic_cover());
            }
            //저장된 표지 사진이 없으면 일반사진으로 대체
            else{
                img_picture.setImageResource(R.drawable.ic_book);
            }

            txt_title.setText(item.getTitle());
            txt_writer.setText(item.getWriter());
            txt_pub.setText(item.getPub());
            txt_st_date.setText(item.getSt_date());
            txt_fi_date.setText(item.getFi_date());
            txt_index.setText(Integer.toString(item.getIndex()));
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
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
        public void setTitleImage(String path){
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
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                //회전된 사진을 보정한다
                bmRotated = rotateBitmap(bitmap, orientation);
                img_picture.setImageBitmap(bmRotated);
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
}
