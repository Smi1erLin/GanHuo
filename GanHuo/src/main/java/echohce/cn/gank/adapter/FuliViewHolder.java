package echohce.cn.gank.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import echohce.cn.gank.R;
import echohce.cn.gank.model.GankData;

/**
 * Created by lin on 2016/6/5.
 */
public class FuliViewHolder extends BaseViewHolder<GankData.Result> {
    public ImageView mImageView;

    public FuliViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_fuli);
        mImageView = $(R.id.fuli_img);
    }

    @Override
    public void setData(GankData.Result data) {
        super.setData(data);
        Glide.with(getContext())
                .load(data.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);
    }
}
