package th.or.dga.royaljitarsa.utils;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebViewClientImpl extends WebViewClient {

    private Activity activity = null;
    private UrlCache urlCache = null;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
        this.urlCache = new UrlCache(activity);

        this.urlCache.register("http://tutorials.jenkov.com/", "tutorials-jenkov-com.html",
                "text/html", "UTF-8", 5 * UrlCache.ONE_MINUTE);

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if(url.startsWith("http://tutorials.jenkov.com/images/logo.png")){
            String mimeType = "image/png";
            String encoding = "";
            URL urlObj = null;
            InputStream input = null;
            try {
                urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.getInputStream();
                input = urlConnection.getInputStream();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            WebResourceResponse response = new WebResourceResponse(mimeType, encoding, input);

            return response;
        }

        return this.urlCache.load(url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("AKOTVHome", "URL: " + url);

        if("http://tutorials.jenkov.com/".equals(url)){
            this.urlCache.load("http://tutorials.jenkov.com/java/index.html");
        }
    }
}
