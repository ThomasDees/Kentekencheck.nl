package com.example.myfirstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HistoryPage extends AppCompatActivity{

    private String unique_id;
    ArrayList<DataModel> gekochte_producten_list;
    ArrayList<DataModel> gratis_producten_list;
    private static CustomAdapter gekocht_adapter;
    private static CustomAdapter gratis_adapter;
    TextView textView1;
    TextView textView2;
    ListView gekochte_producten;
    ListView gratis_producten;
    BottomNavigationView bottomNavigationView;

    /** Informatie pagina text
     * \r\n\ om een regel over te slaan
     * om het overzichtelijk te houden wordt string + string + string... aangehouden
     * dit hoeft niet persee.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

        SharedPreferences prefs = getSharedPreferences("unique_id", 0);
        unique_id = prefs.getString("uuid", "");

        //setting views
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        gekochte_producten = (ListView) findViewById(R.id.gekochte_producten);
        gratis_producten = (ListView) findViewById(R.id.gratis_producten);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        textView1.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);
        gekochte_producten.setVisibility(View.INVISIBLE);
        gratis_producten.setVisibility(View.INVISIBLE);

        //padding for old android devices


        bottomNavigationView.setSelectedItemId(R.id.navigation_history);

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
                                break;
                        }
                        return true;
                    }

                });


        gekochte_producten_list= new ArrayList<>();
        gratis_producten_list= new ArrayList<>();

        System.out.println(gratis_producten_list);
        System.out.println(gekochte_producten_list);
        System.out.println(gratis_producten_list.isEmpty());

        gekocht_adapter= new CustomAdapter(gekochte_producten_list,getApplicationContext());
        gratis_adapter= new CustomAdapter(gratis_producten_list,getApplicationContext());
        gekochte_producten.setAdapter(gekocht_adapter);
        gratis_producten.setAdapter(gratis_adapter);

        long time = System.currentTimeMillis();
        (new JSONClass(time)).execute(new String[]{"https://www.kentekencheck.nl/api/app_history.php?ref="+unique_id});


    }
    public class JSONClass extends AsyncTask<String, Void, String> {
        private long spawned;

        public JSONClass(long spawned) {
            this.spawned = spawned;
        }

        @Override
        protected String doInBackground(String... url) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(url[0]);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.connect();
                int status = c.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        Gson gson = new Gson();
                        String data=sb.toString();
                        JsonParser parser = new JsonParser();
                        JsonElement jsonElement = parser.parse(sb.toString());
                        JsonArray history_gegevens = jsonElement.getAsJsonArray();

                        String plate;
                        String display_plate;
                        String basisrapport;
                        String schaderapport;
                        String tellerrapport;
                        String eigenaren;
                        String waarderapport;
                        String url_pdf;
                        String url_html;
                        ArrayList<String> display_plate_array = new ArrayList<String>();
                        System.out.println("Array: "+history_gegevens);

                        for (int i = 0; i < history_gegevens.size(); i++) {
                            JsonObject history_plate = (JsonObject) history_gegevens.get(i);
                            plate = history_plate.get("plate").toString();
                            display_plate = history_plate.get("display_plate").toString();
                            basisrapport = history_plate.get("plate").toString();
                            schaderapport = history_plate.get("plate").toString();
                            tellerrapport = history_plate.get("plate").toString();
                            eigenaren = history_plate.get("plate").toString();
                            waarderapport = history_plate.get("plate").toString();
                            url_pdf = history_plate.get("url_pdf").toString();
                            url_html = history_plate.get("url_html").toString();
                            display_plate_array.add(display_plate);
                            if (basisrapport.equals("1") && schaderapport.equals("0") && tellerrapport.equals("0") && waarderapport.equals("0")) {
                                gekochte_producten_list.add(new DataModel(display_plate_array.get(i).replace("\"", ""), "audi", "info"));
                                gratis_producten_list.add(new DataModel(display_plate_array.get(i).replace("\"", ""), "Skoda", "info"));
                            } else {
                                gratis_producten_list.add(new DataModel(display_plate_array.get(i).replace("\"", ""), "Ferrari", "info"));
                            }
                            Log.d("display_plate_array", display_plate_array.get(i));

                        }

                        //gekochte_producten_list.add(new DataModel("GT-200-D", "Volvo","info"));
                        //gekochte_producten_list.add(new DataModel("AU-245-A", "BMW","info"));
                        //gekochte_producten_list.add(new DataModel("HJ-356-Y", "Toyota","info"));
                        //gekochte_producten_list.add(new DataModel("GH-300-T", "Mazda","info"));
                        //gratis_producten_list.add(new DataModel("GT-200-D", "Volvo","info"));
                        //gratis_producten_list.add(new DataModel("AU-245-A", "BMW","info"));
                        //gratis_producten_list.add(new DataModel("HJ-356-Y", "Toyota","info"));
                        //gratis_producten_list.add(new DataModel("GH-300-T", "Mazda","info"));

                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("policie", "moi");
            gekocht_adapter = new CustomAdapter(gekochte_producten_list,getApplicationContext());
            gratis_adapter = new CustomAdapter(gratis_producten_list,getApplicationContext());
            gekochte_producten.setAdapter(gekocht_adapter);
            gratis_producten.setAdapter(gratis_adapter);
            gekocht_adapter.notifyDataSetChanged();
            gratis_adapter.notifyDataSetChanged();
            if(gekochte_producten_list.isEmpty() && gratis_producten_list.isEmpty()){
                Log.d("1", "moi");
                textView1.setVisibility(View.VISIBLE);
                textView1.setText("Nog geen kenteken opgezocht");
                textView2.setVisibility(View.GONE);
                gekochte_producten.setVisibility(View.GONE);
                gratis_producten.setVisibility(View.GONE);
            }
            else if(gekochte_producten_list.isEmpty() && !gratis_producten_list.isEmpty())
            {
                Log.d("2", "moi");
                textView1.setVisibility(View.GONE);
                gekochte_producten.setVisibility(View.GONE);
                textView2.setVisibility(View.VISIBLE);
                gratis_producten.setVisibility(View.VISIBLE);
            }
            else {
                Log.d("3", "moi");
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                gekochte_producten.setVisibility(View.VISIBLE);
                gratis_producten.setVisibility(View.VISIBLE);
            }
        };
    }

    public void InfoPage(){
        Intent intent = new Intent(this, InfoPage.class);
        startActivity(intent);
    }
    public void mainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
