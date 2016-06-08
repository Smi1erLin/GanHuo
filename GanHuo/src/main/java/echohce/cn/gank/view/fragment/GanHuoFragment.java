package echohce.cn.gank.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import echohce.cn.gank.HidingScrollListener;
import echohce.cn.gank.R;
import echohce.cn.gank.adapter.FuliAdapter;
import echohce.cn.gank.adapter.GanHuoAdapter;
import echohce.cn.gank.model.GankData;
import echohce.cn.gank.network.GankSingleTon;
import echohce.cn.gank.view.activity.FuliActivity;
import echohce.cn.gank.view.activity.GanHuoActivity;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lin on 2016/6/5.
 */
public class GanHuoFragment extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerview)
    EasyRecyclerView recyclerView;

    // fab控件
    private FloatingActionButton fab;

    List<GankData.Result> ganHuoList;
    private FuliAdapter fuLiAdapter;
    private GanHuoAdapter ganHuoAdapter;

    // Title : [福利|ANDROID|IOS|休息视频]
    private String title;

    // 常量：每次加载的数量
    private static final int DEAFULT_LOAD_COUNT = 20;

    // 记录当前页数。
    private int currentPage = 1;

    private Handler handler = new Handler();

    // RxJava Observer对象。
    private Observer<GankData> observer = new Observer<GankData>() {
        @Override
        public void onCompleted() {
            Log.e("Observer_State", "Completed");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("Observer_State", "onError");
            Snackbar.make(recyclerView, "NO NETWORK", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(GankData gankData) {
            Log.e("Observer_State", "onNext");
            ganHuoList = gankData.getResults();
            if (title.equals("福利")) {
                fuLiAdapter.addAll(ganHuoList);
            } else {
                ganHuoAdapter.addAll(ganHuoList);
            }
        }
    };

    private void getGanHuo(String type, int count, int page) {
        subscription = GankSingleTon.getGankService()
                .getGankData(type, count, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment, container, false);
        ButterKnife.bind(this, view);
        // 初始化操作
        init(view);
        return view;
    }

    private void init(View view) {

        // 获取Activity的Fab对象
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // 根据title判断对应的布局，和设置适配器的样式
        if (title.equals("福利")) {
            // 瀑布流
            StaggeredGridLayoutManager staggeredGridLayoutManager
                    = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            fuLiAdapter = new FuliAdapter(getContext());
            dealWithAdapter(fuLiAdapter);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ganHuoAdapter = new GanHuoAdapter(getContext());
            dealWithAdapter(ganHuoAdapter);
        }

        // 设置Scroll监听器
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onShow() {
                showViews();
            }

            @Override
            public void onHide() {
                hideViews();
            }
        });
        // 设置Refresh监听器
        recyclerView.setRefreshListener(this);
        // 刷新界面
        onRefresh();
    }

    private void hideViews() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        fab.animate().translationY(fab.getHeight() + fabBottomMargin).setInterpolator(new LinearInterpolator()).start();
    }

    private void showViews() {
        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
    }

    // EasyRecyclerView 简单的设置
    private void dealWithAdapter(final RecyclerArrayAdapter adapter) {
        recyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.layout_load_more, this);
        adapter.setNoMore(R.layout.layout_no_more);
        adapter.setError(R.layout.layout_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (title.equals("福利")) startOtherActivity(adapter, position, FuliActivity.class);
                else startOtherActivity(adapter, position, GanHuoActivity.class);
            }
        });
    }


    private void startOtherActivity(RecyclerArrayAdapter<GankData.Result> adapter, int position, Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        intent.putExtra("desc", adapter.getItem(position).getDesc());
        intent.putExtra("url", adapter.getItem(position).getUrl());
        startActivity(intent);
    }

    /**
     * 上拉更多功能
     * 处理 LoadMore 逻辑，需要实现 RecyclerArrayAdapter.OnLoadMoreListener 接口
     * 具体实现是请求下一页的数据。
     */
    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (title.equals("福利")) {
                    getGanHuo("福利", DEAFULT_LOAD_COUNT, currentPage);
                } else if (title.equals("Android")) {
                    getGanHuo("Android", DEAFULT_LOAD_COUNT, currentPage);
                } else if (title.equals("iOS")) {
                    getGanHuo("iOS", DEAFULT_LOAD_COUNT, currentPage);
                } else if (title.equals("前端")) {
                    getGanHuo("前端", DEAFULT_LOAD_COUNT, currentPage);
                }
                currentPage++;
            }
        }, 1000);
    }


    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (title.equals("福利")) {
                    fuLiAdapter.clear();
                    getGanHuo("福利", DEAFULT_LOAD_COUNT, 1);
                } else if (title.equals("Android")) {
                    ganHuoAdapter.clear();
                    getGanHuo("Android", DEAFULT_LOAD_COUNT, 1);
                } else if (title.equals("iOS")) {
                    ganHuoAdapter.clear();
                    getGanHuo("iOS", DEAFULT_LOAD_COUNT, 1);
                } else if (title.equals("前端")) {
                    ganHuoAdapter.clear();
                    getGanHuo("前端", DEAFULT_LOAD_COUNT, 1);
                }
                currentPage = 2;
            }
        }, 1000);
    }

    // 返回一个带title的Fragment实例
    public static Fragment getInstance(String title) {
        GanHuoFragment fragment = new GanHuoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     *  滑动到顶端，没有动画，很僵硬
     */
    public void scrollToTop() {
        recyclerView.scrollToPosition(0);
    }

}
