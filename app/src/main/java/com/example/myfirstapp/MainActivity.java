package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    private Context ctx = this;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private long lastSpawnedThread;

    private String unique_id;
    private ArrayList<String> history_array_kenteken;
    private ArrayList<String> history_array_auto;
    private ArrayList<String> history_array_kenteken_old;
    private ArrayList<String> history_array_auto_old;
    private kentekenMetAutoNaam history_array;
    private kentekenMetAutoNaam history_array_old;

    private TextView textView4;
    private TextView alignTextView;

    EditText editText;
    String text;

    // The suggestion list
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_info:
                            infoPage();

                            break;
                        case R.id.navigation_search:

                            makeViewVisible("textView2", "button", "autoCompleteTextView", "textView3", "imageView");
                            textView4.setText("");

                            break;
                        case R.id.navigation_history:
                            historyPage();

                            dataModels.clear();
                            adapter.notifyDataSetChanged();
                            editText.setText("");

                            break;
                    }
                    return true;
                }
            });

        SharedPreferences prefs = getSharedPreferences("unique_id", 0);
        Context context = getApplicationContext();


        if ((unique_id = prefs.getString("uuid", "")).isEmpty()){
            unique_id = String.valueOf(UUID.randomUUID());
            prefs.edit().putString("uuid", unique_id).commit();
            Log.d("Unique ID: ", unique_id);
        }
        else {
            Log.d("Unique ID: ", unique_id);
        }

        editText = (EditText)findViewById(R.id.autoCompleteTextView);

        // The suggestions
        listView=(ListView)findViewById(R.id.list);
        dataModels= new ArrayList<>();

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= dataModels.get(position);
                Log.d("kenteken: ", dataModel.getKenteken());
                sendMessageSuggestion(dataModel.getKenteken());
            }
        });

        alignTextView =(TextView) findViewById(R.id.alignTextView);
        Log.d("Grootte ", ""+(int)setAlignmentView());


        textView4 =(TextView) findViewById(R.id.textView4);
        makeViewInvisible("textView4");

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                alignTextView.setVisibility(View.GONE);
                RelativeLayout kentekencheck_view = (RelativeLayout) findViewById(R.id.kentekencheck_view);
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) kentekencheck_view.getLayoutParams();
                p.weight = 13;
                LinearLayout.LayoutParams x = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
                x.weight = 10;

                kentekencheck_view.setLayoutParams(p);
                relativeLayout.setLayoutParams(x);


            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                String input = editText.getText().toString();

                if(input.length() > 1 ) {
                    text = editText.getText().toString();
                    int length1 = text.length();
                    int length2 = text.replace("-", "").length();
                    System.out.println(text);
                    int difference = 0;
                    if(length1 - length2 == 1){ difference = 1; System.out.println(length2); System.out.println(difference);}
                    else if(length1 - length2 == 2){ difference = 2; System.out.println(length2); System.out.println(difference);}
                    else if(length1 - length2 > 2){ difference = 2; System.out.println(length2); System.out.println(difference);}


                    if(text.charAt(length1-1) == '-')
                    {
                        text = text.substring(0, length1-1);
                        System.out.println(text);
                        editText.setText(text);
                    }
                    if(length2 == 1) {
                        Log.d("if statement", "1");
                        editText.setText(text.replace("-", ""));
                    }
                    else if(length2 == 2 && difference == 0) {
                        Log.d("if statement", "2-0");
                        if(Character.isDigit(text.charAt(0)) && Character.isLetter(text.charAt(1))){
                            text = text.substring(0, 1)+"-"+text.substring(1);
                            System.out.println(text);
                            editText.setText(text);
                        }
                        else if(Character.isLetter(text.charAt(0)) && Character.isDigit(text.charAt(1)))
                        {
                            text = text.substring(0, 1)+"-"+text.substring(1);
                            System.out.println(text);
                            editText.setText(text);
                        }
                    }
                    else if(length2 == 2 && difference == 1) { /*do nothing*/ Log.d("if statement", "2-1"); }
                    else if(length2 == 3 && difference == 0) {
                        Log.d("if statement", "3-0");
                        if(Character.isDigit(text.charAt(1)) && Character.isLetter(text.charAt(2)) || Character.isLetter(text.charAt(1)) && Character.isDigit(text.charAt(2))){
                            text = text.substring(0, 2)+"-"+text.substring(2);
                            System.out.println(text);
                            editText.setText(text);
                        }
                    }
                    else if(length2 == 3 && difference == 1) {
                        Log.d("if statement", "3-1");
                        System.out.println(text);
                        if(Character.isDigit(text.charAt(1)) && Character.isLetter(text.charAt(3)) || Character.isLetter(text.charAt(1)) && Character.isDigit(text.charAt(3))){ /* do nothing */ }
                        else if(Character.isDigit(text.charAt(0)) && Character.isLetter(text.charAt(2)) || Character.isLetter(text.charAt(0)) && Character.isDigit(text.charAt(2))){ /* do nothing */ }
                    }
                    else if(length2 == 4 && difference == 0) {
                        Log.d("if statement", "4-0");
                        if(Character.isDigit(text.charAt(2)) && Character.isLetter(text.charAt(3)) || Character.isLetter(text.charAt(2)) && Character.isDigit(text.charAt(3))) {
                            text = text.substring(0, 3) + "-" + text.substring(3);
                            System.out.println(text);
                            editText.setText(text);
                        }
                        else if(Character.isDigit(text.charAt(2)) && Character.isDigit(text.charAt(3)) || Character.isLetter(text.charAt(2)) && Character.isLetter(text.charAt(3))){
                            text = text.substring(0, 2) + "-" + text.substring(2, 4);
                            System.out.println(text);
                            editText.setText(text);
                        }
                    }
                    else if(length2 == 4 && difference == 1) {
                        Log.d("if statement", "4-1");
                        System.out.println(text);
                        if(text.charAt(1) == '-' && Character.isDigit(text.charAt(3)) && Character.isLetter(text.charAt(4)) || text.charAt(1) == '-' && Character.isLetter(text.charAt(3)) && Character.isDigit(text.charAt(4))) {
                            text = text.substring(0, 4) + "-" + text.substring(4);
                            System.out.println(text);
                            editText.setText(text);
                        }
                    }
                    else if (length2 == 5 && difference == 1) {
                        Log.d("if statement", "5-1");
                        System.out.println(text);
                        if(text.charAt(1) == '-' && Character.isDigit(text.charAt(4)) && Character.isLetter(text.charAt(5)) || text.charAt(1) == '-' && Character.isLetter(text.charAt(4)) && Character.isDigit(text.charAt(5))){
                            text = text.substring(0, 5) + "-" + text.substring(5);
                            System.out.println(text);
                            editText.setText(text);
                        }
                        else if(text.charAt(1) == '-' && Character.isDigit(text.charAt(4)) && Character.isDigit(text.charAt(5)) || text.charAt(1) == '-' && Character.isLetter(text.charAt(4)) && Character.isLetter(text.charAt(5))){
                            text = text.substring(0, 5);
                            System.out.println(text);
                            editText.setText(text);
                        }
                        else if(text.charAt(2) == '-' && Character.isDigit(text.charAt(4)) && Character.isLetter(text.charAt(5)) || text.charAt(2) == '-' && Character.isLetter(text.charAt(4)) && Character.isDigit(text.charAt(5))){
                            text = text.substring(0, 5) + "-" + text.substring(5);
                            System.out.println(text);
                            editText.setText(text);
                        }
                    }
                    else if (length2 == 5 && difference == 2) {
                        Log.d("if statement", "5-1");
                        System.out.println(text);
                        if(text.charAt(1) == '-' && Character.isDigit(text.charAt(4)) && Character.isLetter(text.charAt(5)) || text.charAt(1) == '-' && Character.isLetter(text.charAt(4)) && Character.isDigit(text.charAt(5))){
                            text = text.substring(0, 5) + "-" + text.substring(5);
                            System.out.println(text);
                            editText.setText(text);
                        }
                        else if(text.charAt(1) == '-' && Character.isDigit(text.charAt(4)) && Character.isDigit(text.charAt(5)) || text.charAt(1) == '-' && Character.isLetter(text.charAt(4)) && Character.isLetter(text.charAt(5))){
                            text = text.substring(0, 5);
                            System.out.println(text);
                            editText.setText(text);
                        }
                        else if(text.charAt(2) == '-' && Character.isDigit(text.charAt(4)) && Character.isLetter(text.charAt(5)) || text.charAt(2) == '-' && Character.isLetter(text.charAt(4)) && Character.isDigit(text.charAt(5))){
                            text = text.substring(0, 5) + "-" + text.substring(5);
                            System.out.println(text);
                            editText.setText(text);
                        }
                    }
                    else if (length2 == 6 && difference == 1) {
                        Log.d("if statement", "6-1");
                        System.out.println(text);
                        Log.d("POLICIE", "HANDEN OMHOOG");
                        if(Character.isDigit(text.charAt(5)) && Character.isDigit(text.charAt(6)) || Character.isLetter(text.charAt(5)) && Character.isLetter(text.charAt(6))) {
                            text = text.substring(0, 5) + "-" + text.substring(5, 7);
                            System.out.println(text);
                            editText.setText(text);
                        }
                        else {
                            text = text.substring(0, 6) + "-" + text.substring(6);
                            System.out.println(text);
                            editText.setText(text);
                        }
                    }



                    editText.setSelection(editText.getText().length());
                        //if(Character.isDigit(text.charAt(0)))
                    Log.d("Text",(editText.getText()).toString());


                    String siteUrl = "https://api.voertuig.net/v2/html/kentekens/" + input.replace("-", "").toUpperCase() + "*?source=website";
                    Log.d("SiteUrl: ", siteUrl);
                    long time = System.currentTimeMillis();
                    lastSpawnedThread = time;
                    textView4.setText("resultaten");
                    makeViewVisible("textView4");
                    (new ParseURL(time)).execute(new String[]{siteUrl});
                }
                else {
                    dataModels.clear();
                    adapter.notifyDataSetChanged();
                    makeViewInvisible("textView4");
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                //editText.setText(text);
            }
        });
    }
        public class ParseURL extends AsyncTask<String, Void, String> {
        private long spawned;

        public ParseURL(long spawned) {
            this.spawned = spawned;
        }

            @Override
            protected String doInBackground(String... strings) {
                //StringBuffer buffer = new StringBuffer();
                try {
                    dataModels.clear();
                    Log.d("JSwa", "Connecting to [" + strings[0] + "]");
                    Document doc = Jsoup.connect(strings[0]).get();
                    Log.d("JSwa", "Connected to [" + strings[0] + "]");
                    // Get document (HTML page) title

                    // Get meta info
                    Elements metaElems = doc.select("div.voertuig");
                    for (Element metaElem : metaElems ) {

                        String kenteken = metaElem.getElementsByClass("kenteken").text();
                        String auto = metaElem.getElementsByTag("strong").text();

                        dataModels.add(new DataModel(kenteken, auto,"info"));
                    }

                } catch (Throwable t) {
                    t.printStackTrace();
                }
                return "moi";
            }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (lastSpawnedThread == spawned) {
                adapter = new CustomAdapter(dataModels,getApplicationContext());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
    public int dpToPixels(int dp){
        float density = getResources().getDisplayMetrics().density;
        return(int) (int)(dp * density);
    }

    public float setAlignmentView(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        return dpHeight/2;
    }

    public void sendMessageSuggestion(String kenteken){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, kenteken);
        startActivity(intent);

    }

    public void infoPage(){
        Intent intent = new Intent(this, InfoPage.class);
        startActivity(intent);
    }
    public void historyPage(){
        Intent intent = new Intent(this, HistoryPage.class);
        startActivity(intent);
    }

    // Called when user pressed the Check button
    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.autoCompleteTextView);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        Log.d("MSG ", message);
        startActivity(intent);

    }

    public void makeViewInvisible(String... views){
        for (int i = 0; i < views.length; ++i) {
            int viewID = getResources().getIdentifier(views[i], "id", getPackageName());
            View view = (View)findViewById(viewID);
            view.setVisibility(View.INVISIBLE);
        }
    }
    public void makeViewVisible(String... views){
        for (int i = 0; i < views.length; ++i) {
            int viewID = getResources().getIdentifier(views[i], "id", getPackageName());
            View view = (View)findViewById(viewID);
            view.setVisibility(View.VISIBLE);
        }
    }

    public kentekenMetAutoNaam saveArrayToSharedPreferences(String history_item_kenteken, String history_item_auto, String kentekenArrayName, String autoArrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        history_array_old = loadArrayFromSharedPreferences("history", "car", getApplicationContext());
        history_array_kenteken_old = history_array_old.getKenteken();
        history_array_auto_old = history_array_old.getAutoNaam();
        if(history_array_kenteken_old.contains(history_item_kenteken)){}
        else {
            history_array_kenteken_old.add(history_item_kenteken);
            history_array_auto_old.add(history_item_auto);
        }

        if(history_array_kenteken_old.size() > 10) {
            history_array_kenteken_old.remove(0);
            history_array_auto_old.remove(0);
        }

        editor.putInt(kentekenArrayName +"_size", history_array_kenteken_old.size());
        editor.putInt(autoArrayName +"_size", history_array_auto_old.size());
        int i = 0;
        for(String history_items: history_array_kenteken_old){
            editor.putString(kentekenArrayName + "_" + i, history_items);
            Log.d("ARRAY KENTEKEN OLD", history_items);
            i++;
        }
        for(String history_items: history_array_auto_old){
            editor.putString(autoArrayName + "_" + i, history_items);
            Log.d("ARRAY AUTO OLD", history_items);
            i++;
        }

        Log.d("ArraySize", ""+history_array_kenteken_old.size());
        editor.commit();
        return  new kentekenMetAutoNaam(history_array_kenteken_old, history_array_auto_old);
    }

    public kentekenMetAutoNaam loadArrayFromSharedPreferences(String kentekenArrayName, String autoArrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(kentekenArrayName + "_size", 0);
        ArrayList<String> array = new ArrayList<String>(size);
        ArrayList<String> array2 = new ArrayList<String>(size);
        for(int i=0;i<size;i++) {
            array.add(prefs.getString(kentekenArrayName + "_" + i, null));
            array2.add(prefs.getString(autoArrayName + "_" + i, null));
        }
        return new kentekenMetAutoNaam(array, array2);
    }

    public class kentekenMetAutoNaam {
        private ArrayList<String> kentekens; //array1
        private ArrayList<String> autonamen; //array2

        public kentekenMetAutoNaam(ArrayList<String> kentekens, ArrayList<String> autonamen)
        {
            this.kentekens = kentekens;
            this.autonamen = autonamen;
        }

        public ArrayList<String> getKenteken() {
            return kentekens;
        }

        public ArrayList<String> getAutoNaam() {
            return autonamen;
        }
    }

    /*public String formatKenteken(String kenteken){
        int length = kenteken.length();
        if(Character.isLetter(length-1) && Character.isDigit(length-2)){
            kenteken.substring(length-2)

        }
    }*/
}
