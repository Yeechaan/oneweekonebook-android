package com.lee.oneweekonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.ViewHolder> {
    Context context;
    ArrayList<ItemReview> items = new ArrayList<ItemReview>();
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public AdapterReview(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_review, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemReview item = items.get(position);
        holder.setItem(item);

        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //하나 추가
    public void addItem(ItemReview item){
        items.add(item);
    }
    //하나 제거
    public void subItem(int index){
        items.remove(index);
    }

    public ItemReview getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_subtitle;
        TextView txt_page;
        TextView txt_contents;
        TextView txt_review;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_subtitle = (TextView) itemView.findViewById(R.id.txt_subtitle);
            txt_page = (TextView) itemView.findViewById(R.id.txt_page);
            txt_contents = (TextView) itemView.findViewById(R.id.txt_contents);
            txt_review = (TextView) itemView.findViewById(R.id.txt_review);

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

        public void setItem(ItemReview item){
            txt_subtitle.setText(item.getSub_title());
            txt_page.setText(item.getPage());
            txt_contents.setText(item.getContents());
            txt_review.setText(item.getReview());
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

    }
}
