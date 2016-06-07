package echohce.cn.gank.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.Bind;
import butterknife.ButterKnife;
import echohce.cn.gank.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class FuliActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fuli_image)
    ImageView fuliImage;

    private PhotoViewAttacher mAttacher;
    private Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli);
        ButterKnife.bind(this);
        // Windows快捷键 ctrl + alt + m 可以快速对代码进行封装哦！
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");
        String url = intent.getStringExtra("url");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(desc);

        mAttacher = new PhotoViewAttacher(fuliImage);
        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        fuliImage.setImageBitmap(resource);
                        mBitmap = resource;
                        mAttacher.update();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fuli,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_save:break;
            case R.id.action_share:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
