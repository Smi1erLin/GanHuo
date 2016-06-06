package echohce.cn.gank.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import echohce.cn.gank.R;
import echohce.cn.gank.view.fragment.GanHuoFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private String[] titles = {"福利", "Android", "iOS", "前端"};
    private List<Fragment> fragments;
    private MyFragmentPageAdapter adapter = new MyFragmentPageAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 沉浸栏
        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorPrimaryDark));
        ButterKnife.bind(this);
        // 初始化View控件
        initView();
    }

    private void initView() {

        setSupportActionBar(toolbar);
        fab.setOnClickListener(this);

        fragments = new ArrayList<>();
        for (String title : titles) {
            fragments.add(GanHuoFragment.getInstance(title));
        }
        // TODO: 2016/6/6

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                adapter.getCurrentFragment().scrollToTop();
                break;
            default:
                break;
        }
    }


    // 获取当前的 Fragment
    // http://solo.farbox.com/blog/how-to-get-current-fragment-from-viewpager
    public abstract class MyFragmentPageAdapter extends FragmentPagerAdapter {
        GanHuoFragment currentFragment;

        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (GanHuoFragment) object;
            super.setPrimaryItem(container, position, object);
        }

        public GanHuoFragment getCurrentFragment() {
            return currentFragment;
        }
    }
}
