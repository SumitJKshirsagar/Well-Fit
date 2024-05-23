package com.example.well_fit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewAdapter extends PagerAdapter {
    Context context;
    int[] slide = {R.drawable.wellfit, R.drawable.wel, R.drawable.tria};

    public ViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slide.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.screen1, container, false);
        ImageView img = view.findViewById(R.id.slideimg);
        img.setImageResource(slide[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
