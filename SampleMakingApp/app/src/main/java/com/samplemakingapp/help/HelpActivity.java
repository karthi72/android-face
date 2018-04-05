package com.samplemakingapp.help;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.samplemakingapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    public static ArrayList<String> mEcodedImagesLsit = new ArrayList<>();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.image_a,R.drawable.image_b,R.drawable.image_c,R.drawable.image_d};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    ArrayList<String> heading;
    ArrayList<String> text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);


        heading=new ArrayList<>();
        heading.add(getString(R.string.enroll_me));
        heading.add(getString(R.string.enroll_me));
        heading.add(getString(R.string.search_by_camera));
        heading.add(getString(R.string.search_by_gallery));

        text=new ArrayList<>();
        text.add(getString(R.string.enrollment_text));
        text.add(getString(R.string.enroll_text_new));
        text.add(getString(R.string.carema_text));
        text.add(getString(R.string.gallery_text));


        viewpage();

    }


    public void viewpage(){

        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(new SlidingImageAdapter(this,ImagesArray,heading,text));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(pager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

    }
    @OnClick({R.id.pager, R.id.indicator})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pager:
                break;
            case R.id.indicator:
                break;
        }
    }
}
