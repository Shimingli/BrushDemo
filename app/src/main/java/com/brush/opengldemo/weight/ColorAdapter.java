package com.brush.opengldemo.weight;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.brush.opengldemo.R;

import java.util.ArrayList;
import java.util.List;



public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private List<ColorBean> paths;
    private final Context mContext1;
    private ArrayList<ColorBean> mNewData;
    private boolean isAnimation=true;
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
        final ViewHolder viewHolder = (ViewHolder) holder;
        final ColorBean colorBean = paths.get(position);
        int argb = Color.argb(colorBean.alpha, colorBean.red, colorBean.green, colorBean.blue);
        System.out.println("shiming  argb===="+argb);
        viewHolder.mImageView.setColorFilter(argb);
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnimation){
                    onItemClickListener.onClick(v,colorBean);
                    AnimationSet animationSet = new AnimationSet(true);
            /*
                参数解释：
                    第一个参数：X轴水平缩放起始位置的大小（fromX）。1代表正常大小
                    第二个参数：X轴水平缩放完了之后（toX）的大小，0代表完全消失了
                    第三个参数：Y轴垂直缩放起始时的大小（fromY）
                    第四个参数：Y轴垂直缩放结束后的大小（toY）
                    第五个参数：pivotXType为动画在X轴相对于物件位置类型
                    第六个参数：pivotXValue为动画相对于物件的X坐标的开始位置
                    第七个参数：pivotXType为动画在Y轴相对于物件位置类型
                    第八个参数：pivotYValue为动画相对于物件的Y坐标的开始位置

                   （第五个参数，第六个参数），（第七个参数,第八个参数）是用来指定缩放的中心点
                    0.5f代表从中心缩放
             */


                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,1.5f,1,1.5f,
                            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    //3秒完成动画
                    scaleAnimation.setDuration(1000);
                    //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
                    animationSet.addAnimation(scaleAnimation);
                    animationSet.setFillAfter(true);
                    animationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation=false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //启动动画
                    viewHolder.mImageView.startAnimation(animationSet);
                }else {
                    AnimationSet animationSet = new AnimationSet(true);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,1f,1,1f,
                            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    //3秒完成动画
                    scaleAnimation.setDuration(1000);
                    //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
                    animationSet.addAnimation(scaleAnimation);
                    animationSet.setFillAfter(true);
                    animationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimation=true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //启动动画
                    viewHolder.mImageView.startAnimation(animationSet);
                }


            }
        });
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
        void onClick(View v, ColorBean  position);
    }
}
