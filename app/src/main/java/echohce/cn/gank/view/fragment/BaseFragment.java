package echohce.cn.gank.view.fragment;

import android.support.v4.app.Fragment;

import rx.Subscription;

/**
 * Created by lin on 2016/6/2.
 */
public abstract class BaseFragment extends Fragment {
    protected Subscription subscription;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
