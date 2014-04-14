package com.pragathoys.lib.controllers;

import android.app.Activity;
import android.content.ContentValues;
import static android.content.Context.MODE_PRIVATE;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 * @author marjohn
 */
public class Db {
    private SQLiteDatabase db;
    private String db_name;
    private Activity activity;
    
    private String db_schema[];
    
    public Db(String db_name,Activity activity){
        this.db_name = db_name;
        this.activity = activity;
        
        // Initialize the db
        db = activity.openOrCreateDatabase(db_name,MODE_PRIVATE,null);       
        
        // Define the schema of the db
        db_schema = new String[] {
            "CREATE TABLE IF NOT EXISTS generic_table(_id INTEGER PRIMARY KEY AUTOINCREMENT, param VARCHAR);",
            "CREATE TABLE IF NOT EXISTS images(_id INTEGER PRIMARY KEY AUTOINCREMENT, img BLOB,thumb BLOB, filename VARCHAR, created_at TIMESTAMP, updated_at TIMESTAMP);",
        };            
    }
    
    public void init_schema(){        
        for(int i=0;i<db_schema.length;i++){
            db.execSQL(db_schema[i]);
        }
    }
    
    public void insert(String sql[]){
        for(int i=0;i<sql.length;i++){
            db.execSQL(sql[i]);
        }        
    }
    
    public void insert_image(String table_name, ContentValues cv){        
          db.insert(table_name, null, cv);
    }    
    
    public void update(String sql[]){
        for(int i=0;i<sql.length;i++){
            db.execSQL(sql[i]);
        }        
    }    
    
    public Cursor select(String sql){
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        
        return c;
    }
    
    public Cursor select_args(String sql,String[] args){
        Cursor c = db.rawQuery(sql, args);
        c.moveToFirst();
        
        return c;
    }    
    
    public Cursor select_images(String table_name,String[] columns,String selection,String[] selectionArgs){
        Cursor c = db.query(true,table_name,columns, selection, selectionArgs, null, null, null, null);
        return c;
    }    
    
    public void init_db(){
        // Delete DB
        activity.deleteDatabase(db_name);
        // Recreate DB
        db = activity.openOrCreateDatabase(db_name,MODE_PRIVATE,null); 
        // init DB schema
        init_schema();
    }
    
    public void close(){
        db.close();
    }   
}