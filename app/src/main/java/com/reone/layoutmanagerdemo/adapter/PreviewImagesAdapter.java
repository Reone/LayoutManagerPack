package com.reone.layoutmanagerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reone.layoutmanagerdemo.R;
import com.reone.layoutmanagerdemo.bean.ItemBean;
import com.reone.layoutmanagerdemo.utils.ItemClickAdapter;

import java.util.List;

public class PreviewImagesAdapter extends ItemClickAdapter<ItemBean, PreviewImagesAdapter.ImagesViewHolder> {
    private Context context;
    private List<ItemBean> data;

    public PreviewImagesAdapter(List<ItemBean> data, Context context) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_pre_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagesViewHolder holder, int position) {
        ItemBean item = data.get(position);
        holder.tvDesc.setText(item.getRegistry());
        holder.ivImage.setImageResource(item.getMovie());
        attachClick(holder.itemView, item, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ImagesViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDesc;
        private ImageView ivImage;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
