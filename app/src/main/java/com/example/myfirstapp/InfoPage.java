package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by killm_000 on 8/18/2017.
 */

public class InfoPage extends AppCompatActivity {
    TextView textView1;
    TextView txtView;
    ScrollView scrollView;
    BottomNavigationView bottomNavigationView;

    /** Informatie pagina text
     * \r\n\ om een regel over te slaan
     * om het overzichtelijk te houden wordt string + string + string... aangehouden
     * dit hoeft niet persee.
     */
    String informatie_pagina_text =
            "Hier komt een hele lange tekst met allemaal mogelijkheden over de app. " +
                    ", heel veel tekst over de app en mogelijkheden om meer te weten te komen over een voertuig" +
                    ", kentekens en controle op schades die je online kan inchecken via de app." +
                    "Kenteken app sinds 2016." + "Nog meer informatie over dit product" +
                    "\n\r \n\r- Schademeldingen \n\r- KM-stand controle \n\r- Eigenarenrapport" +
                    "\n\r \n\rveel tekst over de app en mogelijkheden om meer te weten te komen over een voertuig" +
                    ", kentekens en controle op schades die je online kan inchecken via de app." +
                    "Kenteken app sinds 2016." + "Nog meer informatie over dit product" + " veel tekst over de app en mogelijkheden om" +
                    "\n\r \n\rmeer te weten te komen over een voertuig" +
                    ", kentekens en controle op schades die je online kan inchecken via de app." +
                    "Kenteken app sinds 2016." + "Nog meer informatie over dit product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_page);

        //setting views
        textView1 = (TextView)findViewById(R.id.textView1);
        txtView = (TextView)findViewById(R.id.informatie);
        scrollView = (ScrollView)findViewById(R.id.scrollView1);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);


        txtView.setText(informatie_pagina_text);
        txtView.setMovementMethod(new ScrollingMovementMethod());



        bottomNavigationView.setSelectedItemId(R.id.navigation_info);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_info:
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

    public void mainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void historyPage(){
        Intent intent = new Intent(this, HistoryPage.class);
        startActivity(intent);
    }
}
