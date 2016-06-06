package echohce.cn.gank.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import echohce.cn.gank.R;
import echohce.cn.gank.model.GankData;
import echohce.cn.gank.util.Util;

/**
 * Created by lin on 2016/6/5.
 */
public class GankHuoViewHolder extends BaseViewHolder<GankData.Result> {
    private TextView title;
    private TextView author;
    private TextView time;

    public GankHuoViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_ganhuo);
        title = $(R.id.ganhuo_title);
        author = $(R.id.ganhuo_author);
        time = $(R.id.ganhuo_time);
    }

    @Override
    public void setData(GankData.Result data) {
        super.setData(data);
        // 干货标题
        title.setText(data.getDesc());
        // 干货时间 利用工具类格式化时间
        time.setText(Util.getFormatTime(data.getCreatedAt()));
        // 干货作者
        author.setText(data.getWho());
    }
}
