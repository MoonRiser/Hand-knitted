package com.example.hand_knitted.activity;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.hand_knitted.R;
import com.example.hand_knitted.adapter.TabFragmentAdapter;
import com.example.hand_knitted.fragment.FeedFragment;
import com.example.hand_knitted.fragment.MyWorkFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.TB2)
    public Toolbar toolbar;
    @BindView(R.id.pager)
    public ViewPager pager;
    @BindView(R.id.tab)
    public TabLayout tabs;

    private Fragment f1;
    private Fragment f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_ma, menu);
        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        ((FeedFragment) f1).refreshData();
        ((MyWorkFragment)f2).getPresenter().inqueryPost();
       // Log.i("Activity的重新打开被回调", "yes");
    }

    private void init() {

        setSupportActionBar(toolbar);
        initTabViewPager();
    }


    private void initTabViewPager() {
        final List<String> tabList = new ArrayList<>();
        final int[] materalColor = getResources().getIntArray(R.array.material_color);
        tabList.add("Feed流");
        tabList.add("本人作品");
        // tabList.add("随拍");

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
                getWindow().setStatusBarColor(evaluate);
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
         f1 = new FeedFragment();
         f2 = new MyWorkFragment();

        fragmentList.add(f1);
        fragmentList.add(f2);

        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(fragmentManager, fragmentList, tabList);
        pager.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        tabs.setupWithViewPager(pager);//将TabLayout和ViewPager关联起来。
        //tabLayout.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器

    }

}
