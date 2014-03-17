package com.pragathoys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Main extends Activity
{
    /** Called when the activity is first created.
     * @param savedInstanceState */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d("LOG_APP", "Starting app ...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mnu_preferences){
            Log.d("LOG_APP", "Preferences");
        }else if(item.getItemId() == R.id.mnu_about){
            //Log.d("LOG_APP", "About page");
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            
        }
        return super.onOptionsItemSelected(item); 
    }
    
    
            
            
    
    
}
