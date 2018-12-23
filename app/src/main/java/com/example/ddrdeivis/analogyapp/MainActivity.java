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
    // Declaring elements of the app
    protected Button btn_submit;
    protected TextView txt_analogy;
    protected EditText ed_input;

    // Declaration of variables needed for analogy manipulation
    protected String text = "";
    protected String[] words;
    protected String[] input_words;


    // Method that runs when app opens
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Disables the focus of the editView, hides the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initializes the elements
        btn_submit = findViewById(R.id.btn_submit);
        txt_analogy = findViewById(R.id.txt_analogy);
        ed_input = findViewById(R.id.ed_input);

        // Loads the file
        loadFile();

        // Splits by new line
        words = text.split("\\r?\\n");

        // Adds a text watcher for the editBox
        ed_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If the length of string is empty clears the analogy text
                if(s.length() != 0){
                    txt_analogy.setText("");
                    txt_analogy.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Sets an on click listener for the button
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If its empty returns a toast
                if(ed_input.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Text Box is Empty!", Toast.LENGTH_SHORT).show();
                }else {
                    // If its not empty proceeds with the anology finder
                    // Get the input from the text box
                    String input_string = ed_input.getText().toString();

                    // Splits by spaces
                    input_words = input_string.split(" ");

                    // Gets the searchList count
                    int[] listHits = searchList(input_words);

                    // Find the largest number within the list
                    int largestNumber = findLargestValue(listHits);

                    // If it's -1 shows a toast of "Analogies not found"
                    if (largestNumber == -1) {
                        Toast.makeText(getApplicationContext(), "No Analogies Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Makes text visible, and updates the text to specific analogy
                        txt_analogy.setVisibility(View.VISIBLE);
                        txt_analogy.setText(words[largestNumber]);
                    }
                }
            }
        });
    }

    // Method for settings button in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Method for selecting a button on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gets the id of selected item
        int id = item.getItemId();

        // Checks which one was selected
        if(id == R.id.action_settings){
            // Starts a new intent
            Intent intent = new Intent(this, CreateAnalogyActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // Method for searching a list of analogies
    public int[] searchList(String [] input_list){
        // An array for picking the right analogy
        int [] countSum = new int[words.length];

        // Counter that will be added to count sum for later use
        int num;

        // Counts the number of words occurring within a list for each input word, for each word within list
        for(int i = 0; i < input_list.length; i++){
            for(int j = 0; j < words.length; j++){
                // Puts everything to lower case
                String in = words[j].toLowerCase();
                num = 0;
                // Sets up the pattern and the matcher
                Pattern p = Pattern.compile(input_list[i].toLowerCase());
                Matcher m = p.matcher(in);

                // Checks if it exists
                if(m.find()){
                    num++;
                }else{
                    num = 0;
                }
                countSum[j] += num;
            }
        }
        // Returns the count list
        return countSum;
    }

    // Method that find the largest value within the list for analogy picking
    public int findLargestValue(int[] list){
        // Checks if the list is empty or not
        if ( list == null || list.length == 0 ) return -1;

        // Variable that calculates the flag hits if no words were found within the list
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

        // Finds the largest integer number
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
            // Gets a file from assets
            InputStream is = getAssets().open("list.txt");

            // Gets the size of the file
            int size = is.available();
            byte[] buffer = new byte[size];

            // Reads the file
            is.read(buffer);
            is.close();

            // Sets the data from the file to the text
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
