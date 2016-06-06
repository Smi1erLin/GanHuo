package echohce.cn.gank.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import echohce.cn.gank.model.GankData;

/**
 * Created by lin on 2016/6/5.
 */
public class FuliAdapter extends RecyclerArrayAdapter<GankData.Result> {

    public FuliAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FuliViewHolder(parent);
    }
}
