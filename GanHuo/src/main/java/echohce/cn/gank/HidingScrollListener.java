package echohce.cn.gank;

import android.support.v7.widget.RecyclerView;

/**
 * Created by lin on 2016/6/4.
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 20;
    private boolean controlsVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }
        if ((controlsVisible && dy > 0) | (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }
    public abstract void onShow();
    public abstract void onHide();
}
