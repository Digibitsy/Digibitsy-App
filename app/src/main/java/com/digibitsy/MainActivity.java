package com.digibitsy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.File;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private WebView webview;
    ProgressBar progressBar;
    ProgressBar progressBar2;
    AlertDialog alertDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    String appurl = "https://digibitsy.com/dashboard";

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    public ValueCallback<Uri[]> uploadMessage;
    private int LOCATION_PERMISSION_CODE = 1;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decor = getWindow().getDecorView();
        //decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        webview = (WebView) findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);

        //webview.setWebChromeClient(new CustomChrome());

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        //webSettings.setAppCacheEnabled(true);

        //webSettings.setBuiltInZoomControls(true);

        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webSettings.setJavaScriptEnabled(true);
//        webview.getSettings().setUserAgentString(webview.getSettings().getUserAgentString());
//        webview.getSettings().setUserAgentString(webview.getSettings().getUserAgentString().replace("; wv", ""));


//        String userAG = "Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
////        userAG = "Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/537.36 " +
////                "(KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
//
//        userAG = "Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/537.36 " +
//                "(KHTML, like Gecko) Chrome/91.0. 4472.164 Safari/537.36";
//        webview.getSettings().setUserAgentString("Chrome/91.0. 4472.164");

        // for mute or unmute sound
        webSettings.setMediaPlaybackRequiresUserGesture(false);


        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                request.grant(request.getResources());

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
//                if (uploadMessage != null) {
//                    uploadMessage.onReceiveValue(null);
//                    uploadMessage = null;
//                }
//
//                uploadMessage = filePathCallback;
//
//                Intent intent = fileChooserParams.createIntent();
                try {
                    // startActivityForResult(intent, 100);
                    uploadMessage = filePathCallback;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    startActivityForResult(Intent.createChooser(i, "File Chooser"), 100);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

        });




//        if ( Build.VERSION.SDK_INT >= 23){
//            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
//                    PackageManager.PERMISSION_GRANTED  ){
////                requestPermissions(new String[]{
////                                android.Manifest.permission.ACCESS_FINE_LOCATION},
////                        LOCATION_PERMISSION_CODE);
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
//
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
//
//
//
//                //return ;
//            } else {
//                if (canGetLocation()) {
//                    //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
//                } else {
//                    //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
//                    Intent intent = new Intent(
//                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                }
//            }
//        }


        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED  ){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

        }

        String urlData = getIntent().getStringExtra("url");

        if (urlData != null && !urlData.isEmpty() && !urlData.equals("")) {
            appurl = urlData;
        }

        try {
            trimCache(MainActivity.this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        if (isConnected()) {
                                            //progressBar.setVisibility(View.VISIBLE);
                                            loadWeburl(appurl);

                                        } else {
                                            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                            noInternetAlertDialog();
                                        }
                                    }
                                }
        );

//        if (isConnected()){
//            progressBar.setVisibility(View.VISIBLE);
//            //resize();
//            loadWeburl(appurl);
//
//        } else {
//            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
//            noInternetAlertDialog();
//        }

        requestNotificationPermission();

    }


    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},111);
        }

    }

    @Override
    public void onRefresh() {
        if (isConnected()) {
            //progressBar.setVisibility(View.VISIBLE);
            appurl = webview.getUrl();
            loadWeburl(appurl);
        } else {
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            noInternetAlertDialog();
        }
    }

    //////////////////////////////////// check internet connection ////////////////////////////////////
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    //////////////////////////////////// load Web url ////////////////////////////////////
    private void loadWeburl(String url) {

        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.i("TAG", "Processing webview url click...");
                progressBar.setVisibility(View.VISIBLE);
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                // view.loadUrl(url);

                if (url != null && url.startsWith("whatsapp://")) {
                    progressBar.setVisibility(View.GONE);
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else if (url.contains("tel:")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.contains("tel:")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }  else if ((url != null && url.contains("telegram")) || (url != null && url.contains("https://t.me")) ) {
                    try { //https://t.me/+6AAd5oCJXSAwNmU1
                        progressBar.setVisibility(View.GONE);
                        Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                        telegramIntent.setData(Uri.parse(url));
                        startActivity(telegramIntent);
                        return true;
                    } catch (android.content.ActivityNotFoundException anfe) {
                        view.loadUrl(url);
                        appurl = url;
                    }
                }  else {
                    view.loadUrl(url);
                    appurl = url;
                }


                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("TAG", "Finished loading URL: " + url);
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                webview.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");

                progressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("TAG", "Error: " + description);
                //Toast.makeText(Main.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                if (!isConnected()) {
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    noInternetAlertDialog();

                }

            }
        });
        webview.loadUrl(url);

    }


    //////////////////////////////////// View no Internet AlertDialog ////////////////////////////////////
    private void noInternetAlertDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this).setCancelable(true);
        dialogBuilder.setView(R.layout.nointernet_view);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Button tryAgainBtn = alertDialog.findViewById(R.id.tryagainBtn);
        progressBar2 = alertDialog.findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isConnected()) {

                            progressBar2.setVisibility(View.GONE);
                            alertDialog.dismiss();
                            progressBar.setVisibility(View.VISIBLE);
                            loadWeburl(appurl);
                        } else {
                            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                            alertDialog.dismiss();
                            noInternetAlertDialog();
                        }
                    }
                }, 10000);


            }
        });


        // Creating Dynamic
        Rect displayRectangle = new Rect();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 1f);
        int heightLcl = (int) (displayMetrics.heightPixels * 1f);

        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);    //(displayRectangle.width() * 0.9f
        alertDialog.getWindow().setLayout((int) (widthLcl), heightLcl);  //alertDialog.getWindow().getAttributes().height

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == 100) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;

            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webview.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webview.restoreState(savedInstanceState);
    }

    private class CustomChrome extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;

        //protected FrameLayout mFullscreenContainer;
        //private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        CustomChrome() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        @SuppressLint("SourceLockedOrientationActivity")
        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        @SuppressLint("SourceLockedOrientationActivity")
        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }

            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  //SCREEN_ORIENTATION_LANDSCAPE
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            //this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                if (canGetLocation()) {
                    //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
                } else {
                    //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean canGetLocation() {
        boolean result = true;
        LocationManager lm;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            networkEnabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return gpsEnabled && networkEnabled;
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


}
