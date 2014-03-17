package com.pragathoys;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class About extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Log.d("LOG_APP", "About Activity ...");
    }    
}