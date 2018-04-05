package com.samplemakingapp.help;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.samplemakingapp.R;

import java.util.ArrayList;

/**
 * Created by applr on 23/03/18.
 */

public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<String> header;
    ArrayList<String> text;


    public SlidingImageAdapter(Context context,ArrayList<Integer> IMAGES,ArrayList<String> header,ArrayList<String> text) {
        this.context = context;
        this.IMAGES=IMAGES;
        this.header=header;
        this.text=text;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        TextView tvHeader=(TextView) imageLayout.findViewById(R.id.tvHeader);
        TextView tvtext=(TextView) imageLayout.findViewById(R.id.tvtext);

        tvHeader.setText(header.get(position));
        tvtext.setText(text.get(position));

        imageView.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}

