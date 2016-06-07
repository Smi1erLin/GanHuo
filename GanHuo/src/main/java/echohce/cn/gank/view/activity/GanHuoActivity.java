package echohce.cn.gank.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import echohce.cn.gank.R;
import echohce.cn.gank.util.Util;

public class GanHuoActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private long exitTime = 0;

    private String desc;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gan_huo);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        Intent intent = getIntent();
        desc = intent.getStringExtra("desc");
        url = intent.getStringExtra("url");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(desc);

        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else progressBar.setProgress(newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ganhuo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                webView.reload();
                progressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.action_share:
                Util.shareText(this, String.format("「%s」- %s ", desc, url));
                break;
            case R.id.action_loading:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                final Snackbar s = Snackbar.make(webView, "再按一次返回上层界面", Snackbar.LENGTH_SHORT);
                s.setAction("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s.dismiss();
                    }
                }).show();
                exitTime = System.currentTimeMillis();
            } else finish();
        }
    }

}
