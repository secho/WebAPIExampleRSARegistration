package cz.flexer.webapiexample;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ActLoginGetCode extends ActionBarActivity {

    public static final String EXTRA_SETTINGS_CODE = "cz.flexer.webapiexample.actlogingetcode.code";
    private static final String REDIRECT_URI = "http://localhost";
    private static final String URL = "http://www.csast.csas.cz/widp/oauth2/auth?scope=/v1/marcus&response_type=code&redirect_uri=http://localhost&client_id=marcus_id";

    @InjectView(R.id.webview)
    WebView mWebLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login_get_code);
        ButterKnife.inject(this);
        mWebLogin = new WebView(this);
        setContentView(mWebLogin);
        mWebLogin.clearCache(true);
        mWebLogin.clearHistory();
        mWebLogin.getSettings().setJavaScriptEnabled(true);
        startLoginScreen();
    }

    protected void startLoginScreen() {
        mWebLogin.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith(REDIRECT_URI)) {
                    mWebLogin.setVisibility(View.GONE);
                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter("code");
                    Intent backIntent = new Intent();
                    backIntent.putExtra(EXTRA_SETTINGS_CODE, code);
                    setResult(RESULT_OK, backIntent);
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebLogin.loadUrl(URL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_login_get_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
