package com.pragathoys.lib.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.pragathoys.R;
import com.pragathoys.lib.controllers.Crud;

/**
 *
 * @author marjohn
 */
public class List extends ListActivity {
    Crud crud;
    SimpleCursorAdapter sca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        crud = new Crud("myDB", this);
        Cursor c = crud.list("generic_table");

        // Set the ListAdapter
        sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[]{"param"}, new int[]{android.R.id.text1});
        setListAdapter(sca);
        
        // Add action Listener to quick search
        EditText et = (EditText)findViewById(R.id.etQuickSearch);
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //sca.getFilter().filter(cs);   
                Cursor c = crud.list("generic_table",new String[]{"param"},new String[]{cs.toString()});
                sca.changeCursor(c);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                          
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("LIST_VIEW", "List item clicked " + id + " at position " + position);

        Intent intent = new Intent(this, Form.class);
        intent.putExtra("id", new Long(id));
        intent.putExtra("position", position);
        intent.putExtra("mode", Crud.MODE_UPDATE);

        startActivityForResult(intent, Crud.MODE_UPDATE);

//    startActivityForResult(i, ACTIVITY_EDIT);       
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.d("LIST", "Returned from Form:: requestCode: " + requestCode + ",resultCode: " + resultCode);

        Cursor c;
        EditText et = (EditText)findViewById(R.id.etQuickSearch);
        if(et.getText().toString()!=""){
            c = crud.list("generic_table",new String[]{"param"},new String[]{et.getText().toString()});
        }else{
            c = crud.list("generic_table");    
        }
        
        sca.changeCursor(c);
        
        // Set the ListAdapter
//        sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[]{"param"}, new int[]{android.R.id.text1});        
//        setListAdapter(sca);
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

            intent.putExtra("id", new Long(0));
            intent.putExtra("position", 0);
            intent.putExtra("mode", Crud.MODE_NEW);
            startActivityForResult(intent, Crud.MODE_NEW);

            // Retrieve PReferences
            //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            //boolean option_1 = settings.getBoolean("gaa_option_1", false);
        }
        return super.onOptionsItemSelected(item);
    }

}
