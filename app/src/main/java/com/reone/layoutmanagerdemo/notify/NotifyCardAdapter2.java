package com.reone.layoutmanagerdemo.notify;

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
import java.util.Locale;

/**
 * Created by wangxingsheng on 2020/6/22.
 * desc:
 */
public class NotifyCardAdapter2 extends ItemClickAdapter<ItemBean, NotifyCardAdapter2.ItemViewHolder> {

    private Context context;
    private List<ItemBean> data;

    public NotifyCardAdapter2(Context context, List<ItemBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notify_layout2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemBean item = data.get(position);
        holder.imageView.setImageResource(item.getHeaderIcon());
        holder.movieImage.setImageResource(item.getMovie());
        holder.textView.setText(String.format(Locale.CHINA, "[%d]%s", holder.getAdapterPosition(), item.getName()));
        holder.textView2.setText(item.getBreed());
        holder.textView3.setText(item.getRegistry());
        attachClick(holder.itemView, item, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView movieImage;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            movieImage = itemView.findViewById(R.id.movie);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }
}