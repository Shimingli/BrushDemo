package com.brush.opengldemo.weight;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brush.opengldemo.R;

import java.util.ArrayList;
import java.util.List;



public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private List<ColorBean> paths;
    private final Context mContext1;
    private ArrayList<ColorBean> mNewData;

    public ColorAdapter(List<ColorBean> path, Context context) {
        this.paths = new ArrayList<>();
        mContext1 = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder myViewHolder = new ViewHolder(LayoutInflater.from(mContext1).inflate(R.layout.item_layout, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ColorBean colorBean = paths.get(position);
        int argb = Color.argb(colorBean.alpha, colorBean.red, colorBean.green, colorBean.blue);
        viewHolder.mImageView.setColorFilter(argb);

    }


    @Override
    public int getItemCount() {
        return paths.size();
    }

    public void setNewData(ArrayList<ColorBean> newData) {
        paths.clear();
        paths.addAll(newData);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.colorCircle);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }
}
