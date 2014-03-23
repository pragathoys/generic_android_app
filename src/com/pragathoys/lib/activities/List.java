package com.pragathoys.lib.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.pragathoys.R;
import com.pragathoys.lib.controllers.Db;

/**
 *
 * @author marjohn
 */
public class List extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        Db db = new Db("myDB", this);
        db.init_schema();

        Cursor c = db.select("select * from generic_table");
        db.close();
        // Set the ListAdapter
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[]{"param"}, new int[]{android.R.id.text1});
        setListAdapter(sca);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("LIST_VIEW", "List item clicked " + id + " at position " + position);

        Intent intent = new Intent(this, Form.class);
        intent.putExtra("id", new Long(id));
        intent.putExtra("position", position);
        int mode = 2; // Edit
        intent.putExtra("mode", mode);

        startActivityForResult(intent, mode);

//    startActivityForResult(i, ACTIVITY_EDIT);       
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.d("LIST", "Returned from Form:: requestCode: " + requestCode + ",resultCode: " + resultCode);
        
        Db db = new Db("myDB", this);
        Cursor c = db.select("select * from generic_table");
        db.close();
        // Set the ListAdapter
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[]{"param"}, new int[]{android.R.id.text1});
        setListAdapter(sca);
        
        
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
            // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnu_newitem) {
            //Log.d("LOG_APP", "Preferences");
            Intent intent = new Intent(this, Form.class);

            int mode = 1; // New
            intent.putExtra("id", new Long(0));
            intent.putExtra("position", 0);
            intent.putExtra("mode", mode);
            startActivityForResult(intent, mode);

            // Retrieve PReferences
            //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            //boolean option_1 = settings.getBoolean("gaa_option_1", false);
        }
        return super.onOptionsItemSelected(item);
    }

}
