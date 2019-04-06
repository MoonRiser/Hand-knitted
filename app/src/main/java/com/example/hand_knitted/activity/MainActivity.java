package com.example.hand_knitted.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;

import com.example.hand_knitted.R;
import com.example.hand_knitted.adapter.TabFragmentAdapter;
import com.example.hand_knitted.fragment.FeedFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.pager)
    public ViewPager pager;
    @BindView(R.id.tab)
    public TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {

        setSupportActionBar(toolbar);
    }


    private void initTabViewPager() {
        final List<String> tabList = new ArrayList<>();
        final int[] materalColor = getResources().getIntArray(R.array.custom_color);
        tabList.add("星座故事");
        tabList.add("笔记");
        tabList.add("天气");

        //final TabLayout tabLayout = findViewById(R.id.tabs);
        //  ViewPager viewPager = findViewById(R.id.view_pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //让tab和toolbar实现颜色渐变；
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int evaluate = (Integer) evaluator.evaluate(positionOffset, materalColor[position], materalColor[(position + 1) % tabList.size()]);
                tabs.setBackgroundColor(evaluate);
                toolbar.setBackgroundColor(evaluate);
                //     statusView.setBackgroundColor(evaluate);
                getWindow().setNavigationBarColor(evaluate);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //设置tab模式，MODE_FIXED是固定的，MODE_SCROLLABLE可超出屏幕范围滚动的
        //  tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        List<Fragment> fragmentList = new ArrayList<>();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment f1 = new FeedFragment();

        fragmentList.add(f1);

        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(fragmentManager, fragmentList, tabList);
        pager.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        tabs.setupWithViewPager(pager);//将TabLayout和ViewPager关联起来。
        //tabLayout.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器

    }

}
