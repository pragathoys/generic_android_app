package com.pragathoys.lib.activities;

import com.pragathoys.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 *
 * @author marjohn
 */
public class Prefs extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        addPreferencesFromResource(R.xml.prefs);   
    }   
}