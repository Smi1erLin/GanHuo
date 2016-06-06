package echohce.cn.gank.view.fragment;

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
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lin on 2016/6/5.
 */
public class GanHuoFragment extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
    List<GankData.Result> ganHuoList;

    @Bind(R.id.recyclerview)
    EasyRecyclerView recyclerView;

    private String title;
    private FloatingActionButton fab;
    private static final int DEAFULT_LOAD_COUNT = 20;
    private int currentPage = 1;
    private Handler handler = new Handler();

    private FuliAdapter fuLiAdapter;
    private GanHuoAdapter ganHuoAdapter;
    private Observer<GankData> observer = new Observer<GankData>() {
        @Override
        public void onCompleted() {
            Log.e("Observer_State", "Completed");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("Observer_State", "onError");
            Snackbar.make(recyclerView, "NO NETWORK", Snackbar.LENGTH_LONG).show();
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
        init(view);
        return view;
    }

    private void init(View view) {

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        // recyclerView = (EasyRecyclerView) view.findViewById(R.id.recyclerview);

        if (title.equals("福利")) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            fuLiAdapter = new FuliAdapter(getContext());
            dealWithAdapter(fuLiAdapter);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ganHuoAdapter = new GanHuoAdapter(getContext());
            dealWithAdapter(ganHuoAdapter);
        }
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

    private void dealWithAdapter(RecyclerArrayAdapter adapter) {
        recyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.layout_load_more, this);
        adapter.setNoMore(R.layout.layout_no_more);
        adapter.setError(R.layout.layout_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO: 2016/6/5
            }
        });
    }

    // 处理 LoadMore 逻辑，需要实现 RecyclerArrayAdapter.OnLoadMoreListener 接口
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

    private void getGanHuo(String type, int count, int page) {
        subscription = GankSingleTon.getGankService()
                .getGankData(type, count, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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

    public void scrollToTop(){
        recyclerView.scrollToPosition(0);
    }


}
