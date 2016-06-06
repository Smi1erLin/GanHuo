package echohce.cn.gank.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jaeger.library.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import echohce.cn.gank.R;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.about_toolbar)
    Toolbar aboutToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // 沉浸栏
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark));
        ButterKnife.bind(this);
        setSupportActionBar(aboutToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
