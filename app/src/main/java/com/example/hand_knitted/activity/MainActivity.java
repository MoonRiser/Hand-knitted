package com.example.hand_knitted.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ddz.floatingactionbutton.FloatingActionButton;
import com.ddz.floatingactionbutton.FloatingActionMenu;
import com.example.hand_knitted.R;
import com.example.hand_knitted.adapter.TabFragmentAdapter;
import com.example.hand_knitted.fragment.FeedFragment;
import com.example.hand_knitted.fragment.MyWorkFragment;
import com.example.hand_knitted.util.MyUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.TB2)
    public Toolbar toolbar;
    @BindView(R.id.pager)
    public ViewPager pager;
    @BindView(R.id.tab)
    public TabLayout tabs;
    @BindView(R.id.FAB)
    public FloatingActionMenu fab;
    @BindView(R.id.FABwork)
    public FloatingActionButton fabwork;
    @BindView(R.id.FABsnap)
    public com.ddz.floatingactionbutton.FloatingActionButton fabsnap;

    private Fragment f1;
    private Fragment f2;
    private Boolean isDarkMode = false;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkMode = sp.getBoolean("pref_theme_dark", false);
        fabSiwth(false);
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_ma, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.darkMode:
                darkModeSwith();
                break;
            case R.id.about:
                showToastLong("copyright reserved,balabala");
                break;
            case R.id.setting:
                showToastLong("占位待实现");
                break;
            case R.id.logout:
                BmobUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyUtils.POST_EDIT) {
            try {
                Thread.sleep(700);//线程沉睡否则容易出现同步问题
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((FeedFragment) f1).refreshData();
            ((MyWorkFragment) f2).reFreshData();
        }
    }

    private void init() {

        setSupportActionBar(toolbar);
        fab.setButtonIcon(R.drawable.ic_cached_black_24dp);
        initTabViewPager();
        // fabwork.setOnClickListener(this);
    }


    private void initTabViewPager() {
        final List<String> tabList = new ArrayList<>();
        final int[] materalColor = getResources().getIntArray(R.array.material_color);
        tabList.add("全部作品");
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
                //  Log.i("当前显示的碎片的位置：",position+"");
                switch (position) {
                    case 0:
                        fabSiwth(false);
                        fab.setButtonIcon(R.drawable.ic_cached_black_24dp);
                        break;
                    case 1:
                        fabSiwth(true);
                        fab.setButtonIcon(R.drawable.ic_add_black_24dp);
                        break;
                }
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


    private void darkModeSwith() {

        SharedPreferences.Editor editor = sp.edit();
        if (!isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("pref_theme_dark", true);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("pref_theme_dark", false);
        }
        editor.apply();
        finish();
        startActivity(new Intent(this, this.getClass()));
    }


    public void fabSiwth(Boolean show) {
        if (fabwork == null || fabsnap == null)
            return;
        if (show) {
            fabwork.setOnClickListener(this);
            fabsnap.setOnClickListener(this);
        } else {
            fabwork.setOnClickListener(v -> {

                ((FeedFragment)f1).setSnap(false);
                ((MyWorkFragment)f2).setSnap(false);
                ((FeedFragment) f1).refreshData();
                ((MyWorkFragment) f2).reFreshData();
                fab.collapse();
            });
            fabsnap.setOnClickListener(v -> {
                ((FeedFragment)f1).setSnap(true);
                ((MyWorkFragment)f2).setSnap(true);
                ((FeedFragment) f1).refreshData();
                ((MyWorkFragment) f2).reFreshData();
                fab.collapse();
            });
        }

    }


    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {

            case R.id.FABwork:
                intent = new Intent(this, EditPostActivity.class);
                intent.putExtra("isADD", true);//发表新帖子和更新旧帖子用同一个EditPostActivity，所以用这个标志区分一下
                break;
            default:
                intent = new Intent(this, EditSnapActivity.class);
                intent.putExtra("isADD", true);
                break;

        }
        startActivityForResult(intent, MyUtils.POST_EDIT);

    }
}
