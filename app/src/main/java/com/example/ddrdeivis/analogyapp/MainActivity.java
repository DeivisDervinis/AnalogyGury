package com.example.ddrdeivis.analogyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    // Declaring variables for the application
    protected Button btn_submit;
    protected TextView txt_analogy;
    protected EditText ed_input;
    protected String text = "";
    protected String[] words;
    protected String[] input_words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn_submit = findViewById(R.id.btn_submit);
        txt_analogy = findViewById(R.id.txt_analogy);
        ed_input = findViewById(R.id.ed_input);

        // Loads the file
        loadFile();

        // Splits by new line
        words = text.split("\\r?\\n");

        ed_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    txt_analogy.setText("");
                    txt_analogy.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_input.getText().toString().equals(" ") || ed_input.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Text Box is Empty!", Toast.LENGTH_SHORT).show();
                }else {
                    String input_string = ed_input.getText().toString();
                    input_words = input_string.split(" ");

                    int[] listHits = searchList(input_words);
                    int largestNumber = findLargestValue(listHits);
                    if (largestNumber == -1) {
                        Toast.makeText(getApplicationContext(), "No Analogies Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        //Log.d("LARGEST NUM", "Largest Num");

                        txt_analogy.setVisibility(View.VISIBLE);
                        txt_analogy.setText(words[largestNumber]);
                        //Log.d("LARGEST NUM", "Largest Num");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent intent = new Intent(this, CreateAnalogyActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public int[] searchList(String [] input_list){
        int [] countSum = new int[words.length];
        int num;

        // Counts the number of words occurring within a list
        for(int i = 0; i < input_list.length; i++){
            for(int j = 0; j < words.length; j++){
                String in = words[j].toLowerCase();
                num = 0;
                Pattern p = Pattern.compile(input_list[i].toLowerCase());
                Matcher m = p.matcher(in);
                if(m.find()){
                    num++;
                }else{
                    num = 0;
                }
                countSum[j] += num;
            }
        }
        return countSum;
        //Log.d("CREATION", "Logged");
    }

    public int findLargestValue(int[] list){
        if ( list == null || list.length == 0 ) return -1;
        int falseFlagHits = 0;


        // Checks if list is valid
        for(int j = 0; j < list.length; j++){
            if(list[j] == 0){
                falseFlagHits++;
            }
        }
        // returns false if the list is invalid
        if(falseFlagHits == list.length){
            return -1;
        }

        int largest = 0;
        for ( int i = 1; i < list.length; i++ )
        {
            if ( list[i] > list[largest] ) largest = i;
        }
        return largest;
    }

    // Loads a file to use
    public void loadFile(){
        try {
            InputStream is = getAssets().open("list.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
