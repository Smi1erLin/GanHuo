package echohce.cn.gank.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

import echohce.cn.gank.R;
import rx.Observable;
import rx.functions.Action1;

public class SplashActivity extends AppCompatActivity {
    //Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // 定时跳转用到了timer操作符
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                SplashActivity.this.finish();
            }
        });
        // 定时跳转
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                // Activity切换效果，必须放在startActivity之后。  -- Android开发艺术探索
//                overridePendingTransition(0, 0);
//                SplashActivity.this.finish();
//            }
//        }, 1500); // 1500ms后进行跳转
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("SplashActivity_State","onDestroy");
    }
}
