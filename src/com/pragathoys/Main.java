package com.pragathoys;

import com.pragathoys.lib.activities.About;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.pragathoys.lib.activities.Form;
import com.pragathoys.lib.activities.List;
import com.pragathoys.lib.activities.Prefs;
import com.pragathoys.lib.controllers.Db;
import com.pragathoys.lib.controllers.Crud;
import com.pragathoys.lib.net.Rest;
import java.util.Map;




public class Main extends Activity
{
    /** Called when the activity is first created.
     * @param savedInstanceState */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Log.d("LOG_APP", "Starting app ...");
        
        Db db;
        db = new Db("myDB2",this);
        db.init_schema();        
        db.close();
        
        // Add Listener to the View List button
        Button btnViewList = (Button) this.findViewById(R.id.btnViewList);
        btnViewList.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                Intent intent = new Intent(Main.this, List.class);
                startActivity(intent);
            }
        
        });
        
        // Add Listener to the View List button
        Button btnNewItem = (Button) this.findViewById(R.id.btnNewItem);
        btnNewItem.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
           //Log.d("LOG_APP", "Preferences");
            Intent intent = new Intent(Main.this, Form.class);

            intent.putExtra("id", new Long(0));
            intent.putExtra("position", 0);
            intent.putExtra("mode", Crud.MODE_NEW);
            startActivityForResult(intent, Crud.MODE_NEW);                
            }
        
        });        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                Intent intent = new Intent(Main.this, List.class);
                startActivity(intent);

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
            //Log.d("LOG_APP", "Preferences");
            Intent intent = new Intent(this, Prefs.class);
            startActivity(intent);
            
            // Retrieve PReferences
            //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            //boolean option_1 = settings.getBoolean("gaa_option_1", false);
                    
            
        }else if(item.getItemId() == R.id.mnu_about){
            //Log.d("LOG_APP", "About page");
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            
        }else if(item.getItemId() == R.id.mnu_sync){
            Log.d("LOG_APP", "Sync");
//            Intent intent = new Intent(this, About.class);
//            startActivity(intent);

            try{
                // Read Preferences
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                String server_ip = settings.getString("gaa_option_3", "192.168.0.1");
                String server_port = settings.getString("gaa_option_4", "3000");
                String api_key = settings.getString("gaa_option_5", "123456789");
                
                Rest rest = new Rest(Rest.PROTOCOL_HTTP,server_ip,server_port);
                Map<String, String> reply = rest.authenticate(api_key);
                Log.d("REST", "status " + reply.get("status"));
                Log.d("REST", "content " + reply.get("content"));
                
            }catch(Exception ex){
                Log.d("LOG_APP", "ERROR Syncing");
            }
        }
        return super.onOptionsItemSelected(item); 
    }   
}