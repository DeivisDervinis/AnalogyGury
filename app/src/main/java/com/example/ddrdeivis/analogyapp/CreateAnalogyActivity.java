package com.example.ddrdeivis.analogyapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class CreateAnalogyActivity extends Activity {

    // Declares the variables
    protected Button btn_submit;
    protected EditText ed_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_analogy);

        // Initializes the variables
        btn_submit = findViewById(R.id.btn_submit);
        ed_data = findViewById(R.id.ed_input);

        // Sets an onclick listener for the data savings
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = ed_data.getText().toString();
                if(data.isEmpty() || data.equals("")){
                    Toast.makeText(getApplicationContext(), "Text Box is Empty!", Toast.LENGTH_LONG).show();
                }
                else{
                    write(data, getApplicationContext());
                }
            }
        });
    }

    // Method that writes data to the context
    public void write(String data, Context context){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("list.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
