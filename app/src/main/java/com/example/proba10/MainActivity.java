package com.example.proba10;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.WebSettings.PluginState;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private String address = "http://192.168.43.138/";
    private WebView webView;
    private Button btnScan;
    private TextView pr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//birnnchi hu metod ishlaish grak.
        setContentView(R.layout.activity_main);//keyin avval layout uqib olishi garak.
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));//toolbar quyilgan bilsa,uniyam uqibalihigarak.

        //kodni berdan davom attirgan yaxshi.
        Intent intent = getIntent();
        //avval tekshiramiz, inten ichinda "getip" borligini:
        String addr = "192.168.0.1";//getip yo'q bulsa default ip addressga ulanishga harakat qiladi.
        if (intent.hasExtra("getip")){
            addr = "http://"+intent.getStringExtra("getip");//shunda bu qator hato barmidi
            address = addr;
        }//Ishlatib guring

        btnScan = findViewById(R.id.btnScan);
        webView = findViewById(R.id.web);




        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        //webView.getSettings().setPluginState(PluginState.ON);
        //webSetting.setAllowFileAccess(true);
        webSetting.setDisplayZoomControls(true);

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.1.200"));
        //startActivity(browserIntent);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new CustomWebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(addr);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
    }

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);

        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String message = result.getContents();
                if (message.length() > 0) {
                    //pr = findViewById(R.id.profit);
                    //pr.setText(address + "/" + message);
                    webView.loadUrl(address + "/frontend/web/site/view?id=" + message);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            final Uri uri = request.getUrl();
            handleError(view, error.getErrorCode(), error.getDescription().toString(), uri);
        }

        @SuppressWarnings("deprecation")
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            final Uri uri = Uri.parse(failingUrl);
            handleError(view, errorCode, description, uri);
        }

        private void handleError(WebView view, int errorCode, String description, final Uri uri) {
            final String host = uri.getHost();// e.g. "google.com"
            final String scheme = uri.getScheme();// e.g. "https"
            // TODO: logic
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            finish();

        }
    }
}
