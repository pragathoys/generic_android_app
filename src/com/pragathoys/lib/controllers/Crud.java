package com.pragathoys.lib.controllers;

import android.app.Activity;
import android.database.Cursor;
import static android.view.ViewDebug.RecyclerTraceType.values;
import com.pragathoys.R.id;

/**
 *
 * @author marjohn
 */
public class Crud {
    
    private Db db;
    String tables[];
    String db_name;
    Activity activity;

    public  final static int MODE_NEW = 1;
    public  final static int MODE_UPDATE = 2;
    
    public Crud(String db_name,Activity activity){
        this.db_name = db_name;
        this.activity = activity;
        
        tables= new String[]{
            "generic_table"
        };
    }
    
    public Cursor list(String table_name){
        Cursor c;
        db = new Db(db_name, activity);
        c = db.select("SELECT * FROM " + table_name);
        db.close();
        
        return c;
    }

    public Cursor list(String table_name,String[] fields, String[] values){
        Cursor c;
        db = new Db(db_name, activity);
        String filter_list = "";
        for(int i=0;i<fields.length;i++){
            if(filter_list == ""){
                filter_list = fields[i] + " LIKE '%" + values[i] + "%'";
            }else{
                filter_list += " OR " + fields[i] + " LIKE '%" + values[i] + "%'";
            }
        }        
        c = db.select("SELECT * FROM " + table_name + " WHERE " + filter_list);
        db.close();
        
        return c;
    }
    
    public Cursor load(String table_name, long id){
        Cursor c;
        db = new Db(db_name, activity);
        c = db.select("SELECT * FROM " + table_name + " WHERE _id=" + id);
        db.close();
        
        return c;
    }       

    public boolean add(String[] fields, String[] values){
        boolean result = true;
        db = new Db(db_name, activity);
        String field_list = "", values_list="";
        for(int i=0;i<fields.length;i++){
            if(field_list == ""){
                field_list = fields[i];
            }else{
                field_list += "," + fields[i];
            }
        }
        
        for(int i=0;i<values.length;i++){
            if(values_list == ""){
                values_list = "'" + values[i] + "'";
            }else{
                values_list += ",'" + values[i] + "'";
            }
        }        
        db.insert(new String[]{"INSERT INTO generic_table("+field_list+") VALUES("+values_list+");"});
        db.close();
        
        return result;
    }  
    
    public boolean update(long id,String[] fields, String[] values){
        boolean result = true;
        db = new Db(db_name, activity);
        String field_list = "";
        for(int i=0;i<fields.length;i++){
            if(field_list == ""){
                field_list = fields[i] + "='" + values[i] + "'";
            }else{
                field_list += "," + fields[i] + "='" + values[i] + "'";
            }
        }
        
        db.update(new String[]{"UPDATE generic_table set "+field_list + " WHERE _id=" + id +" ;"});
        db.close();
        
        return result;
    }               
    
    public boolean delete(String table_name,long id){
        boolean result = true;
        db = new Db(db_name, activity);
        db.update(new String[]{"DELETE FROM " + table_name + " WHERE _id=" + id + ";"});
        db.close();

        return result;
    }
}