package com.lee.oneweekonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder> {
    Context context;
    ArrayList<ItemSearch> items = new ArrayList<ItemSearch>();
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public AdapterSearch(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSearch.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_search, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearch.ViewHolder holder, int position) {
        ItemSearch item = items.get(position);
        holder.setItem(item);

        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //하나 추가
    public void addItem(ItemSearch item){
        items.add(item);
    }
    //하나 제거
    public void subItem(int index){
        items.remove(index);
    }

    public ItemSearch getItem(int position){
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
        TextView txt_index;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_writer = (TextView) itemView.findViewById(R.id.txt_writer);
            txt_pub = (TextView) itemView.findViewById(R.id.txt_pub);
            img_picture = (ImageView) itemView.findViewById(R.id.img_picture);
            txt_st_date = (TextView) itemView.findViewById(R.id.txt_st_date);
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

        public void setItem(ItemSearch item){
            Glide.with(itemView).load(item.getPic_cover()).into(img_picture);
            txt_title.setText(item.getTitle());
            txt_writer.setText(item.getWriter());
            txt_pub.setText(item.getPub());
            txt_st_date.setText(item.getSt_date());
            txt_index.setText(Integer.toString(item.getIndex()));
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }


    }
}
