package com.pragathoys.lib.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.pragathoys.R;
import com.pragathoys.lib.controllers.Db;

public class Form extends Activity {

    private long id = 0;
    private long position = 0;
    private int mode = 1;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        //Log.d("LOG_APP", "Starting app ...");

        try {
            id = getIntent().getLongExtra("id", 0);
        } catch (Exception ex) {
            id = getIntent().getIntExtra("id", 0);
        }
        position = getIntent().getIntExtra("position", 0);
        mode = getIntent().getIntExtra("mode", 1);

        if (mode == 2) {
            // Update then load the content of the form
            Db db = new Db("myDB", this);
            Cursor c = db.select("SELECT * FROM generic_table WHERE _id=" + id);
            String param_value = c.getString(c.getColumnIndex("param"));
            ((EditText) findViewById(R.id.etParam)).setText(param_value);
            db.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formmenu, menu);
        
        
        if (mode == 2) {
            menu.getItem(0).setTitle(R.string.update);
        } else {
            menu.getItem(0).setTitle(R.string.new_item);
            menu.getItem(1).setEnabled(false);
        }        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnu_update) {
                String param_value = ((EditText) findViewById(R.id.etParam)).getText().toString();
            
            if (mode == 1) {
                Log.d("FORM", "Create item ");
                Db db = new Db("myDB", this);
                db.update(new String[]{"INSERT INTO generic_table(param) VALUES('" + param_value + "');"});
                db.close();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            } else {
                Log.d("FORM", "Update item " + id);
                Db db = new Db("myDB", this);
                db.update(new String[]{"UPDATE generic_table set param='"+param_value+"' where _id=" + id + ";"});
                db.close();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        } else if (item.getItemId() == R.id.mnu_delete) {
            Log.d("FORM", "Delete item " + id);

            Db db = new Db("myDB", this);
            db.update(new String[]{"delete from generic_table where _id=" + id + ";"});
            db.close();
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    boolean validate_form() {
        boolean result = false;
        //TODO
        result = true;
        return result;
    }
}
