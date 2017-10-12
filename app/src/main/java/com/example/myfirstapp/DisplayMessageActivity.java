package com.example.myfirstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class DisplayMessageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private String unique_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);


        SharedPreferences prefs = getSharedPreferences("unique_id", 0);
        unique_id = prefs.getString("uuid", "");

        // Get Intent and extract string
        Intent intent = getIntent();
        String kenteken = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        myWebView.setWebViewClient(new WebViewClient());

        Log.d("UNIQUE ID LINK", "https://www.kentekencheck.nl/aff.php?x=f8d2091faf471764182a5a7f3c636c08&kenteken="+kenteken+"&ref="+unique_id);
        myWebView.loadUrl("https://www.kentekencheck.nl/aff.php?x=f8d2091faf471764182a5a7f3c636c08&kenteken="+kenteken+"&ref="+unique_id);


        bottomNavigationView.setSelectedItemId(R.id.navigation_search);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_info:
                                InfoPage();
                                break;
                            case R.id.navigation_search:
                                mainPage();
                                break;
                            case R.id.navigation_history:
                                historyPage();
                                break;
                        }
                        return true;
                    }

                });

    }

    public void InfoPage(){
        Intent intent = new Intent(this, InfoPage.class);
        startActivity(intent);
    }
    public void mainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void historyPage(){
        Intent intent = new Intent(this, HistoryPage.class);
        startActivity(intent);
    }

    public int dpToPixels(int dp){
        float density = getResources().getDisplayMetrics().density;
        return(int) (int)(dp * density);
    }
}
